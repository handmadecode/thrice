/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnegative;


/**
 * A {@code PutCharAtFunction} provides indexed write-only access to a sequence of characters. The
 * function uses zero-based indices.
 */
@FunctionalInterface
public interface PutCharAtFunction
{
    /**
     * Put a {@code char} at a specific index in the underlying character sequence.
     *
     * @param pIndex    The index.
     * @param pValue    The char value.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is negative or greater than the largest
     *                                   valid index in the character sequence.
     */
    void putCharAt(@Nonnegative int pIndex, char pValue);
}
