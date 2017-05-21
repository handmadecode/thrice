/*
 * Copyright 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Base class for {@code Awaitable} implementation unit tests.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public abstract class AwaitableTest
{
    private Awaitable fAwaitable;
    private Runnable fSetConditionAction;


    /**
     * Call {@link #setup(Awaitable, Runnable)} with the instance to test.
     */
    @Before
    abstract public void setup();


    /**
     * Release any blocking call to {@code await()} by setting the condition that the
     * {@code Awaitable} waits for.
     */
    @After
    public void releaseAwait()
    {
        if (fSetConditionAction != null)
            fSetConditionAction.run();
    }


    /**
     * Set the {@code Awaitable} to test and a {@code Runnable} with a {@code run()} method that
     * will set the condition that the {@code Awaitable} is waiting to true.
     *<p>
     * This method must be called by the subclasses before executing a test.
     *
     * @param pAwaitable            The instance to test.
     * @param pSetConditionAction   The action to invoke to set the condition that the instance to
     *                              test is waiting for to true.
     */
    protected void setup(Awaitable pAwaitable, Runnable pSetConditionAction)
    {
        fAwaitable = pAwaitable;
        fSetConditionAction = pSetConditionAction;
    }


    /**
     * The {@code await()} method should return when the {@code Awaitable}'s underlying condition
     * becomes true.
     */
    @Test
    public void awaitReturnsWhenConditionBecomesTrue() throws InterruptedException
    {
        // Given (another thread is blocked in a call to await())
        BlockingTestAction aAction = new BlockingTestAction(fAwaitable::await);
        assertTrue(aAction.startAndAwaitRun());

        // When (the condition that await() is waiting for becomes true)
        fSetConditionAction.run();

        // Then (the thread calling await should have terminated without having been interrupted)
        assertTrue(aAction.timedJoin(2, TimeUnit.SECONDS));
        assertFalse(aAction.wasInterrupted());
    }


    /**
     * The {@code await} method should return immediately without waiting if the {@code Awaitable}'s
     * underlying condition already is true when the method is called.
     */
    @Test
    public void awaitDoesNotWaitIfConditionAlreadyIsTrue() throws InterruptedException
    {
        // Given (the underlying condition is true)
        fSetConditionAction.run();

        // When (another thread calls await())
        BlockingTestAction aAction = new BlockingTestAction(fAwaitable::await);
        assertTrue(aAction.startAndAwaitRun());

        // Then (the thread calling await should have terminated immediately without having been
        // interrupted)
        assertTrue(aAction.timedJoin(10, TimeUnit.MILLISECONDS));
        assertFalse(aAction.wasInterrupted());
    }


    /**
     * The {@code await()} method should throw an {@code InterruptedException} if it is interrupted
     * before the {@code Awaitable}'s underlying condition becomes true.
     */
    @Test
    public void awaitThrowsIfInterrupted() throws InterruptedException
    {
        // Given (another thread calls await() and is blocked in that call)
        BlockingTestAction aAction = new BlockingTestAction(fAwaitable::await);
        assertTrue(aAction.startAndAwaitRun());

        // When (the thread calling await() is interrupted)
        aAction.interruptRun();

        // Then (await() should have thrown an InterruptedException and the calling thread should
        // have terminated)
        assertTrue(aAction.timedJoin(10, TimeUnit.SECONDS));
        assertTrue(aAction.wasInterrupted());
    }


    /**
     * The {@code await()} method should throw an {@code InterruptedException} if it is called from
     * a thread that has its interrupt status set, even if the underlying condition is true.
     */
    @Test
    public void awaitThrowsIfCallingThreadHasInterruptStatusSet() throws InterruptedException
    {
        // Given (the underlying condition is true)
        fSetConditionAction.run();

        // When (the executing thread has its interrupt status set when await() is called)
        BlockingTestAction.BlockingCall aCall = () -> interruptAndCall(fAwaitable::await);
        BlockingTestAction aAction = new BlockingTestAction(aCall);
        assertTrue(aAction.startAndAwaitRun());

        // Then (await() should have thrown an InterruptedException and the calling thread should
        // have terminated)
        assertTrue(aAction.timedJoin(1, TimeUnit.SECONDS));
        assertTrue(aAction.wasInterrupted());
    }


    /**
     * The {@code await(long, TimeUnit)} method should return {@code true} when the
     * {@code Awaitable}'s underlying condition becomes true.
     */
    @Test
    public void timedAwaitReturnsTrueWhenConditionBecomesTrue() throws InterruptedException
    {
        // Given (another thread is blocked in a call to await(long, TimeUnit))
        TimedAwaitAction aAction = new TimedAwaitAction(fAwaitable, 1, TimeUnit.HOURS);
        assertTrue(aAction.startAndAwaitRun());

        // When (the condition that await() is waiting for becomes true)
        fSetConditionAction.run();

        // Then (the await() call should have returned true and the thread have terminated without
        // having been interrupted)
        assertTrue(aAction.timedJoin(2, TimeUnit.SECONDS));
        assertTrue(aAction.returnedTrue());
        assertFalse(aAction.wasInterrupted());
    }


    /**
     * The {@code await(long, TimeUnit)} method should return {@code false} if the
     * {@code Awaitable}'s underlying condition hasn't become true when the waiting time elapses.
     */
    @Test
    public void timedAwaitReturnsFalseIfWaitingTimeElapses() throws InterruptedException
    {
        // Given (another thread is blocked in a call to await(long, TimeUnit))
        TimedAwaitAction aAction = new TimedAwaitAction(fAwaitable, 10, TimeUnit.MILLISECONDS);
        assertTrue(aAction.startAndAwaitRun());

        // When (the waiting time elapses)
        assertTrue(aAction.timedJoin(1, TimeUnit.SECONDS));

        // Then (the await() call should have returned false and the thread have terminated without
        // having been interrupted)
        assertFalse(aAction.returnedTrue());
        assertFalse(aAction.wasInterrupted());
    }


    /**
     * The {@code await(long, TimeUnit)} method should return true immediately if the
     * {@code Awaitable}'s underlying condition already is true when the method is called.
     */
    @Test
    public void timedAwaitReturnsTrueIfConditionAlreadyIsTrue() throws InterruptedException
    {
        // Given (the underlying condition is true)
        fSetConditionAction.run();

        // When (another thread calls await())
        TimedAwaitAction aAction = new TimedAwaitAction(fAwaitable, 1, TimeUnit.DAYS);
        assertTrue(aAction.startAndAwaitRun());

        // Then (the await() call should have returned true immediately and the thread have
        // terminated without having been interrupted)
        assertTrue(aAction.timedJoin(2, TimeUnit.SECONDS));
        assertTrue(aAction.returnedTrue());
        assertFalse(aAction.wasInterrupted());
    }


    /**
     * The {@code await(long, TimeUnit)} method should return {@code false} immediately if the
     * timeout value is negative and the {@code Awaitable}'s underlying condition isn't true.
     */
    @Test
    public void timedAwaitReturnsImmediatelyForNegativeTimeout() throws InterruptedException
    {
        // Given
        TimedAwaitAction aAction = new TimedAwaitAction(fAwaitable, -1, TimeUnit.MILLISECONDS);

        // When (another thread makes a call to await(long, TimeUnit) with a negative timeout)
        assertTrue(aAction.startAndAwaitRun());

        // Then (the await() call should have returned false and the thread have terminated without
        // having been interrupted)
        assertTrue(aAction.timedJoin(10, TimeUnit.MILLISECONDS));
        assertFalse(aAction.returnedTrue());
        assertFalse(aAction.wasInterrupted());
    }


    /**
     * The {@code await(long, TimeUnit)} method should throw an {@code InterruptedException} if it
     * is interrupted before the {@code Awaitable}'s underlying condition becomes true or the
     * waiting time has elapsed.
     */
    @Test
    public void timedAwaitThrowsIfInterrupted() throws InterruptedException
    {
        // Given (another thread calls await() and is blocked in that call)
        TimedAwaitAction aAction = new TimedAwaitAction(fAwaitable, 1, TimeUnit.DAYS);
        assertTrue(aAction.startAndAwaitRun());

        // When (the thread calling await() is interrupted)
        aAction.interruptRun();

        // Then (await() should have thrown an InterruptedException and the calling thread should
        // have terminated)
        assertTrue(aAction.timedJoin(10, TimeUnit.SECONDS));
        assertTrue(aAction.wasInterrupted());
    }


    /**
     * The {@code await(long, TimeUnit)} method should throw an {@code InterruptedException} if it
     * is called from a thread that has its interrupt status set even if the condition is true.
     */
    @Test
    public void timedAwaitThrowsIfCallingThreadHasInterruptStatusSet() throws InterruptedException
    {
        // Given (the underlying condition is true)
        fSetConditionAction.run();

        // When (the executing thread has its interrupt status set when await() is called)
        BlockingTestAction.BlockingCall aAwaitCall = () -> fAwaitable.await(1, TimeUnit.DAYS);
        BlockingTestAction aAction = new BlockingTestAction(() -> interruptAndCall(aAwaitCall));
        assertTrue(aAction.startAndAwaitRun());

        // Then (await() should have thrown an InterruptedException and the calling thread should
        // have terminated)
        assertTrue(aAction.timedJoin(1, TimeUnit.SECONDS));
        assertTrue(aAction.wasInterrupted());
    }


    /**
     * Make a {@code BlockingCall} when the current thread's interrupt status is set. This method is
     * intended to be used as a {@code BlockingCall} itself in a {@code BlockingTestAction}.
     *
     * @param pCall The blocking call.
     *
     * @throws InterruptedException if the blocking call detects the interrupt status.
     */
    static void interruptAndCall(BlockingTestAction.BlockingCall pCall) throws InterruptedException
    {
        Thread.currentThread().interrupt();
        pCall.call();
    }


    /**
     * A {@code BlockingTestAction} that calls {@code Awaitable.await(long, TimeUnit)} and stores
     * the result.
     */
    static class TimedAwaitAction extends BlockingTestAction
    {
        private final Awaitable fAwaitable;
        private final long fTimeout;
        private final TimeUnit fTimeUnit;
        private volatile boolean fResult;

        TimedAwaitAction(Awaitable pAwaitable, long pTimeout, TimeUnit pTimeUnit)
        {
            fAwaitable = pAwaitable;
            fTimeout = pTimeout;
            fTimeUnit = pTimeUnit;
        }

        @Override
        protected void performBlockingCall() throws InterruptedException
        {
            fResult = fAwaitable.await(fTimeout, fTimeUnit);
        }

        boolean returnedTrue()
        {
            return fResult;
        }
    }
}
