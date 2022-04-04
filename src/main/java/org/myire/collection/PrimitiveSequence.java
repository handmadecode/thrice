/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;

import javax.annotation.Nonnull;


/**
 * A {@code PrimitiveSequence} provides positional (indexed), read-only access to a sequence of
 * primitive values, e.g. {@code int} or {@code double} values. It is a primitive specialization of
 * {@link Sequence}.
 *
 * @param <T>   The wrapper type of the primitive values in the sequence, for example
 *              {@code Integer} for sequences of {@code int} values.
 * @param <C>   The type of primitive consumer that {@link #forEach(Object)} passes the primitive
 *              values to, must be a primitive specialization of {@code Consumer}, for example
 *              {@code IntConsumer} for sequences of {@code int} values.
 */
public interface PrimitiveSequence<T extends Number, C> extends Sequence<T>
{
    /**
     * Pass each value in the sequence to the specified action. The values are passed in ascending
     * index order, starting at index 0. If the action throws an exception the process is aborted
     * and the exception is relayed to the caller.
     * <p>
     * The behavior of this method is unspecified if the action performs side effects that modify
     * the underlying source of primitive values.
     *
     * @param pAction   The action to pass each primitive value to.
     *
     * @throws NullPointerException if {@code pAction} is null.
     */
    void forEach(@Nonnull C pAction);

    /**
     * Return a {@code PrimitiveIterator} for the values in this sequence.
     *
     * @return  A {@code PrimitiveIterator}, never null.
     */
    @Override
    @Nonnull
    PrimitiveIterator<T, C> iterator();
}
