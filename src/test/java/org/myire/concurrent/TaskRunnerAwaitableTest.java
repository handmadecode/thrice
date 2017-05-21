/*
 * Copyright 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;
import static org.junit.Assert.assertTrue;


/**
 * Unit tests for the {@code Awaitable} implementation part of
 * {@link org.myire.concurrent.TaskRunner}.

 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class TaskRunnerAwaitableTest extends AwaitableTest
{
    // Factory for all task execution threads created in the tests.
    static private final DaemonThreadFactory THREAD_FACTORY =
            new DaemonThreadFactory("TaskRunnerAwaitableTest-", true);


    // The instance to test will call run() in a daemon thread.
    private final TaskRunner fTaskRunner = new TaskRunner(this::run, THREAD_FACTORY);


    @Override
    public void setup()
    {
        try
        {
            // Start the task and wait for the execution thread to call run().
            if (fTaskRunner.startTask().await(10, TimeUnit.SECONDS))
                setup(fTaskRunner, this::terminate);
            else
                // Execution thread not started within 10 seconds, fail the test.
                throw new AssertionError("TaskRunner not started within 10 seconds");
        }
        catch (InterruptedException ie)
        {
            // Interrupted, abort wait for execution thread and restore interrupt status to
            // propagate it.
            Thread.currentThread().interrupt();
        }
    }


    /**
     * The {@code await()} method should not wait if the task hasn't been started.
     */
    @Test
    public void awaitDoesNotWaitIfTaskHasNotStarted() throws InterruptedException
    {
        // Given (the task has not started)
        TaskRunner aRunner = new TaskRunner(() -> {});

        // When (another thread calls await())
        BlockingTestAction aTask = new BlockingTestAction(aRunner::await);
        boolean aWasStarted = aTask.startAndAwaitRun();

        // Then (await() should have returned)
        assertTrue(aWasStarted);
        assertTrue(aTask.timedJoin(2, TimeUnit.SECONDS));
    }


    /**
     * The {@code await()} method should throw an {@code InterruptedException} if it is called from
     * a thread that has its interrupt status set, even if the task isn't running.
     */
    @Test
    public void awaitThrowsIfCallingThreadHasInterruptStatusSetAndTaskIsNotRunning() throws InterruptedException
    {
        // Given (the task has not started)
        TaskRunner aRunner = new TaskRunner(() -> {});

        // When (another thread has its interrupt status set and calls await())
        BlockingTestAction.BlockingCall aAwaitCall = () -> interruptAndCall(aRunner::await);
        BlockingTestAction aAction = new BlockingTestAction(aAwaitCall);
        boolean aWasStarted = aAction.startAndAwaitRun();

        // Then (await() should have thrown an InterruptedException and the calling thread should
        // have terminated)
        assertTrue(aWasStarted);
        assertTrue(aAction.timedJoin(1, TimeUnit.SECONDS));
        assertTrue(aAction.wasInterrupted());
    }


    /**
     * The {@code await(long, TimeUnit)} method should return true immediately if the task hasn't
     * been started.
     */
    @Test
    public void timedAwaitReturnsTrueIfTaskHasNotStarted() throws InterruptedException
    {
        // Given (the task has not started)
        TaskRunner aRunner = new TaskRunner(() -> {});

        // When (another thread calls await())
        TimedAwaitAction aTimedAwaitAction = new TimedAwaitAction(aRunner, 1, TimeUnit.DAYS);
        boolean aWasStarted = aTimedAwaitAction.startAndAwaitRun();

        // Then (await() should have returned true)
        assertTrue(aWasStarted);
        assertTrue(aTimedAwaitAction.timedJoin(100, TimeUnit.MILLISECONDS));
        assertTrue(aTimedAwaitAction.returnedTrue());
    }


    /**
     * The {@code await(long, TimeUnit)} method should throw an {@code InterruptedException} if it
     * is called from a thread that has its interrupt status set, even if the task is not running.
     */
    @Test
    public void timedAwaitThrowsIfCallingThreadHasInterruptStatusSetAndTaskIsNotRunning() throws InterruptedException
    {
        // Given (the task has not started)
        TaskRunner aRunner = new TaskRunner(() -> {});

        // When (another thread has its interrupt status set and calls await())
        BlockingTestAction.BlockingCall aAwaitCall = () -> aRunner.await(1, TimeUnit.DAYS);
        BlockingTestAction aAction = new BlockingTestAction(() -> interruptAndCall(aAwaitCall));
        boolean aWasStarted = aAction.startAndAwaitRun();

        // Then (await() should have thrown an InterruptedException and the calling thread should
        // have terminated)
        assertTrue(aWasStarted);
        assertTrue(aAction.timedJoin(1, TimeUnit.SECONDS));
        assertTrue(aAction.wasInterrupted());
    }


    /**
     * Run method for the task of {@code TaskRunner} being tested. This method waits for the current
     * thread to be interrupted.
     */
    private void run()
    {
        while (!Thread.currentThread().isInterrupted())
            LockSupport.park();
    }


    /**
     * Stop {@code TaskRunner} being tested.
     */
    private void terminate()
    {
        try
        {
            fTaskRunner.stopTask();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
