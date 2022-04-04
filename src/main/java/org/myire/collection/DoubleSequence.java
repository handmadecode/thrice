/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;


/**
 * A {@code DoubleSequence} provides positional (indexed), read-only access to a sequence of
 * {@code double} values. It is a primitive specialization of {@link Sequence} for {@code double}
 * values.
 */
public interface DoubleSequence extends PrimitiveSequence<Double, DoubleConsumer>
{
    /**
     * Get the {@code double} value at a specific position in this sequence.
     *
     * @param pIndex    The index of the value to get.
     *
     * @return  The {@code double} value at the specified position in this sequence.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is less than 0 or greater than or equal
     *                                   to {@link #size()}.
     */
    double valueAt(@Nonnegative int pIndex);


    /**
     * Get the {@code Double} element at a specific position in this sequence.
     *
     * @param pIndex    The index of the element to get.
     *
     * @return  The value at the specified position in this sequence wrapped in a {@code Double},
     *          never null.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is less than 0 or greater than or equal
     *                                   to {@link #size()}.
     */
    @Nonnull
    @Override
    default Double elementAt(@Nonnegative int pIndex)
    {
        return Double.valueOf(valueAt(pIndex));
    }

    /**
     * Return a {@code PrimitiveIterator.OfDouble} for the values in this sequence.
     *
     * @return  A {@code PrimitiveIterator.OfDouble}, never null.
     */
    @Override
    @Nonnull
    PrimitiveIterator.OfDouble iterator();
}
