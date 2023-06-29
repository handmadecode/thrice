/*
 * Copyright 2021, 2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;


/**
 * Extension of {@code Future} that adds the {@link #getNow(Object)} method introduced by
 * {@link CompletableFuture}. This interface can be used to expose the {@code getNow()} method
 * without exposing the methods that completes the {@code CompletableFuture}.
 *
 * @param <T>   The result type returned by this future.
 */
public interface PollableFuture<T> extends Future<T>
{
    /**
     * Return the result value if completed, otherwise return the specified value. If the completion
     * of this instance has encountered any exception, that exception is thrown.
     *
     * @param pNotCompletedValue    The value to return if this instance is not completed.
     *
     * @return  The result value, if completed, otherwise {@code pNotCompletedValue}.
     *
     * @throws CancellationException if this instance has been cancelled.
     * @throws CompletionException if this instance completed exceptionally.
     *
     * @see java.util.concurrent.CompletableFuture#getNow(Object)
     */
    T getNow(T pNotCompletedValue);


    /**
     * Create a {@code PollableFuture} that delegates all calls to another {@code Future}.
     *
     * @param pDelegate The {@code Future} to delegate all calls to.
     *
     * @param <T> The result type returned by the created {@code PollableFuture}.
     *
     * @return  A new {@code PollableFuture}, never null.
     *
     * @throws NullPointerException if {@code pDelegate} is null.
     */
    static <T> PollableFuture<T> of(Future<T> pDelegate)
    {
        return new DelegatingPollableFuture<>(pDelegate, PollableFuture::pollNow);
    }


    /**
     * Create a {@code PollableFuture} that delegates all calls to a {@code CompletableFuture}.
     *
     * @param pDelegate The {@code CompletableFuture} to delegate all calls to.
     *
     * @param <T> The result type returned by the created {@code PollableFuture}.
     *
     * @return  A new {@code PollableFuture}, never null.
     *
     * @throws NullPointerException if {@code pDelegate} is null.
     */
    static <T> PollableFuture<T> of(CompletableFuture<T> pDelegate)
    {
        return new DelegatingPollableFuture<>(pDelegate, CompletableFuture::getNow);
    }


    /**
     * Poll a {@code Future} and return the value of its {@code get()} method if it is completed,
     * otherwise returned the specified not completed value.
     *
     * @param pFuture               The {@code Future} to poll.
     * @param pNotCompletedValue    The value to return if {@code pFuture} isn't completed.
     *
     * @return  The value returned by {@code pFuture.get()}, or {@code pNotCompletedValue} if
     *          {@code pFuture} isn't completed.
     *
     * @param <T>   The type of value returned by {@code pFuture}.
     *
     * @throws CancellationException if {@code pFuture} has been cancelled.
     * @throws CompletionException if {@code pFuture} completed exceptionally.
     * @throws NullPointerException if {@code pFuture} is null.
     */
    static <T> T pollNow(Future<T> pFuture, T pNotCompletedValue)
    {
        if (!pFuture.isDone())
            return pNotCompletedValue;

        try
        {
            return pFuture.get();
        }
        catch (ExecutionException e)
        {
            throw new CompletionException(e.getCause());
        }
        catch (InterruptedException e)
        {
            // Restore the interrupt status and return the not completed value.
            Thread.currentThread().interrupt();
            return pNotCompletedValue;
        }
    }


    /**
     * A {@code PollableFuture} that delegates all calls to another {@code Future}.
     *
     * @param <T>   The result type returned by this {@code PollableFuture}.
     * @param <D>   The delegate's type.
     */
    class DelegatingPollableFuture<T, D extends Future<T>> implements PollableFuture<T>
    {
        private final D fDelegate;
        private final BiFunction<D, T, T> fPollFunction;

        /**
         * Create a new {@code DelegatingPollableFuture}.
         *
         * @param pDelegate     The {@code Future} to delegate all calls to.
         * @param pPollFunction A function that polls a {@code D} instance, returning the second
         *                      argument if that {@code Future} is not done yet.
         *
         * @throws NullPointerException if any of the parameters is null.
         */
        DelegatingPollableFuture(@Nonnull D pDelegate, @Nonnull BiFunction<D, T, T> pPollFunction)
        {
            fDelegate = requireNonNull(pDelegate);
            fPollFunction = requireNonNull(pPollFunction);
        }

        @Override
        public boolean cancel(boolean pMayInterruptIfRunning)
        {
            return fDelegate.cancel(pMayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled()
        {
            return fDelegate.isCancelled();
        }

        @Override
        public boolean isDone()
        {
            return fDelegate.isDone();
        }

        @Override
        public T get() throws InterruptedException, ExecutionException
        {
            return fDelegate.get();
        }

        @Override
        public T get(long pTimeout, @Nonnull TimeUnit pUnit)
            throws InterruptedException, ExecutionException, TimeoutException
        {
            return fDelegate.get(pTimeout, pUnit);
        }

        @Override
        public T getNow(T pNotCompletedValue)
        {
            return fPollFunction.apply(fDelegate, pNotCompletedValue);
        }
    }
}
