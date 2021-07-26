/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnegative;


/**
 * A {@code PutByteAtFunction} provides indexed write-only access to a sequence of bytes. The
 * function uses zero-based indices.
 */
@FunctionalInterface
public interface PutByteAtFunction
{
    /**
     * Put a {@code byte} at a specific index in the underlying byte sequence.
     *
     * @param pIndex    The index.
     * @param pValue    The byte value.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is negative or greater than the largest
     *                                   valid index in the byte sequence.
     */
    void putByteAt(@Nonnegative int pIndex, byte pValue);
}
