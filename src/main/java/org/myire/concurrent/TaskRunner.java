/*
 * Copyright 2006, 2009-2011, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;


/**
 * A {@code TaskRunner} runs a {@code Runnable} in a separate thread, and provides means for
 * starting, stopping, and awaiting that thread.
 *<p>
 * The task execution thread will run until the {@code run()} method of the {@code Runnable}
 * returns. That thread can also be terminated explicitly from another thread by calling
 * {@link #stopTask()}. That method interrupts the task execution thread and then waits for it to
 * terminate by calling {@code Thread.join()}. The {@code Runnable} should therefore make sure that
 * it checks the current thread's interrupt status at regular intervals to detect if the task should
 * be explicitly terminated.
 *<p>
 * The task execution thread is created by a thread factory specified in the constructor. The
 * characteristics of this thread can further be tuned by overriding
 * {@link #prepareExecutionThread(Thread)}. That method is called from the thread that calls
 * {@link #startTask()} before the task execution thread is started. The default implementation of
 * {@link #prepareExecutionThread(Thread)} set the thread's {@code Thread.UncaughtExceptionHandler}
 * to {@link #onUncaughtException(Thread, Throwable)}, which by default does nothing. Subclasses may
 * want to override this method to take action when the task terminates abnormally.
 *<p>
 * This class is intended to be used for long-running tasks that may need to be stopped explicitly.
 * It is not intended to be used for repeated execution of many short-lived tasks, since a new
 * thread is created every time {@link #startTask()} is called. To execute many short-lived tasks,
 * one of the {@code java.util.concurrent.Executor} implementations is probably a better choice.
 *<p>
 * It is possible to restart a task that has been stopped by calling {@link #startTask()} again.
 * Whether or not the {@code Runnable} is reentrant is not the responsibility of the
 * {@code TaskRunner}, that must be guaranteed by the calling code.
 *<p>
 * Instances of this class are safe for use by multiple threads.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@ThreadSafe
public class TaskRunner implements Awaitable
{
    // The task to run.
    private final Runnable fTask;

    // The thread factory to create the task's execution thread with.
    private final ThreadFactory fThreadFactory;

    // The thread the task is running in (or did run in), null if the task hasn't ever been started.
    private volatile Thread fThread;

    // Monitor that must be held when starting and stopping the task execution thread.
    private final Object fThreadLock = new Object();

    // CountDownLatch that is counted down to zero when the task execution thread has started.
    private volatile CountDownLatch fThreadStartLatch;


    /**
     * Create a new {@code TaskRunner}. The task's thread factory will be a
     * {@code UserThreadFactory} with the class name of {@code pTask} as base name for the thread.
     *
     * @param pTask The task to run.
     *
     * @throws NullPointerException if {@code pTask} is null.
     */
    public TaskRunner(@Nonnull Runnable pTask)
    {
        this(pTask, new UserThreadFactory(pTask.getClass().getSimpleName(), false));
    }


    /**
     * Create a new {@code TaskRunner}.
     *
     * @param pTask             The task to run.
     * @param pThreadFactory    The thread factory to create the task execution thread with.
     *
     * @throws NullPointerException if any of the parameters is null.
     */
    public TaskRunner(@Nonnull Runnable pTask, @Nonnull ThreadFactory pThreadFactory)
    {
        fTask = requireNonNull(pTask);
        fThreadFactory = requireNonNull(pThreadFactory);
    }


    /**
     * Start running the task. This will create a new execution thread that will run the task passed
     * to the constructor by calling its {@code run()} method. The thread will execute until the
     * {@code run()} method returns or {@link #stopTask()} is called.
     *<p>
     * If the task already is running then this method does nothing and returns an {@code Awaitable}
     * that returns immediately from its {@code await} methods.
     *
     * @return  An {@code Awaitable} that awaits the task execution thread to call the task's
     *          {@code run()} method.
     */
    @Nonnull
    public Awaitable startTask()
    {
        synchronized (fThreadLock)
        {
            if (!isRunning())
            {
                // Not running, create and prepare the execution thread.
                fThread = fThreadFactory.newThread(this::runTask);
                prepareExecutionThread(fThread);

                // Create a CountdownLatch with count 1 that will reach 0 when the task execution
                // thread executes runTask(), and then start the thread.
                fThreadStartLatch = new CountDownLatch(1);
                fThread.start();
            }

            return Awaitables.wrap(fThreadStartLatch);
        }
    }


    /**
     * Stop the task. This method will try to terminate the task execution thread gracefully by
     * setting its interrupted status. The thread calling this method will then wait for the task
     * execution thread to terminate before returning from this method.
     *<p>
     * If the task currently isn't running then this method does nothing.
     *
     * @throws InterruptedException if the calling thread is interrupted while waiting for the task
     *                              execution thread to terminate.
     */
    public void stopTask() throws InterruptedException
    {
        synchronized (fThreadLock)
        {
            if (isRunning())
            {
                // The task execution thread may be in a blocking operation and thereby may need to
                // be interrupted to exit the blocking call. Calling interrupt() if the thread isn't
                // blocked will only set the thread's interrupted status.
                fThread.interrupt();

                // Wait for the task execution thread to terminate.
                fThread.join();
            }
        }
    }


    /**
     * Check if the task is running.
     *
     * @return  True if the task is running, false if not.
     */
    public boolean isRunning()
    {
        Thread aThread = fThread;
        return aThread != null && aThread.isAlive();
    }


    /**
     * Wait for the task execution thread to terminate. If the task currently is not running then
     * this method returns immediately.
     *<p>
     * If the task currently is running then the current thread becomes disabled for thread
     * scheduling purposes and lies dormant until one of two things happen:
     *<ul>
     * <li>The task execution thread terminates; or</li>
     * <li>Some other thread interrupts the current thread</li>
     *</ul>
     * If the current thread has its interrupted status set on entry to this method then an
     * {@link InterruptedException} is thrown immediately and the current thread's interrupted
     * status is cleared.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    @Override
    public void await() throws InterruptedException
    {
        if (Thread.interrupted())
            throw new InterruptedException();

        Thread aThread = fThread;
        if (aThread != null)
            aThread.join();
    }


    /**
     * Wait for the task execution thread to terminate. If the task currently is not running then
     * this method returns immediately with the value {@code true}.
     *<p>
     * If the task currently is running then the current thread becomes disabled for thread
     * scheduling purposes and lies dormant until one of three things happen:
     *<ul>
     * <li>The task execution thread terminates; or</li>
     * <li>Some other thread interrupts the current thread; or</li>
     * <li>The specified waiting time elapses</li>
     *</ul>
     * If the specified waiting time elapses before the task execution thread terminates then the
     * value {@code false} is returned.
     *<p>
     * If the current thread has its interrupted status set on entry to this method then an
     * {@link InterruptedException} is thrown immediately and the current thread's interrupted
     * status is cleared.
     *
     * @param pTimeout  The maximum time to wait. If the time is less than or equal to zero, the
     *                  method will not wait at all.
     * @param pUnit     The time unit of the {@code pTimeout} parameter.
     *
     * @return  {@code true} if the task execution thread has terminated, {@code false} if the
     *          waiting time elapsed before the thread terminated.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting.
     * @throws NullPointerException if {@code pUnit} is null.
     */
    @Override
    public boolean await(long pTimeout, @Nonnull TimeUnit pUnit) throws InterruptedException
    {
        if (Thread.interrupted())
            throw new InterruptedException();

        Thread aThread = fThread;
        if (aThread != null)
        {
            pUnit.timedJoin(aThread, pTimeout);
            return !aThread.isAlive();
        }
        else
            return true;
    }


    /**
     * Prepare the thread the task will run in. This method is called immediately before the thread
     * is started.
     *<p>
     * Subclasses can override this method to modify the thread's characteristics, such as giving
     * the thread another name than the one set by the thread factory specified in the constructor.
     *<p>
     * This base implementation sets the thread's {@link Thread.UncaughtExceptionHandler} to
     * {@link #onUncaughtException(Thread, Throwable)}.
     *
     * @param pThread   A task execution thread about to be started.
     *
     * @throws NullPointerException if {@code pThread} is null.
     */
    protected void prepareExecutionThread(@Nonnull Thread pThread)
    {
        pThread.setUncaughtExceptionHandler(this::onUncaughtException);
    }


    /**
     * Get notified that the task execution thread has thrown an uncaught exception and is about to
     * die. This implementation does nothing, subclasses may want to override to free up resources
     * or schedule a restart.
     *
     * @param pThread       The thread where the exception occurred, normally the task execution
     *                      thread.
     * @param pThrowable    The uncaught exception that caused the thread to die.
     *
     * @throws NullPointerException if any of the parameters is null.
     */
    protected void onUncaughtException(
            @SuppressWarnings("unused") @Nonnull Thread pThread,
            @SuppressWarnings("unused") @Nonnull Throwable pThrowable)
    {
        // No-op.
    }


    /**
     * Run the task passed to the constructor. This method is called from the task execution thread.
     */
    private void runTask()
    {
        // Signal that the task execution thread has started by counting down the latch returned by
        // startTask().
        fThreadStartLatch.countDown();

        // Run the task.
        fTask.run();
    }
}
