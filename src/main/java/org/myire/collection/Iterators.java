/*
 * Copyright 2009, 2012, 2017, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.myire.annotation.Unreachable;
import static org.myire.util.Numbers.requireNonNegative;


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
     * Create an {@code Iterator} that contains a single element. The {@code Iterator} doesn't
     * support the {@code remove} operation.
     *
     * @param pElement  The iterator's single element.
     * @param <T>       The type of the element returned by the {@code Iterator}.
     *
     * @return  A new {@code Iterator} for the specified element.
     */
    @Nonnull
    static public <T> Iterator<T> singletonIterator(@Nullable T pElement)
    {
        return new SingletonIterator<>(pElement);
    }


    /**
     * Create an {@code Iterator} that returns the elements of an array. The {@code Iterator}
     * doesn't support the {@code remove} operation.
     *
     * @param pElements The array to iterate over. The iterator will reflect changes made to the
     *                  elements in the array. Replacing an element in the array will also cause the
     *                  iterator to return the new element (unless the replaced element already has
     *                  been returned by the iteration).
     * @param <T>       The type of the element returned by the {@code Iterator}.
     *
     * @return  A new {@code Iterator} for the specified array.
     */
    @Nonnull
    static public <T> Iterator<T> arrayIterator(@Nonnull T[] pElements)
    {
        return new ArrayIterator<>(pElements, 0, pElements.length);
    }


    /**
     * Create an {@code Iterator} that returns the elements of an array. The {@code Iterator}
     * doesn't support the {@code remove} operation.
     *
     * @param pElements The array to iterate over. The iterator will reflect changes made to the
     *                  elements in the array. Replacing an element in the array will also cause the
     *                  iterator to return the new element (unless the replaced element already has
     *                  been returned by the iteration).
     * @param pOffset   The offset of the first element to return from the iteration.
     * @param pLength   The number of elements to return from the iteration.
     * @param <T>       The type of the element returned by the {@code Iterator}.
     *
     * @return  A new {@code Iterator} for the specified range of the array.
     *
     * @throws IllegalArgumentException if {@code pOffset} is negative, or if {@code pLength} is
     *                                  negative, or if {@code pOffset} is greater than
     *                                  {@code pElements.length - pLength}.
     */
    @Nonnull
    static public <T> Iterator<T> arrayIterator(
        @Nonnull T[] pElements,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        return new ArrayIterator<>(pElements, pOffset, pLength);
    }


    /**
     * Create an {@code Iterator} that returns the elements of a {@code Sequence}. The
     * {@code Iterator} doesn't support the {@code remove} operation.
     *
     * @param pSequence The sequence to iterate over. The iterator will reflect changes made to the
     *                  elements in the sequence. Replacing an element in the sequence will also
     *                  cause the iterator to return the new element (unless the replaced element
     *                  already has been returned by the iteration).
     * @param <T>       The type of the element returned by the {@code Iterator}.
     *
     * @return  A new {@code Iterator} for the specified sequence.
     */
    @Nonnull
    static public <T> Iterator<T> sequenceIterator(@Nonnull Sequence<T> pSequence)
    {
        return new SequenceIterator<>(pSequence);
    }


    /**
     * Implementation of {@code Iterator} that does <b>not</b> support the {@code remove} operation.
     * The {@code hasNext}, {@code next}, and {@code forEachRemaining} operations are delegated to
     * another {@code Iterator} instance, whereas the {@code remove} operation always throws an
     * {@code UnsupportedOperationException} (by falling back to the default implementation in
     * {@code java.util.Iterator}).
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
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public E next()
        {
            return fDelegate.next();
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


    /**
     * Implementation of {@code Iterator} for a single element. This implementation does <b>not</b>
     * support the {@code remove} operation; that method always throws an
     * {@code UnsupportedOperationException} (by falling back to the default implementation in
     * {@code java.util.Iterator}).
     *
     * @param <E>   The type of the single element.
     */
    static private class SingletonIterator<E> implements Iterator<E>
    {
        private final E fElement;
        private boolean fHasNext = true;

        /**
         * Create a new {@code SingletonIterator}.
         *
         * @param pElement  The single element.
         */
        SingletonIterator(@Nullable E pElement)
        {
            fElement = pElement;
        }

        /**
         * Check if the iteration has more elements.
         *
         * @return  True if the element hasn't been returned by {@code next} yet, false otherwise.
         */
        @Override
        public boolean hasNext()
        {
            return fHasNext;
        }

        /**
         * Get the element if it hasn't been returned yet.
         *
         * @return  The single element.
         *
         * @throws NoSuchElementException if this method already has been called.
         */
        @Override
        public E next()
        {
            if (!fHasNext)
                throw new NoSuchElementException();

            fHasNext = false;
            return fElement;
        }

        /**
         * Perform the specified action for the element if it hasn't been returned by {@code next}.
         *
         * @param pAction   The action to perform for the element.
         *
         * @throws NullPointerException if the {@code pAction} is null.
         */
        @Override
        public void forEachRemaining(Consumer<? super E> pAction)
        {
            if (fHasNext)
            {
                fHasNext = false;
                pAction.accept(fElement);
            }
        }
    }


    /**
     * An {@code Iterator} backed by an array. This implementation does <b>not</b> support the
     * {@code remove} operation; that method always throws an
     * {@code UnsupportedOperationException} (by falling back to the default implementation in
     * {@code java.util.Iterator}).
     *
     * @param <E>   The type of elements in the array.
     */
    static private class ArrayIterator<E> implements Iterator<E>
    {
        private final E[] fElements;
        private final int fLength;
        private int fNextElement;

        /**
         * Create a new {@code ArrayIterator}.
         *
         * @param pElements The array to iterate over. The iterator will reflect changes made to the
         *                  elements in the array. Replacing an element in the array will also cause
         *                  the iterator to return the new element (unless the replaced element
         *                  already has been returned by the iteration).
         * @param pOffset   The offset of the first element to return from the iteration.
         * @param pLength   The number of elements to return from the iteration.
         *
         * @throws NullPointerException if {@code pElements} is null.
         * @throws IllegalArgumentException if {@code pOffset} is negative, or if {@code pLength} is
         *                                  negative, or if {@code pOffset} is greater than
         *                                  {@code pElements.length - pLength}.
         */
        ArrayIterator(@Nonnull E[] pElements, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fElements = requireNonNull(pElements);
            fNextElement = requireNonNegative(pOffset);
            fLength = pOffset + requireNonNegative(pLength);
            if (pOffset > pElements.length - pLength)
                throw new IllegalArgumentException(pOffset + ">" + pElements.length + "-" + pLength);
        }

        @Override
        public boolean hasNext()
        {
            return fNextElement < fLength;
        }

        @Override
        public E next()
        {
            if (fNextElement < fLength)
                return fElements[fNextElement++];
            else
                throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super E> pAction)
        {
            while (fNextElement < fLength)
                pAction.accept(fElements[fNextElement++]);
        }
    }


    /**
     * An {@code Iterator} backed by a {@code Sequence}. This implementation does <b>not</b> support
     * the {@code remove} operation; that method always throws an
     * {@code UnsupportedOperationException} (by falling back to the default implementation in
     * {@code java.util.Iterator}).
     *
     * @param <E>   The type of elements in the array.
     */
    static private class SequenceIterator<E> implements Iterator<E>
    {
        private final Sequence<E> fSequence;
        private int fNextElement;

        /**
         * Create a new {@code SequenceIterator}.
         *
         * @param pSequence The sequence to iterate over.
         *
         * @throws NullPointerException if {@code pSequence} is null.
         */
        SequenceIterator(@Nonnull Sequence<E> pSequence)
        {
            fSequence = requireNonNull(pSequence);
        }

        @Override
        public boolean hasNext()
        {
            return fNextElement < fSequence.size();
        }

        @Override
        public E next()
        {
            if (fNextElement < fSequence.size())
                return fSequence.elementAt(fNextElement++);
            else
                throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super E> pAction)
        {
            while (fNextElement < fSequence.size())
                pAction.accept(fSequence.elementAt(fNextElement++));
        }
    }
}
