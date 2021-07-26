/*
 * Copyright 2013, 2017, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.myire.annotation.Unreachable;
import static org.myire.util.Numbers.requireNonNegative;


/**
 * Factory methods for {@code Sequence} implementations.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Sequences
{
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Sequences()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Get the empty sequence. This is an immutable sequence of size 0.
     *
     * @param <T>   The sequence's element type.
     *
     * @return  The empty sequence.
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    static public <T> Sequence<T> emptySequence()
    {
        return (Sequence<T>) EmptySequence.INSTANCE;
    }


    /**
     * Create a sequence that holds a single element.
     *
     * @param pElement  The sequence's sole element.
     *
     * @param <T>   The sequence's element type.
     *
     * @return  A singleton sequence, never null.
     */
    @Nonnull
    static public <T> Sequence<T> singleton(@Nullable T pElement)
    {
        return new SingletonSequence<>(pElement);
    }


    /**
     * Create a {@code Sequence} that is a wrapper around the elements in a {@code List}. The
     * returned {@code Sequence} will reflect changes made to the specified list. Added elements
     * will be accessible through the sequence, removed elements will disappear from the sequence.
     *
     * @param pList The list to wrap.
     *
     * @param <T>   The sequence's element type.
     *
     * @return  A new {@code Sequence}, never null.
     *
     * @throws NullPointerException if {@code pList} is null.
     */
    @Nonnull
    static public <T> Sequence<T> wrap(@Nonnull List<? extends T> pList)
    {
        return new ListSequence<>(pList);
    }


    /**
     * Create a {@code Sequence} that is a wrapper around the elements in an array. The returned
     * {@code Sequence} will reflect changes made to the elements in the array. Replacing an element
     * in the array will also replace the element in the sequence.
     *
     * @param pElements The array to wrap.
     *
     * @param <T>   The sequence's element type.
     *
     * @return  A new {@code Sequence}, never null.
     *
     * @throws NullPointerException if {@code pElements} is null.
     */
    @Nonnull
    static public <T> Sequence<T> wrap(@Nonnull T[] pElements)
    {
        return pElements.length == 0 ? emptySequence() : new ArraySequence<>(pElements, 0, pElements.length);
    }


    /**
     * Create a {@code Sequence} that is a wrapper around a range of the elements in an array. The
     * returned {@code Sequence} will reflect changes made to the elements in the array. Replacing
     * an element in the array will also replace the element in the sequence.
     *
     * @param pElements The array to wrap.
     * @param pOffset   The offset of the first element in the array that belongs to the sequence.
     * @param pLength   The number of elements in the sequence, starting at the specified offset.
     *
     * @param <T>   The sequence's element type.
     *
     * @return  A new {@code Sequence}, never null.
     *
     * @throws NullPointerException if {@code pElements} is null.
     * @throws IllegalArgumentException if {@code pOffset} is negative, or if {@code pLength} is
     *                                  negative, or if {@code pOffset} is greater than
     *                                  {@code pElements.length - pLength}.
     */
    @Nonnull
    static public <T> Sequence<T> wrap(
        @Nonnull T[] pElements,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        return pLength == 0 ? emptySequence() : new ArraySequence<>(pElements, pOffset, pLength);
    }


    /**
     * Immutable implementation of an empty {@code Sequence}.
     *
     * @param <E>   The sequence's element type.
     */
    static private class EmptySequence<E> implements Sequence<E>
    {
        static final Sequence<Object> INSTANCE = new EmptySequence<>();

        @Override
        @Nonnegative
        public int size()
        {
            return 0;
        }

        @Override
        @Nullable
        public E elementAt(int pIndex)
        {
            throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull Consumer<? super E> pAction)
        {
            // Nothing do to in an empty sequence.
        }

        @Override
        @Nonnull
        public Iterator<E> iterator()
        {
            return Collections.emptyIterator();
        }
    }


    /**
     * A {@code Sequence} with only one element.
     *
     * @param <E>   The sequence's element type.
     */
    static private class SingletonSequence<E> implements Sequence<E>
    {
        private final E fElement;

        /**
         * Create a new {@code SingletonSequence}.
         *
         * @param pElement  The sequence's sole element, possibly null.
         */
        SingletonSequence(@Nullable E pElement)
        {
            fElement = pElement;
        }

        @Override
        @Nonnegative
        public int size()
        {
            return 1;
        }

        @Override
        @Nullable
        public E elementAt(int pIndex)
        {
            if (pIndex == 0)
                return fElement;
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull Consumer<? super E> pAction)
        {
            pAction.accept(fElement);
        }

        @Override
        @Nonnull
        public Iterator<E> iterator()
        {
            return Iterators.singletonIterator(fElement);
        }
    }


    /**
     * Implementation of {@code Sequence} backed by a {@code List}.
     *
     * @param <E>   The sequence's element type.
     */
    static private class ListSequence<E> implements Sequence<E>
    {
        private final List<? extends E> fList;

        /**
         * Create a new {@code ListSequence}.
         *
         * @param pList The underlying list. This reference will be used, no copy will be made.
         *
         * @throws NullPointerException if {@code pList} is null.
         */
        ListSequence(@Nonnull List<? extends E> pList)
        {
            fList = requireNonNull(pList);
        }

        @Override
        @Nonnegative
        public int size()
        {
            return fList.size();
        }

        @Override
        @Nullable
        public E elementAt(int pIndex)
        {
            return fList.get(pIndex);
        }

        @Override
        public void forEach(@Nonnull Consumer<? super E> pAction)
        {
            fList.forEach(pAction);
        }

        @Override
        @Nonnull
        public Iterator<E> iterator()
        {
            return Iterators.unmodifiableIterator(fList.iterator());
        }
    }


    /**
     * Implementation of {@code Sequence} backed by (a subrange of) an array.
     *
     * @param <E>   The sequence's element type.
     */
    static private class ArraySequence<E> implements Sequence<E>
    {
        private final E[] fElements;
        private final int fOffset;
        private final int fLength;

        /**
         * Create a new {@code ArraySequence}.
         *
         * @param pElements The underlying array. This reference will be used, no copy will be made.
         * @param pOffset   The offset of the first element in the array that belongs to the
         *                  sequence.
         * @param pLength   The number of elements in the sequence, starting at the specified
         *                  offset.
         *
         * @throws NullPointerException if {@code pElements} is null.
         * @throws IllegalArgumentException if {@code pOffset} is negative, or if {@code pLength} is
         *                                  negative, or if {@code pOffset} is greater than
         *                                  {@code pElements.length - pLength}.
         */
        ArraySequence(@Nonnull E[] pElements, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fElements = requireNonNull(pElements);
            fOffset = requireNonNegative(pOffset);
            fLength= requireNonNegative(pLength);
            if (pOffset > pElements.length - pLength)
                throw new IllegalArgumentException(pOffset + ">" + pElements.length + "-" + pLength);
        }

        @Override
        @Nonnegative
        public int size()
        {
            return fLength;
        }

        @Override
        @Nullable
        public E elementAt(int pIndex)
        {
            if (pIndex >= 0 && pIndex < fLength)
                return fElements[fOffset + pIndex];
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull Consumer<? super E> pAction)
        {
            for (int i=0; i<fLength; i++)
                pAction.accept(fElements[fOffset + i]);
        }

        @Override
        @Nonnull
        public Iterator<E> iterator()
        {
            return Iterators.arrayIterator(fElements, fOffset, fLength);
        }
    }
}
