/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;


/**
 * A {@code GetCharAtFunction} provides indexed read-only access to a sequence of characters. The
 * function uses zero-based indices.
 */
@FunctionalInterface
public interface GetCharAtFunction
{
    /**
     * Get the {@code char} value at the specified index in the character sequence.
     *
     * @param pIndex    The index of the char to get.
     *
     * @return  The char value stored at the specified index in the sequence.
     *
     * @throws IndexOutOfBoundsException if {@code pIndex} is negative or greater than the largest
     *                                   valid index in the character sequence.
     */
    char charAt(@Nonnegative int pIndex);


    /**
     * Create a {@code String} from a range of characters returned by this function.
     *
     * @param pOffset   The offset of the first character in the range.
     * @param pLength   The number of characters in the range.
     *
     * @return  A new string, never null.
     *
     * @throws IndexOutOfBoundsException if {@code pOffset} is less than zero or greater than or
     *                                   equal to the number of available characters in the
     *                                   underlying character sequence, or if {@code pLength} is
     *                                   greater than the number of available characters minus the
     *                                   offset, or if {@code pLength} is negative.
     */
    @Nonnull
    default String charsToString(@Nonnegative int pOffset, @Nonnegative int pLength)
    {
        if (pOffset < 0)
            throw new IndexOutOfBoundsException(String.valueOf(pOffset));
        if (pLength < 0)
            throw new IndexOutOfBoundsException(String.valueOf(pLength));

        if (pLength == 0)
            return "";

        StringBuilder aBuilder = new StringBuilder(pLength);
        int aLast = pOffset + pLength;
        for (int i=pOffset; i<aLast; i++)
            aBuilder.append(charAt(i));

        return aBuilder.toString();
    }
}
