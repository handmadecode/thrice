/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;


/**
 * An {@code IntSequence} provides positional (indexed), read-only access to a sequence of
 * {@code int} values. It is a primitive specialization of {@link Sequence} for {@code int} values.
 */
public interface IntSequence extends PrimitiveSequence<Integer, IntConsumer>
{
    /**
     * Get the {@code int} value at a specific position in this sequence.
     *
     * @param pIndex    The index of the value to get.
     *
     * @return  The {@code int} value at the specified position in this sequence.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is less than 0 or greater than or equal
     *                                   to {@link #size()}.
     */
    int valueAt(@Nonnegative int pIndex);

    /**
     * Get the {@code Integer} element at a specific position in this sequence.
     *
     * @param pIndex    The index of the element to get.
     *
     * @return  The value at the specified position in this sequence wrapped in an {@code Integer},
     *          never null.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is less than 0 or greater than or equal
     *                                   to {@link #size()}.
     */
    @Nonnull
    @Override
    default Integer elementAt(@Nonnegative int pIndex)
    {
        return Integer.valueOf(valueAt(pIndex));
    }

    /**
     * Return a {@code PrimitiveIterator.OfInt} for the values in this sequence.
     *
     * @return  A {@code PrimitiveIterator.OfInt}, never null.
     */
    @Override
    @Nonnull
    PrimitiveIterator.OfInt iterator();
}
