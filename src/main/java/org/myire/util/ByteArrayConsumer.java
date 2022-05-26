/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnull;


/**
 * A consumer of a (range of a) byte array.
 */
@FunctionalInterface
public interface ByteArrayConsumer
{
    /**
     * Consume all bytes in a byte array.
     *
     * @param pBytes    The bytes.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     */
    default void accept(@Nonnull byte[] pBytes)
    {
        accept(pBytes, 0, pBytes.length);
    }

    /**
     * Consume the bytes in a range of a byte array.
     *
     * @param pBytes    The bytes.
     * @param pOffset   The offset of the first byte to consume.
     * @param pLength   The number of bytes to consume.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     * @throws IndexOutOfBoundsException    if {@code pOffset} or {@code pLength} specify an invalid
     *                                      range in {@code pBytes}.
     */
    void accept(@Nonnull byte[] pBytes, int pOffset, int pLength);
}
