/*
 * Copyright 2011, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;


/**
 * A {@code Future} that is completed by explicitly setting its result. Instances of this class will
 * not perform any computation to calculate the result; it must be set explicitly by calling
 * {@link #setResult(Object)}.
 *<p>
 * Instances of this class are safe for use by multiple threads.
 *
 * @param <T>   The type of the result this future will hold.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@ThreadSafe
public class FutureResult<T> implements PollableFuture<T>
{
    private final CountDownLatch fLatch = new CountDownLatch(1);
    private final AtomicReference<Completion<T>> fCompletionRef = new AtomicReference<>();


    /**
     * Attempt to cancel this {@code Future}. Calling this method will only set the completion state
     * to cancelled, meaning that calls to {@link #get()} will throw a
     * {@code CancellationException}. No cancellation of the result computation will be done, as
     * there is no computation.
     *
     * @param pMayInterruptIfRunning    ignored.
     *
     * @return  True if this {@code Future} was cancelled, false if it already had been completed.
     */
    @Override
    public boolean cancel(boolean pMayInterruptIfRunning)
    {
        return cancel();
    }


    /**
     * Attempt to cancel this {@code Future}. Calling this method will only set the completion state
     * to cancelled, meaning that calls to {@link #get()} will throw a
     * {@code CancellationException}. No cancellation of the result computation will be done, as
     * there is no computation.
     *
     * @return  True if this {@code Future} was cancelled, false if it already had been completed.
     */
    public boolean cancel()
    {
        return setCompletion(new Completion<>());
    }


    /**
     * Return whether this {@code Future} has been cancelled or not.
     *
     * @return  True if this {@code Future} has been cancelled, false if not.
     */
    @Override
    public boolean isCancelled()
    {
        Completion<T> aResult = fCompletionRef.get();
        return aResult != null && aResult.wasCancelled();
    }


    /**
     * Return whether this {@code Future} has completed or not. Completion may be due to normal
     * termination, an exception, or cancellation; in all of these cases, this method will return
     * true.
     *
     * @return  True if this {@code Future} has been completed, false if not.
     */
    @Override
    public boolean isDone()
    {
        return fCompletionRef.get() != null;
    }


    /**
     * Return whether the external calculation of the result has thrown an exception or not.
     *
     * @return  True if this {@code Future} has been completed with an exception, false if it
     *          completed normally or hasn't been completed.
     */
    public boolean completedExceptionally()
    {
        Completion<T> aResult = fCompletionRef.get();
        return aResult != null && aResult.completedExceptionally();
    }


    /**
     * Wait if necessary for the result to become available, and then return the result.
     *
     * @return  The result, possibly null.
     *
     * @throws InterruptedException     if the current thread was interrupted while waiting for the
     *                                  result.
     * @throws ExecutionException       if the external calculation of the result threw an
     *                                  exception.
     * @throws CancellationException    if this {@code Future} was cancelled before the result was
     *                                  set.
     */
    @Override
    @Nullable
    public T get() throws InterruptedException, ExecutionException
    {
        fLatch.await();
        return getResult();
    }


    /**
     * Wait if necessary for at most the given time for the result to become available, and then
     * return the result.
     *
     * @param pTimeout  The maximum time to wait. If the time is less than or equal to zero, the
     *                  method will not wait at all.
     * @param pUnit     The time unit of {@code pTimeout}.
     *
     * @return  The result, possibly null.
     *
     * @throws InterruptedException     if the current thread was interrupted while waiting for the
     *                                  result.
     * @throws ExecutionException       if the external calculation of the result threw an
     *                                  exception.
     * @throws CancellationException    if this {@code Future} was cancelled before the result was
     *                                  set.
     * @throws TimeoutException         if the wait timed out.
     * @throws NullPointerException     if {@code pUnit} is null.
     */
    @Override
    @Nullable
    public T get(long pTimeout, @Nonnull TimeUnit pUnit)
            throws InterruptedException, ExecutionException, TimeoutException
    {
        if (fLatch.await(pTimeout, pUnit))
            return getResult();
        else
            throw new TimeoutException();
    }


    /**
     * Return the result value if completed, otherwise return the specified absence value. If the
     * completion of this instance has encountered any exception, that exception is thrown.
     *
     * @param pNotCompletedValue    The value to return if this instance is not completed.
     *
     * @return  The result value, if completed, otherwise {@code pNotCompletedValue}.
     *
     * @throws CancellationException if this instance has been cancelled.
     * @throws CompletionException if this instance completed exceptionally.
     */
    @Override
    public T getNow(T pNotCompletedValue)
    {
        Completion<T> aCompletion = fCompletionRef.get();
        return aCompletion != null ? aCompletion.getCompletedResult() : pNotCompletedValue;
    }


    /**
     * Set the result of this {@code Future} unless it already has been completed.
     *
     * @param pResult   The result, possibly null.
     *
     * @return  True if the result was set, false if this instance already has been completed.
     */
    public boolean setResult(@Nullable T pResult)
    {
        return setCompletion(new Completion<>(pResult));
    }


    /**
     * Cause this {@code Future} to report an {@code ExecutionException} with the given throwable as
     * its cause, unless this {@code Future} already has been completed.
     *
     * @param pCause    The underlying cause of the {@code ExecutionException}.
     *
     * @return  True if the exception was set, false if this instance already has been completed.
     */
    public boolean setException(@Nonnull Throwable pCause)
    {
        return setCompletion(new Completion<>(pCause));
    }


    /**
     * Set the completion of this {@code Future} unless it has already been set.
     *
     * @param pCompletion   The completion.
     *
     * @return  True if the completion was set, false if this instance already has been completed.
     */
    private boolean setCompletion(@Nonnull Completion<T> pCompletion)
    {
        if (fCompletionRef.compareAndSet(null, pCompletion))
        {
            // Release threads blocked in a call to get().
            fLatch.countDown();
            return true;
        }
        else
            return false;
    }


    /**
     * Get the result of this {@code Future}.
     *
     * @return  The result, possibly null.
     *
     * @throws ExecutionException       if the result of this future was an exception rather than a
     *                                  message.
     * @throws CancellationException    if this {@code Future} was cancelled before the result was
     *                                  set.
     * @throws NullPointerException     if this {@code Future} has not been completed.
     */
    @Nullable
    private T getResult() throws ExecutionException
    {
        return fCompletionRef.get().getResult();
    }


    /**
     * Inner class holding the completion of a {@code FutureResult}.
     *
     * @param <T>   The type of the completed result.
     */
    static private class Completion<T>
    {
        private final T fResult;
        private final Throwable fException;
        private final boolean fWasCancelled;

        /**
         * Create a new successful {@code Completion}.
         *
         * @param pResult   The result of the successful completion.
         */
        Completion(@Nullable T pResult)
        {
            fResult = pResult;
            fException = null;
            fWasCancelled = false;
        }

        /**
         * Create a new {@code Completion} for a result that threw an exception.
         *
         * @param pException    The thrown exception.
         */
        Completion(@Nullable Throwable pException)
        {
            fException = pException;
            fResult = null;
            fWasCancelled = false;
        }

        /**
         * Create a new {@code Completion} that was cancelled.
         */
        Completion()
        {
            fResult = null;
            fException = null;
            fWasCancelled = true;
        }

        /**
         * Get the result with which the {@code Future} was completed.
         *
         * @return  The result, possibly null.
         *
         * @throws ExecutionException       if the {@code Future} was completed by a thrown
         *                                  exception.
         * @throws CancellationException    if the {@code Future} was completed by a cancellation.
         */
        @Nullable
        T getResult() throws ExecutionException
        {
            if (fException != null)
                throw new ExecutionException(fException);
            else if (fWasCancelled)
                throw new CancellationException();
            else
                return fResult;
        }

        /**
         * Get the result with which the {@code Future} was completed.
         *
         * @return  The result, possibly null.
         *
         * @throws CompletionException      if the {@code Future} was completed by a thrown
         *                                  exception.
         * @throws CancellationException    if the {@code Future} was completed by a cancellation.
         */
        @Nullable
        T getCompletedResult()
        {
            if (fException != null)
                throw new CompletionException(fException);
            else if (fWasCancelled)
                throw new CancellationException();
            else
                return fResult;
        }

        /**
         * Return whether the {@code Future} was completed due to a thrown exception or not.
         *
         * @return  True if the {@code Future} was completed with a thrown exception, false if not.
         */
        boolean completedExceptionally()
        {
            return fException != null;
        }

        /**
         * Return whether the {@code Future} was completed due to a cancellation or not.
         *
         * @return  True if the {@code Future} was completed with a cancellation, false if not.
         */
        boolean wasCancelled()
        {
            return fWasCancelled;
        }
    }
}
