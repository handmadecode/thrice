/*
 * Copyright 2013, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.List;
import java.util.function.Consumer;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.myire.annotation.Unreachable;


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
        return pElements.length == 0 ? emptySequence() : new ArraySequence<>(pElements);
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
            throw new IndexOutOfBoundsException("Index: "+pIndex);
        }

        @Override
        public void forEach(@Nonnull Consumer<? super E> pAction)
        {
            // Nothing do to in an empty sequence.
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
                throw new IndexOutOfBoundsException("Index: "+pIndex);
        }

        @Override
        public void forEach(@Nonnull Consumer<? super E> pAction)
        {
            pAction.accept(fElement);
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
            fList.iterator().forEachRemaining(pAction);
        }
    }


    /**
     * Implementation of {@code Sequence} backed by an array.
     *
     * @param <E>   The sequence's element type.
     */
    static private class ArraySequence<E> implements Sequence<E>
    {
        private final E[] fElements;

        /**
         * Create a new {@code ArraySequence}.
         *
         * @param pElements The underlying array. This reference will be used, no copy will be made.
         *
         * @throws NullPointerException if {@code pElements} is null.
         */
        ArraySequence(@Nonnull E[] pElements)
        {
            fElements = requireNonNull(pElements);
        }

        @Override
        @Nonnegative
        public int size()
        {
            return fElements.length;
        }

        @Override
        @Nullable
        public E elementAt(int pIndex)
        {
            return fElements[pIndex];
        }

        @Override
        public void forEach(@Nonnull Consumer<? super E> pAction)
        {
            for (E aElement : fElements)
                pAction.accept(aElement);
        }
    }
}
