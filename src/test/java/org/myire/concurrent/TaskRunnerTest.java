/*
 * Copyright 2006, 2007, 2009-2011, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for {@link org.myire.concurrent.TaskRunner}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class TaskRunnerTest
{
    /**
     * The constructor should throw a {@code NullPointerException} when passed a null task
     * parameter.
     */
    @Test(expected = NullPointerException.class)
    public void ctorThrowsForNullTask()
    {
        new TaskRunner(null);
    }


    /**
     * The constructor should throw a {@code NullPointerException} when passed a null task parameter
     * and a non-null thread factory parameter.
     */
    @Test(expected = NullPointerException.class)
    public void ctorThrowsForNullTaskAndNonnullThreadFactory()
    {
        new TaskRunner(null, Executors.defaultThreadFactory());
    }


    /**
     * The constructor should throw a {@code NullPointerException} when passed a null thread factory
     * parameter and a non-null task parameter.
     */
    @Test(expected = NullPointerException.class)
    public void ctorThrowsForNullThreadFactory()
    {
        new TaskRunner(() -> {}, null);
    }


    /**
     * The {@code startTask()} method should start a new thread that executes the task.
     */
    @Test
    public void startTaskStartsNewThread() throws InterruptedException
    {
        // Given
        ConcurrentTestAction aTask = new ConcurrentTestAction();
        TaskRunner aRunner = new TaskRunner(aTask);

        // When
        aRunner.startTask();

        // Then
        assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));
        assertNotSame(Thread.currentThread(), aTask.getThread());
    }


    /**
     * The {@code startTask()} method should create the task execution thread using the
     * {@code ThreadFactory} passed to the constructor.
     */
    @Test
    public void startTaskUsesThreadFactoryPassedToConstructor() throws InterruptedException
    {
        // Given
        ThreadFactory aThreadFactory = spy(new UserThreadFactory("x", false));
        TaskRunner aRunner = new TaskRunner(new ConcurrentTestAction(), aThreadFactory);

        // When
        aRunner.startTask();

        // Then
        verify(aThreadFactory).newThread(any());
    }


    /**
     * The {@code startTask()} method should call the {@code prepareExecutionThread()} method before
     * starting the thread.
     */
    @Test
    public void startTaskCallsPrepareExecutionThread() throws InterruptedException
    {
        // Given
        ConcurrentTestAction aTask = new ConcurrentTestAction();
        TaskRunner aRunner = spy(new TaskRunner(aTask));

        // When
        aRunner.startTask();

        // Then (the task must be running before its getThread() method can return the execution
        // thread)
        assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));
        verify(aRunner).prepareExecutionThread(aTask.getThread());
    }


    /**
     * The {@code Awaitable} returned by the {@code startTask()} method should wait for the task
     * execution thread to be running.
     */
    @Test
    public void startTaskAwaitableAwaitsTaskExecutionThread() throws InterruptedException
    {
        // Given
        ConcurrentTestAction aTask = new ConcurrentTestAction();
        TaskRunner aRunner = new TaskRunner(aTask);

        // When
        Awaitable aAwaitable = aRunner.startTask();

        // Then (as soon as the task has started running, await() should return true immediately)
        assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));
        assertTrue(aAwaitable.await(0, TimeUnit.MICROSECONDS));
    }


    /**
     * Calling the {@code startTask()} method when the task already is running should not start a
     * second task execution thread.
     */
    @Test
    public void startTaskDoesNotStartThreadIfAlreadyRunning() throws InterruptedException
    {
        // Given
        CountDownLatch aLatch = new CountDownLatch(1);
        BlockingTestAction aTask = new BlockingTestAction(aLatch::await);
        TaskRunner aRunner = new TaskRunner(aTask);

        try
        {
            // Given (the task is running and blocked in CountDownLatch::await)
            aRunner.startTask();
            assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));
            Thread aTaskExecutionThread = aTask.getThread();

            // When (startTask() is called when already running)
            aRunner.startTask();

            // Then (waiting for an already running task should return immediately and the task
            // execution thread should be the same)
            assertTrue(aTask.awaitRun(0, TimeUnit.MILLISECONDS));
            assertSame(aTaskExecutionThread, aTask.getThread());
        }
        finally
        {
            // Release the task execution thread.
            aLatch.countDown();
        }
    }


    /**
     * Calling the {@code startTask()} method when the task has been started and terminated should
     * start the task again in a new task execution thread.
     */
    @Test
    public void startTaskRestartsIfAlreadyTerminated() throws InterruptedException
    {
        // Given
        ConcurrentTestAction aFirstAction = new ConcurrentTestAction();
        TaskProxy aProxy = new TaskProxy(aFirstAction);
        TaskRunner aRunner = new TaskRunner(aProxy);

        // Given (the task has started and terminated)
        aRunner.startTask();
        assertTrue(aFirstAction.awaitRun(10, TimeUnit.SECONDS));
        assertTrue(aFirstAction.timedJoin(10, TimeUnit.SECONDS));

        // Given (the task is reset for a second run)
        ConcurrentTestAction aSecondAction = new ConcurrentTestAction();
        aProxy.setDelegate(aSecondAction);

        // When (startTask() is called after previous run has terminated)
        aRunner.startTask();

        // Then (the task should be running again but in a new thread)
        assertTrue(aSecondAction.awaitRun(10, TimeUnit.SECONDS));
        assertNotSame(aFirstAction.getThread(), aSecondAction.getThread());
    }


    /**
     * The {@code stopTask()} method should wait for the task execution thread to terminate before
     * returning.
     */
    @Test
    public void stopTaskReturnsAfterExecutionThreadHasTerminated() throws InterruptedException
    {
        // Given (a task that will block in CountDownLatch::await)
        BlockingTestAction aTask = new BlockingTestAction(new CountDownLatch(1)::await);
        TaskRunner aRunner = new TaskRunner(aTask);

        // Given (the task is running)
        aRunner.startTask();
        assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));

        // When
        aRunner.stopTask();

        // Then (the task should have been interrupted and its thread have terminated before
        // stopTask() returns)
        assertTrue(aTask.wasInterrupted());
        assertFalse(aTask.getThread().isAlive());
    }


    /**
     * The {@code stopTask()} should do nothing if the task execution thread already has terminated.
     */
    @Test
    public void stopTaskDoesNothingIfThreadAlreadyHasTerminated() throws InterruptedException
    {
        // Given
        BlockingTestAction aTask = new BlockingTestAction();
        TaskRunner aRunner = new TaskRunner(aTask);

        // Given (the task has started and terminated).
        aRunner.startTask();
        assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));
        assertTrue(aTask.timedJoin(10, TimeUnit.SECONDS));

        // When
        aRunner.stopTask();

        // Then
        assertFalse(aTask.wasInterrupted());
        assertFalse(aTask.getThread().isAlive());
    }


    /**
     * The {@code stopTask()} method should throw an {@code InterruptedException} if it is
     * interrupted before the task execution thread has terminated.
     */
    @Test
    public void stopTaskThrowsIfInterrupted() throws InterruptedException
    {
        // Given
        CountDownLatch aLatch = new CountDownLatch(1);
        ConcurrentTestAction aUnresponsiveTask = new ConcurrentTestAction(() -> awaitUninterruptibly(aLatch));
        TaskRunner aRunner = new TaskRunner(aUnresponsiveTask);

        try
        {
            // Given (the unresponsive task is running)
            aRunner.startTask();
            assertTrue(aUnresponsiveTask.awaitRun(10, TimeUnit.SECONDS));

            // Given (another thread calls stopTask() and is blocked because the unresponsive task
            // ignores interrupts)
            BlockingTestAction aStopTask = new BlockingTestAction(aRunner::stopTask);
            assertTrue(aStopTask.startAndAwaitRun());

            // When (the stop task thread is interrupted)
            aStopTask.interruptRun();

            // Then (stopTask() should have thrown an InterruptedException and returned without
            // waiting for the task execution thread to terminate).
            assertTrue(aStopTask.timedJoin(10, TimeUnit.SECONDS));
            assertTrue(aStopTask.wasInterrupted());
            assertTrue(aRunner.isRunning());
        }
        finally
        {
            // Let the unresponsive task terminate.
            aLatch.countDown();
        }
    }


    /**
     * The {@code isRunning()} method should return false if the {@code TaskRunner} has not been
     * started.
     */
    @Test
    public void isRunningReturnsFalseWhenTaskHasNotStarted() throws InterruptedException
    {
        // Given
        TaskRunner aRunner = new TaskRunner(() -> {});

        // Then
        assertFalse(aRunner.isRunning());
    }


    /**
     * The {@code isRunning()} method should return true when the task execution thread hasn't
     * terminated.
     */
    @Test
    public void isRunningReturnsTrueWhenTaskIsRunning() throws InterruptedException
    {
        // Given
        CountDownLatch aLatch = new CountDownLatch(1);
        BlockingTestAction aTask = new BlockingTestAction(aLatch::await);
        TaskRunner aRunner = new TaskRunner(aTask);

        try
        {
            // When (the task is running)
            aRunner.startTask();
            assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));

            // Then
            assertTrue(aRunner.isRunning());

        }
        finally
        {
            // Release the blocking call.
            aLatch.countDown();
        }
    }


    /**
     * The {@code isRunning()} method should return false when the {@code stopTask()} method has
     * returned.
     */
    @Test
    public void isRunningReturnsFalseWhenTaskHasStopped() throws InterruptedException
    {
        // Given
        BlockingTestAction aTask = new BlockingTestAction(new CountDownLatch(1)::await);
        TaskRunner aRunner = new TaskRunner(aTask);

        // Given (the task is running)
        aRunner.startTask();
        assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));

        // When
        aRunner.stopTask();

        // Then
        assertFalse(aRunner.isRunning());
    }


    /**
     * The {@code isRunning()} method should return false if the {@code startTask()} method throws
     * before starting the task execution thread.
     */
    @Test
    public void isRunningReturnsFalseIfStartTaskThrows() throws InterruptedException
    {
        // Given
        TaskRunner aRunner = spy(new TaskRunner(() -> {}));
        doThrow(new RuntimeException()).when(aRunner).prepareExecutionThread(any());

        // When (start the task and catch the exception thrown)
        try
        {
            aRunner.startTask();
            fail("startTask() did not throw");
        }
        catch (RuntimeException expected)
        {
            // Expected, and asserts follow.
        }

        // Then
        assertFalse(aRunner.isRunning());
    }


    /**
     * The task execution thread's uncaught exception handler should call the
     * {@code onUncaughtException()} method.
     */
    @Test
    public void crashingTaskCallsOnUncaughtException() throws InterruptedException
    {
        // Given
        RuntimeException aRuntimeException = new RuntimeException();
        ConcurrentTestAction aTask = new ConcurrentTestAction(() -> {throw aRuntimeException;});
        TaskRunner aRunner = spy(new TaskRunner(aTask));

        // When (start the task and wait for it to terminate due to the RuntimeException)
        aRunner.startTask();
        assertTrue(aTask.awaitRun(10, TimeUnit.SECONDS));
        assertTrue(aTask.timedJoin(10, TimeUnit.SECONDS));

        // Then
        verify(aRunner).onUncaughtException(aTask.getThread(), aRuntimeException);
    }


    /**
     * Call {@code await()} on a {@code CountDownLatch} and ignore all interrupts. This method will
     * not return until the latch's count reaches zero. Intended to be used as a
     * {@code BlockingCall} for a {@code BlockingTestAction}.
     *
     * @param pLatch    The latch to await.
     */
    static private void awaitUninterruptibly(CountDownLatch pLatch)
    {
        boolean aDone = false;
        while (!aDone)
        {
            try
            {
                pLatch.await();
                aDone = true;
            }
            catch (InterruptedException e)
            {
                // Ignore
            }
        }
    }


    /**
     * A {@code Runnable} that is a proxy for another {@code Runnable} that can be reassigned. The
     * {@code run()} method simply delegates to the other {@code Runnable}'s {@code run()} method.
     */
    static private class TaskProxy implements Runnable
    {
        private volatile Runnable fDelegate;

        TaskProxy(Runnable pDelegate)
        {
            fDelegate = pDelegate;
        }

        @Override
        public void run()
        {
            fDelegate.run();
        }

        void setDelegate(Runnable pDelegate)
        {
            fDelegate = pDelegate;
        }
    }
}
