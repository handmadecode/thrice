/*
 * Copyright 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.myire.annotation.Unreachable;


/**
 * Factory methods for {@code Awaitable} implementations.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Awaitables
{
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Awaitables()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Create an {@code Awaitable} that waits for a {@code CountDownLatch} to reach a count of zero.
     *
     * @param pLatch    The {@code CountDownLatch} that the {@code await} methods wait for.
     *
     * @return  A new {@code Awaitable}, never null.
     *
     * @throws NullPointerException if {@code pLatch} is null.
     */
    @Nonnull
    static public Awaitable wrap(@Nonnull CountDownLatch pLatch)
    {
        return new CountDownLatchAwaitable(pLatch);
    }


    /**
     * Create an {@code Awaitable} that waits for a thread to terminate.
     *
     * @param pThread    The {@code Thread} that the {@code await} methods wait for.
     *
     * @return  A new {@code Awaitable}, never null.
     *
     * @throws NullPointerException if {@code pThread} is null.
     */
    @Nonnull
    static public Awaitable wrap(@Nonnull Thread pThread)
    {
        return new ThreadAwaitable(pThread);
    }


    /**
     * Implementation of {@code Awaitable} that waits for {@code CountDownLatch}'s count to reach
     * zero.
     */
    @ThreadSafe
    static private class CountDownLatchAwaitable implements Awaitable
    {
        private final CountDownLatch fLatch;

        /**
         * Create a new {@code CountDownLatchAwaitable}.
         *
         * @param pLatch    The {@code CountDownLatch} that the {@code await} methods wait for.
         *
         * @throws NullPointerException if {@code pLatch} is null.
         */
        CountDownLatchAwaitable(@Nonnull CountDownLatch pLatch)
        {
            fLatch = requireNonNull(pLatch);
        }

        @Override
        public void await() throws InterruptedException
        {
            fLatch.await();
        }

        @Override
        public boolean await(long pTimeout, @Nonnull TimeUnit pUnit) throws InterruptedException
        {
            return fLatch.await(pTimeout, pUnit);
        }
    }


    /**
     * Implementation of {@code Awaitable} that waits for a thread to terminate.
     */
    @ThreadSafe
    static private class ThreadAwaitable implements Awaitable
    {
        private final Thread fThread;

        /**
         * Create a new {@code ThreadAwaitable}.
         *
         * @param pThread    The {@code Thread} that the {@code await} methods wait for.
         *
         * @throws NullPointerException if {@code pThread} is null.
         */
        ThreadAwaitable(@Nonnull Thread pThread)
        {
            fThread = requireNonNull(pThread);
        }

        @Override
        public void await() throws InterruptedException
        {
            checkInterrupted();
            fThread.join();
        }


        @Override
        public boolean await(long pTimeout, @Nonnull TimeUnit pUnit) throws InterruptedException
        {
            checkInterrupted();
            pUnit.timedJoin(fThread, pTimeout);
            return !fThread.isAlive();
        }


        /**
         * Check if the current thread's interrupted status is set and throw an
         * {@code InterruptedException} if that is the case. The interrupted status of the thread
         * will be cleared by this method.
         *
         * @throws InterruptedException if the current thread's interrupted status was set when this
         *                              method was called.
         */
        static private void checkInterrupted() throws InterruptedException
        {
            if (Thread.interrupted())
                throw new InterruptedException();
        }
    }
}
