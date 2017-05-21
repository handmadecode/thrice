/*
 * Copyright 2009, 2012, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.function.Consumer;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

import org.myire.annotation.Unreachable;


/**
 * Factory and utility methods for iterators.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Iterators
{
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Iterators()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Create an {@code Iterator} that provides unmodifiable iteration, that is, doesn't support the
     * {@code remove} operation. The returned {@code Iterator} will delegate the {@code hasNext},
     * {@code next}, and {@code forEachRemaining} operations to the specified {@code Iterator}
     * instance, whereas the {@code remove} operation always throws an
     * {@code UnsupportedOperationException}.
     *
     * @param pIterator An iterator to create an unmodifiable view of.
     * @param <T>       The type of elements returned by the {@code Iterator}.
     *
     * @return  A new {@code Iterator} that wraps {@code pIterator}.
     *
     * @throws NullPointerException if {@code pIterator} is null.
     */
    @Nonnull
    static public <T> Iterator<T> unmodifiableIterator(@Nonnull Iterator<? extends T> pIterator)
    {
        return new UnmodifiableIterator<>(pIterator);
    }


    /**
     * Implementation of {@code Iterator} that does <b>not</b> support the {@code remove} operation.
     * The {@code hasNext}, {@code next}, and {@code forEachRemaining} operations are delegated to
     * another {@code Iterator} instance, whereas the {@code remove} operation always throws an
     * {@code UnsupportedOperationException}.
     *
     * @param <E>   The type of elements this iterator operates on.
     */
    static private class UnmodifiableIterator<E> implements Iterator<E>
    {
        private final Iterator<? extends E> fDelegate;

        /**
         * Create a new {@code UnmodifiableIterator}.
         *
         * @param pIterator The iterator to delegate the {@code hasNext()} {@code next()}, and
         *                  {@code forEachRemaining()} calls to.
         *
         * @throws NullPointerException if {@code pIterator} is null.
         */
        UnmodifiableIterator(@Nonnull Iterator<? extends E> pIterator)
        {
            fDelegate = requireNonNull(pIterator);
        }

        /**
         * Check if the iteration has more elements. This method returns true if a call to
         * {@link #next()} would return an element rather than throw an exception.
         *
         * @return  True if the iterator has more elements, false if not.
         */
        @Override
        public boolean hasNext()
        {
            return fDelegate.hasNext();
        }

        /**
         * Get the next element in the iteration. Calling this method repeatedly until the
         * {@link #hasNext()} method returns false will return each element in the underlying
         * iterable exactly once.
         *
         * @return  The next element in the iteration.
         *
         * @throws java.util.NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public E next()
        {
            return fDelegate.next();
        }

        /**
         * The remove operation is not supported.
         *
         * @throws UnsupportedOperationException    always.
         */
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Perform the specified action for each remaining element until all elements have been
         * processed or the action throws an exception. Actions are performed in the order of
         * iteration, if that order is specified. Exceptions thrown by the action are relayed to the
         * caller.
         *
         * @param pAction   The action to perform for each element.
         *
         * @throws NullPointerException if the {@code pAction} is null.
         */
        @Override
        public void forEachRemaining(@Nonnull Consumer<? super E> pAction)
        {
            fDelegate.forEachRemaining(pAction);
        }
    }
}
