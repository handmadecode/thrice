/*
 * Copyright 2013, 2017, 2021-2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.myire.annotation.Unreachable;
import static org.myire.util.Numbers.requireRangeWithinBounds;


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
        return wrap(pElements, 0, pElements.length);
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
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength} is
     *                                   negative, or if {@code pOffset} is greater than
     *                                   {@code pElements.length - pLength}.
     */
    @Nonnull
    static public <T> Sequence<T> wrap(
        @Nonnull T[] pElements,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        if (pLength == 0)
            return emptySequence();
        else if (pLength == 1)
            return singleton(pElements[pOffset]);
        else
            return new ArraySequence<>(pElements, pOffset, pLength);
    }


    /**
     * Check if two {@code Sequence} instances are equal. Two sequences are equal if the have the
     * same size and contain equal elements in the same order. The equality of the elements are
     * tested with the specified predicate.
     *
     * @param pFirst        The first instance to check.
     * @param pSecond       The second instance to check.
     * @param pPredicate    A predicate to test element equality with.
     *
     * @param <E> The type used to test the elements for equality.
     *
     * @return  True if the two instances are equal or if both are null. False otherwise.
     *
     * @throws NullPointerException if {@code pPredicate} is null.
     */
    static public <E> boolean areEqual(
        @Nullable Sequence<? extends E> pFirst,
        @Nullable Sequence<? extends E> pSecond,
        @Nonnull BiPredicate<E, E> pPredicate)
    {
        // The two instances are equal if they are the same instance or both are null.
        if (pFirst == pSecond)
            return true;

        // If one of the instances is null but the other isn't they are not equal. Both cannot be
        // null here (that would have been caught above).
        if (pFirst == null || pSecond == null)
            return false;

        // Both instances are non-null, check their sizes.
        int aNumElements = pFirst.size();
        if (pSecond.size() != aNumElements)
            return false;

        // Equal sizes, compare the elements.
        for (int i=0; i<aNumElements; i++)
            if (!pPredicate.test(pFirst.elementAt(i), pSecond.elementAt(i)))
                // Elements differ at this position.
                return false;

        return true;
    }


    /**
     * Cast a sequence to a sequence of a supertype of its elements.
     *<p>
     * The main use case for this is a sequence with a non-public element type that should be
     * exposed as a sequence of a public element type, where the public type is a supertype of the
     * non-public type.
     *<p>
     * An example:
     *<pre>
     * public interface X {...}         // public type
     * class XImpl implements X {...}   // non-public type
     *
     * public class Example
     * {
     *   private Sequence&lt;XImpl&gt; xs;
     *
     *   public Sequence&lt;X&gt; getXs()
     *   {
     *     return Sequences.upCast(xs);
     *   }
     * }
     *</pre>
     *<p>
     * Note that this type of cast is not safe for mutable collections, since it would allow adding
     * elements of another subtype to the collection. Since sequences do not expose any methods for
     * mutating their contents, this kind of upcast can be allowed.
     *
     * @param pSequence The sequence to cast.
     * @param <T>   The element type of the cast sequence.
     *
     * @return  {@code pSequence} cast to a {@code Sequence<T>}.
     */
    @SuppressWarnings("unchecked")
    static public <T> Sequence<T> upCast(@Nullable Sequence<? extends T> pSequence)
    {
        return (Sequence<T>) pSequence;
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
         * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength}
         *                                   is negative, or if {@code pOffset} is greater than
         *                                   {@code pElements.length - pLength}.
         */
        ArraySequence(@Nonnull E[] pElements, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fElements = requireNonNull(pElements);
            fOffset = requireRangeWithinBounds(pOffset, pLength, pElements.length);
            fLength = pLength;
        }

        @Override
        @Nonnegative
        public int size()
        {
            return fLength;
        }

        @Override
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
