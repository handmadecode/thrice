/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;


/**
 * A {@code LongSequence} provides positional (indexed), read-only access to a sequence of
 * {@code long} values. It is a primitive specialization of {@link Sequence} for {@code long}
 * values.
 */
public interface LongSequence extends PrimitiveSequence<Long, LongConsumer>
{
    /**
     * Get the {@code long} value at a specific position in this sequence.
     *
     * @param pIndex    The index of the value to get.
     *
     * @return  The {@code long} value at the specified position in this sequence.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is less than 0 or greater than or equal
     *                                   to {@link #size()}.
     */
    long valueAt(@Nonnegative int pIndex);

    /**
     * Get the {@code Long} element at a specific position in this sequence.
     *
     * @param pIndex    The index of the element to get.
     *
     * @return  The value at the specified position in this sequence wrapped in a {@code Long},
     *          never null.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is less than 0 or greater than or equal
     *                                   to {@link #size()}.
     */
    @Nonnull
    @Override
    default Long elementAt(@Nonnegative int pIndex)
    {
        return Long.valueOf(valueAt(pIndex));
    }

    /**
     * Return a {@code PrimitiveIterator.OfLong} for the values in this sequence.
     *
     * @return  A {@code PrimitiveIterator.OfLong}, never null.
     */
    @Override
    @Nonnull
    PrimitiveIterator.OfLong iterator();
}
