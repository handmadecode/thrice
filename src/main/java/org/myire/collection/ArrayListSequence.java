/*
 * Copyright 2022-2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;


/**
 * An {@code ArrayList} that implements {@code Sequence}. The list is modifiable but provides a
 * read-only {@code Sequence} view without requiring a wrapper {@code Sequence} implementation. Note
 * that implementing {@code Sequence} implies that the {@code Iterator} returned by
 * {@link #iterator()} will <i>not</i> support the {@code remove} operation.
 *<p>
 * Instances of this class are <b>not</b> safe for use by multiple threads.
 *
 * @param <E>   The type of the list's/sequence's elements.
 */
@NotThreadSafe
public class ArrayListSequence<E> extends ArrayList<E> implements Sequence<E>
{
    /**
     * Create an empty list with the specified initial capacity.
     *
     * @param pInitialCapacity  The list's initial capacity.
     *
     * @throws IllegalArgumentException if {@code pInitialCapacity} is negative
     */
    public ArrayListSequence(@Nonnegative int pInitialCapacity)
    {
        super(pInitialCapacity);
    }


    /**
     * Create a list containing the elements of the specified collection. The elements will be added
     * to the list in the order they are returned by the collection's iterator.
     *
     * @param pElements A collection with the elements to populate this list with. No reference will
     *                  be kept to the collection, only to its elements.
     *
     * @throws NullPointerException if {@code pElements} is null.
     */
    public ArrayListSequence(@Nonnull Collection<? extends E> pElements)
    {
        super(pElements);
    }


    @Override
    public E elementAt(int pIndex)
    {
        return get(pIndex);
    }


    /**
     * Return an iterator over the elements in this list/sequence. The iteration order will be that
     * of the superclass. The returned iterator does <i>not</i> support the {@code remove}
     * operation.
     *
     * @return  An iterator over the elements in this list/sequence, never null.
     */
    @Override
    @Nonnull
    public Iterator<E> iterator()
    {
        return Iterators.unmodifiableIterator(super.iterator());
    }
}
