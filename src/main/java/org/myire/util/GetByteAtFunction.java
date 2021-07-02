/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnegative;


/**
 * A {@code GetByteAtFunction} provides indexed read-only access to a sequence of bytes. The
 * function uses zero-based indices.
 */
@FunctionalInterface
public interface GetByteAtFunction
{
    /**
     * Get the {@code byte} value at the specified index in the byte sequence.
     *
     * @param pIndex    The index of the byte to get.
     *
     * @return  The byte value stored at the specified index in the sequence.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is negative or greater than the largest
     *                                   valid index in the byte sequence.
     */
    byte byteAt(@Nonnegative int pIndex);
}
