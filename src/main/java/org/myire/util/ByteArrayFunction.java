/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnull;


/**
 * A function that accepts a (range of a) byte array as input and produces a result.
 *
 * @param <R>   The type of the result produced by this function.
 */
@FunctionalInterface
public interface ByteArrayFunction<R>
{
    /**
     * Apply this function to the specified byte array.
     *
     * @param pBytes    The bytes to apply this function to.
     *
     * @return  The result of applying this function to the specified byte array.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     */
    default R apply(@Nonnull byte[] pBytes)
    {
        return apply(pBytes, 0, pBytes.length);
    }

    /**
     * Apply this function to the specified byte array range.
     *
     * @param pBytes    The bytes to apply this function to.
     * @param pOffset   The offset of the first byte to apply the function to.
     * @param pLength   The number of bytes to apply the function to.
     *
     * @return  The result of applying this function to the specified byte array range.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     * @throws IndexOutOfBoundsException    if {@code pOffset} or {@code pLength} specify an invalid
     *                                      range in {@code pBytes}.
     */
    R apply(@Nonnull byte[] pBytes, int pOffset, int pLength);
}
