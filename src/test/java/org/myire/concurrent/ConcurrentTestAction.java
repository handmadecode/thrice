/*
 * Copyright 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


/**
 * Base class for test actions that are run in separate threads and need to be synchronized with
 * actions in the main test thread.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
class ConcurrentTestAction implements Runnable
{
    static private final ThreadFactory THREAD_FACTORY =
            new DaemonThreadFactory("ConcurrentTestAction", true);

    private final Runnable fRunnable;
    private final CountDownLatch fRunLatch = new CountDownLatch(1);
    private volatile Thread fThread;


    /**
     * Create a new {@code ConcurrentTestAction} with no {@code Runnable}. The {@code perform()}
     * method will effectively be a no-op.
     */
    ConcurrentTestAction()
    {
        this(null);
    }


    /**
     * Create a new {@code ConcurrentTestAction}.
     *
     * @param pRunnable  The runnable to invoke in {@link #perform()}, possibly null.
     */
    ConcurrentTestAction(Runnable pRunnable)
    {
        fRunnable = pRunnable;
    }


    /**
     * Store a reference to the current thread, release threads blocked in
     * {@link #awaitRun(long, TimeUnit)} and call {@link #perform()}. This method returns when the
     * call to {@link #perform()} returns, thereby terminating the executing thread.
     */
    @Override
    public void run()
    {
        fThread = Thread.currentThread();
        fRunLatch.countDown();
        perform();
    }


    /**
     * Perform the action.
     */
    protected void perform()
    {
        if (fRunnable != null)
            fRunnable.run();
    }


    /**
     * Create a new thread with this instance as target, start that thread and wait until the
     * {@code run()} method has been called by the new thread.
     *
     * @return  True if {@code run()} has been called, false if that hasn't happened within
     *          10 seconds after the thread was started.
     *
     * @throws InterruptedException if the calling thread is interrupted while waiting for the
     *                              {@code run()} method to been called.
     */
    boolean startAndAwaitRun() throws InterruptedException
    {
        THREAD_FACTORY.newThread(this).start();
        return awaitRun(10, TimeUnit.SECONDS);
    }


    /**
     * Wait for the {@link #run()} method to be called.
     *
     * @param pTimeout  The maximum time to wait.
     * @param pUnit     The unit of the timeout value.
     *
     * @return  True if the {@code run} method was called before the waiting time elapsed, false if
     *          not. True is also returned if {@code run} already has been called.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    boolean awaitRun(long pTimeout, TimeUnit pUnit) throws InterruptedException
    {
        return fRunLatch.await(pTimeout, pUnit);
    }


    /**
     * Interrupt the thread that is running this action. If this action hasn't been started in a
     * thread this method does nothing.
     */
    void interruptRun()
    {
        Thread aThread = fThread;
        if (aThread != null)
            aThread.interrupt();
    }


    /**
     * Wait for the thread that runs this action to terminate.
     *
     * @param pTimeout  The maximum time to wait. If the time is less than or equal to zero, the
     *                  method will not wait at all.
     * @param pUnit     The time unit of the {@code pTimeout} parameter.
     *
     * @return  True if the thread is terminated, false if the waiting time elapsed.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    boolean timedJoin(long pTimeout, TimeUnit pUnit) throws InterruptedException
    {
        Thread aThread = fThread;
        if (aThread == null)
            return false;

        pUnit.timedJoin(aThread, pTimeout);
        return !aThread.isAlive();
    }


    /**
     * Get the thread that (last) executed {@code run()}.
     *
     * @return  The execution thread, or null if {@code run()} hasn't been called yet.
     */
    Thread getThread()
    {
        return fThread;
    }
}
