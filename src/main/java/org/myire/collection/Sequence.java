/*
 * Copyright 2013, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.function.Consumer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * A sequence provides positional (indexed) access to the elements in an ordered collection, such as
 * a {@code java.util.List} or an array.
 *<p>
 * Even though a sequence does not allow elements to be put into or removed from the underlying
 * structure, that structure may still be mutable, meaning that the size and elements may be
 * modified at any time by other threads. Implementations are generally not thread-safe; sharing a
 * sequence between threads requires external synchronization.
 *<p>
 * Whether or not duplicate elements and null elements are allowed is determined by the
 * implementation.
 *
 * @param <E>   The type of elements in the sequence.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public interface Sequence<E>
{
    /**
     * Get the number of elements in this sequence.
     *
     * @return  The number of elements.
     */
    @Nonnegative int size();

    /**
     * Get the element at a specific position in this sequence.
     *
     * @param pIndex    The index of the element to get.
     *
     * @return  The element at the specified position in this sequence.
     *
     * @throws IndexOutOfBoundsException    if {@code pIndex} is less than 0 or greater than or
     *                                      equal to {@link #size()}.
     */
    @Nullable E elementAt(int pIndex);

    /**
     * Perform an action for each element in the sequence. The action is performed sequentially in
     * the order of the elements. Exceptions thrown by the action are relayed to the caller.
     *
     * @param pAction   The action to perform for each element.
     *
     * @throws NullPointerException if the {@code pAction} is null.
     */
    void forEach(@Nonnull Consumer<? super E> pAction);
}
