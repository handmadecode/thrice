/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit tests for {@link org.myire.util.GetCharAtFunction}.
 */
public class GetCharAtFunctionTest
{
    /**
     * {@code charsToString} should return a string with the specified range of characters from the
     * underlying character sequence.
     */
    @Test
    public void charsToStringReturnsTheExpectedValue()
    {
        // Given
        int aOffset = 4, aLength = 3;
        String aChars = "abcdefghi";
        GetCharAtFunction aFunction = aChars::charAt;

        // When
        String aResult = aFunction.charsToString(aOffset, aLength);

        // Then
        assertEquals(aChars.substring(aOffset, aOffset + aLength), aResult);
    }


    /**
     * {@code charsToString} should return an empty string when passed a zero length argument.
     */
    @Test
    public void charsToStringReturnsEmptyStringForZeroLength()
    {
        // Given
        String aChars = "one flew over the cuckoo's nest";
        GetCharAtFunction aFunction = aChars::charAt;

        // When
        String aResult = aFunction.charsToString(2, 0);

        // Then
        assertTrue(aResult.isEmpty());
    }


    /**
     * {@code charsToString} should throw an {@code IndexOutOfBoundsException} when passed a
     * negative offset argument.
     */
    @Test
    public void charsToStringThrowsForNegativeOffsetArgument()
    {
        // Given
        GetCharAtFunction aFunction = "xxx"::charAt;

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> aFunction.charsToString(-1, 0)
        );
    }


    /**
     * {@code charsToString} should throw an {@code IndexOutOfBoundsException} when passed a
     * negative length argument.
     */
    @Test
    public void charsToStringThrowsForNegativeLengthArgument()
    {
        // Given
        GetCharAtFunction aFunction = "yyy"::charAt;

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> aFunction.charsToString(0, -1)
        );
    }


    /**
     * {@code charsToString} should throw an {@code IndexOutOfBoundsException} when passed an
     * invalid offset and length combination.
     */
    @Test
    public void factoryMethodThrowsForInvalidOffsetAndLength()
    {
        // Given
        GetCharAtFunction aFunction = "aa"::charAt;

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> aFunction.charsToString(1, 2)
        );
    }
}
