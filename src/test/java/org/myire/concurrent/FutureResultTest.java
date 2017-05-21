/*
 * Copyright 2011, 2013, 2016-2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


/**
 * Unit tests for {@link org.myire.concurrent.FutureResult}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class FutureResultTest
{
    /**
     * Calling {@code setResult} should succeed when the {@code FutureResult} hasn't been completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setResultSucceedsWhenNotCompleted() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // Then
        assertTrue(aResult.setResult("someResult"));
    }


    /**
     * Calling {@code setResult} should fail when the {@code FutureResult} already has completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setResultFailsWhenCompleted() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.setResult("someResult");

        // Then
        assertFalse(aResult.setResult("anotherResult"));
    }


    /**
     * Calling {@code setResult} should fail when the {@code FutureResult} already has completed
     * with an exception.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setResultFailsWhenCompletedExceptionally() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.setException(new Exception());

        // Then
        assertFalse(aResult.setResult("someResult"));
    }


    /**
     * Calling {@code setResult} should fail when the {@code FutureResult} already has been
     * canceled.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setResultFailsWhenCanceled() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.cancel();

        // Then
        assertFalse(aResult.setResult("someResult"));
    }


    /**
     * Calling {@code setException} should succeed when the {@code FutureResult} isn't completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setExceptionSucceedsWhenNotCompleted() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // Then
        assertTrue(aResult.setException(new Exception()));
    }


    /**
     * Calling {@code setException} should fail when the {@code FutureResult} has completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setExceptionFailsWhenCompleted() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.setResult("blah");

        // Then
        assertFalse(aResult.setException(new Exception()));
    }


    /**
     * Calling {@code setException} should fail when the {@code FutureResult} already has completed
     * with an exception.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setExceptionFailsWhenCompletedExceptionally() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.setException(new Exception());

        // Then
        assertFalse(aResult.setException(new Exception()));
    }


    /**
     * Calling {@code setException} should fail when the {@code FutureResult} has been canceled.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void setExceptionFailsWhenCanceled() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.cancel();

        // Then
        assertFalse(aResult.setException(new Exception()));
    }


    /**
     * Calling {@code cancel} should succeed when the {@code FutureResult} isn't completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void cancelSucceedsWhenNotCompleted() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // Then
        assertTrue(aResult.cancel());
    }


    /**
     * Calling {@code cancel} should fail when the {@code FutureResult} has completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void cancelFailsWhenCompleted() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.setResult("done");

        // Then
        assertFalse(aResult.cancel());
    }


    /**
     * Calling {@code cancel} should fail when the {@code FutureResult} already has completed with
     * an exception.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void cancelFailsWhenCompletedExceptionally() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.setException(new Exception());

        // Then
        assertFalse(aResult.cancel());
    }


    /**
     * Calling {@code cancel} should fail when the {@code FutureResult} already has been canceled.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void cancelFailsWhenCanceled() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        aResult.cancel();

        // Then
        assertFalse(aResult.cancel());
    }


    /**
     * Calling {@code cancel(true)} should cancel the task assuming it has not completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void cancelWithMayInterruptTrueCancels() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        boolean aWasCancelled = aResult.cancel(true);

        // Then
        assertTrue(aWasCancelled);
        assertTrue(aResult.isCancelled());
    }


    /**
     * Calling {@code cancel(false)} should cancel the task assuming it has not completed.
     *
     * @throws InterruptedException if the test is interrupted.
     * @throws ExecutionException   if the test fails unexpectedly.
     */
    @Test
    public void cancelWithMayInterruptFalseCancels() throws InterruptedException, ExecutionException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();

        // When
        boolean aWasCancelled = aResult.cancel(false);

        // Then
        assertTrue(aWasCancelled);
        assertTrue(aResult.isCancelled());
    }


    /**
     * The {@code get} method should return the value specified in the call to {@code setResult}.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getReturnsValueFromSetResult() throws InterruptedException
    {
        // Given (a thread calls get() and is blocked because the Future isn't completed)
        String aValue = "badger";
        FutureResult<String> aResult = new FutureResult<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aResult);
        aGetAction.startAndAwaitRun();

        // When
        aResult.setResult(aValue);

        // Then (the call to get() should have returned the result)
        assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS));
        assertEquals(aValue, aGetAction.getResult());
    }


    /**
     * The {@code get} method should block when the {@code FutureResult} isn't completed.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getBlocksWhenNotCompleted() throws InterruptedException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aResult);

        try
        {
            // When (the get action should be blocked and its thread shouldn't terminate)
            aGetAction.startAndAwaitRun();

            // Then
            assertFalse(aGetAction.timedJoin(100, TimeUnit.MILLISECONDS));
        }
        finally
        {
            // Release the thread calling get().
            aResult.setResult(null);
        }
    }


    /**
     * The {@code get} method should throw an {@code ExecutionException} when the
     * {@code FutureResult} completed with an exception.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getThrowsWhenCompletedExceptionally() throws InterruptedException
    {
        // Given
        Exception aException = new NullPointerException("Whoops");
        FutureResult<String> aResult = new FutureResult<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aResult);
        aGetAction.startAndAwaitRun();

        // When
        aResult.setException(aException);

        // Then (the call to get() should have thrown an ExecutionException containing the
        // Exception passed to setException())
        assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS));
        assertSame(aException, aGetAction.getExecutionException().getCause());
    }


    /**
     * The {@code get} method should throw a {@code CancellationException} when the
     * {@code FutureResult} has been canceled.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getThrowsWhenCanceled() throws InterruptedException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aResult);
        aGetAction.startAndAwaitRun();

        // When
        aResult.cancel();

        // Then (the call to get() should have thrown a CancellationException)
        assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS));
        assertNotNull(aGetAction.getCancellationException());
    }


    /**
     * The {@code get(long, TimeUnit)} method should return the value specified in the call to
     * {@code setResult}.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetReturnsValueFromSetResult() throws InterruptedException, ExecutionException, TimeoutException
    {
        // Given
        String aValue = "ghastly";
        FutureResult<String> aResult = new FutureResult<>();
        TimedFutureGetAction<String> aGetAction = new TimedFutureGetAction<>(aResult, 1, TimeUnit.DAYS);
        aGetAction.startAndAwaitRun();

        // When
        aResult.setResult(aValue);

        // Then (the call to get() should have returned the result)
        assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS));
        assertEquals(aValue, aGetAction.getResult());
    }


    /**
     * The {@code get(long, TimeUnit} method should throw a {@code TimeoutException} if the
     * {@code FutureResult} hasn't completed when the waiting time elapses.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetThrowsWhenNotCompleted() throws InterruptedException, ExecutionException, TimeoutException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();
        TimedFutureGetAction<String> aGetAction = new TimedFutureGetAction<>(aResult, 10, TimeUnit.MILLISECONDS);

        // When (the Future is not completed before the waiting time elapses)
        aGetAction.startAndAwaitRun();

        // Then
        assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS));
        assertNotNull(aGetAction.getTimeoutException());
    }


    /**
     * The {@code get(long, TimeUnit} method should throw an {@code ExecutionException} when the
     * {@code FutureResult} completed with an exception.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetThrowsWhenCompletedExceptionally() throws InterruptedException
    {
        // Given
        Exception aException = new IllegalArgumentException("Not that one!");
        FutureResult<String> aResult = new FutureResult<>();
        TimedFutureGetAction<String> aGetAction = new TimedFutureGetAction<>(aResult, 1, TimeUnit.DAYS);
        aGetAction.startAndAwaitRun();

        // When
        aResult.setException(aException);

        // Then (the call to get() should have thrown an ExecutionException containing the
        // Exception passed to setException())
        assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS));
        assertSame(aException, aGetAction.getExecutionException().getCause());
    }


    /**
     * The {@code get(long, TimeUnit} method should throw a {@code CancellationException} when the
     * {@code FutureResult} has been canceled.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetThrowsWhenCanceled() throws InterruptedException
    {
        // Given
        FutureResult<String> aResult = new FutureResult<>();
        TimedFutureGetAction<String> aGetAction = new TimedFutureGetAction<>(aResult, 1, TimeUnit.DAYS);
        aGetAction.startAndAwaitRun();

        // When
        aResult.cancel();

        // Then (the call to get() should have thrown a CancellationException)
        assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS));
        assertNotNull(aGetAction.getCancellationException());
    }


    /**
     * A new {@code FutureResult} should not be completed nor canceled.
     */
    @Test
    public void newInstanceIsNotCompleted()
    {
        // Given
        FutureResult<Object> aResult = new FutureResult<>();

        // Then
        assertFalse(aResult.isDone());
        assertFalse(aResult.completedExceptionally());
        assertFalse(aResult.isCancelled());
    }


    /**
     * A {@code FutureResult} that completed normally should be done but not canceled.
     */
    @Test
    public void completedInstanceHasCorrectState()
    {
        // Given
        FutureResult<Object> aResult = new FutureResult<>();

        // When
        aResult.setResult(new Object());

        // Then
        assertTrue(aResult.isDone());
        assertFalse(aResult.completedExceptionally());
        assertFalse(aResult.isCancelled());
    }


    /**
     * A {@code FutureResult} that completed exceptionally should be done but not canceled.
     */
    @Test
    public void exceptionallyCompletedInstanceHasCorrectState()
    {
        // Given
        FutureResult<Object> aResult = new FutureResult<>();

        // When
        aResult.setException(new Exception());

        // Then
        assertTrue(aResult.isDone());
        assertTrue(aResult.completedExceptionally());
        assertFalse(aResult.isCancelled());
    }


    /**
     * A {@code FutureResult} that was canceled should be done and canceled.
     */
    @Test
    public void canceledInstanceHasCorrectState()
    {
        // Given
        FutureResult<Object> aResult = new FutureResult<>();

        // When
        aResult.cancel();

        // Then
        assertTrue(aResult.isDone());
        assertFalse(aResult.completedExceptionally());
        assertTrue(aResult.isCancelled());
    }


    /**
     * Blocking test action that calls {@code Future.get()}.
     */
    static private class FutureGetAction<T> extends BlockingTestAction
    {
        private final Future<T> fFuture;
        private volatile T fResult;
        private volatile ExecutionException fExecutionException;
        private volatile CancellationException fCancellationException;

        FutureGetAction(Future<T> pFuture)
        {
            fFuture = pFuture;
        }

        @Override
        protected void performBlockingCall() throws InterruptedException
        {
            try
            {
                fResult = performGet(fFuture);
            }
            catch (ExecutionException e)
            {
                fExecutionException = e;
            }
            catch (CancellationException e)
            {
                fCancellationException = e;
            }
        }

        protected T performGet(Future<T> pFuture) throws ExecutionException, InterruptedException
        {
            return pFuture.get();
        }

        T getResult()
        {
            return fResult;
        }

        ExecutionException getExecutionException()
        {
            return fExecutionException;
        }

        CancellationException getCancellationException()
        {
            return fCancellationException;
        }
    }


    /**
     * Blocking test action that calls {@code Future.get(long, TimeUnit)}.
     */
    static private class TimedFutureGetAction<T> extends FutureGetAction<T>
    {
        private final long fTimeout;
        private final TimeUnit fUnit;
        private volatile TimeoutException fTimeoutException;

        TimedFutureGetAction(Future<T> pFuture, long pTimeout, TimeUnit pUnit)
        {
            super(pFuture);
            fTimeout = pTimeout;
            fUnit = pUnit;
        }

        @Override
        protected T performGet(Future<T> pFuture) throws ExecutionException, InterruptedException
        {
            try
            {
                return pFuture.get(fTimeout, fUnit);
            }
            catch (TimeoutException e)
            {
                fTimeoutException = e;
                return null;
            }
        }

        TimeoutException getTimeoutException()
        {
            return fTimeoutException;
        }
    }
}
