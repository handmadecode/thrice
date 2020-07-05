/*
 * Copyright 2009, 2012, 2016-2017, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit tests for {@link org.myire.util.Numbers}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class NumbersTest
{
    /**
     * The {@code requireNonNegative(int)} method should return a {@code 0} argument.
     */
    @Test
    public void requireNonNegativeReturnsZeroInt()
    {
        assertEquals(0, Numbers.requireNonNegative(0));
    }


    /**
     * The {@code requireNonNegative(int)} method should return any positive argument.
     */
    @Test
    public void requireNonNegativeReturnsPositiveInt()
    {
        // Given
        int aValue = randomPositiveInt();

        // Then
        assertEquals(aValue, Numbers.requireNonNegative(aValue));
    }


    /**
     * The {@code requireNonNegative(int)} method should throw an {@code IllegalArgumentException}
     * for any negative argument.
     */
    @Test
    public void requireNonNegativeThrowsForNegativeInt()
    {
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Numbers.requireNonNegative(randomNegativeInt())
        );
    }


    /**
     * The {@code requireNonNegative(long)} method should return a {@code 0} argument.
     */
    @Test
    public void requireNonNegativeReturnsZeroLong()
    {
        assertEquals(0L, Numbers.requireNonNegative(0L));
    }


    /**
     * The {@code requireNonNegative(long)} method should return any positive argument.
     */
    @Test
    public void requireNonNegativeReturnsPositiveLong()
    {
        // Given
        long aValue = randomPositiveLong();

        // Then
        assertEquals(aValue, Numbers.requireNonNegative(aValue));
    }


    /**
     * The {@code requireNonNegative(long)} method should throw an {@code IllegalArgumentException}
     * for any negative argument.
     */
    @Test
    public void requireNonNegativeThrowsForNegativeLong()
    {
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Numbers.requireNonNegative(randomNegativeLong())
        );
    }


    /**
     * The {@code parseInt} method should parse valid integer values and return the expected
     * {@code int}.
     */
    @Test
    public void parseIntParsesValidIntegers()
    {
        assertAll(
            () -> assertEquals(0, Numbers.parseInt("0")),
            () -> assertEquals(0, Numbers.parseInt("00")),
            () -> assertEquals(1, Numbers.parseInt("01")),
            () -> assertEquals(-1, Numbers.parseInt("-1")),
            () -> assertEquals(-2, Numbers.parseInt("-0002")),
            () -> assertEquals(20, Numbers.parseInt("020")),
            () -> assertEquals(-10, Numbers.parseInt("-0010")),
            () -> assertEquals(Integer.MAX_VALUE, Numbers.parseInt(String.valueOf(Integer.MAX_VALUE))),
            () -> assertEquals(Integer.MIN_VALUE, Numbers.parseInt(String.valueOf(Integer.MIN_VALUE)))
        );
    }


    /**
     * The {@code parseInt} method should throw a {@code NullPointerException} for a null argument.
     */
    @Test
    public void parseIntThrowsForNullArgument()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                Numbers.parseInt(null)
        );
    }


    /**
     * The {@code parseInt} method should throw a {@code MalformedDataException} for a malformed
     * argument.
     */
    @Test
    public void parseIntThrowsForMalformedArgument()
    {
        assertThrows(
            MalformedDataException.class,
            () ->
                Numbers.parseInt("3.14")
        );
    }


    /**
     * The {@code parseInt} method should throw a {@code MalformedDataException} for an empty
     * string.
     */
    @Test
    public void parseIntThrowsForEmptyString()
    {
        assertThrows(
            MalformedDataException.class,
            () ->
                Numbers.parseInt("")
        );
    }


    /**
     * The {@code parseInt} method should throw a {@code MalformedDataException} for a string with a
     * value that is greater than {@code Integer.MAX_VALUE}.
     */
    @Test
    public void parseIntThrowsForTooLargeValue()
    {
        assertThrows(
            MalformedDataException.class,
            () ->
                Numbers.parseInt("2147483648")
        );
    }


    /**
     * The {@code parseLong} method should parse valid long values and return the expected
     * {@code long}.
     */
    @Test
    public void parseLongParsesValidLongs()
    {
        assertAll(
            () -> assertEquals(0, Numbers.parseLong("0")),
            () -> assertEquals(0, Numbers.parseLong("00")),
            () -> assertEquals(1, Numbers.parseLong("01")),
            () -> assertEquals(-1, Numbers.parseLong("-1")),
            () -> assertEquals(-2, Numbers.parseLong("-0002")),
            () -> assertEquals(20, Numbers.parseLong("020")),
            () -> assertEquals(-10, Numbers.parseLong("-0010")),
            () -> assertEquals(Long.MAX_VALUE, Numbers.parseLong(String.valueOf(Long.MAX_VALUE))),
            () -> assertEquals(Long.MIN_VALUE, Numbers.parseLong(String.valueOf(Long.MIN_VALUE)))
        );
    }


    /**
     * The {@code parseLong} method should throw a {@code NullPointerException} for a null argument.
     */
    @Test
    public void parseLongThrowsForNullArgument()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                Numbers.parseLong(null)
        );
    }


    /**
     * The {@code parseLong} method should throw a {@code MalformedDataException} for a malformed
     * argument.
     */
    @Test
    public void parseLongThrowsForMalformedArgument()
    {
        assertThrows(
            MalformedDataException.class,
            () ->
                Numbers.parseLong("3.14")
        );
    }


    /**
     * The {@code parseLong} method should throw a {@code MalformedDataException} for an empty
     * string.
     */
    @Test
    public void parseLongThrowsForEmptyString()
    {
        assertThrows(
            MalformedDataException.class,
            () ->
                Numbers.parseLong("")
        );
    }


    /**
     * The {@code parseLong} method should throw a {@code MalformedDataException} for a string with
     * a value that is greater than {@code Long.MAX_VALUE}.
     */
    @Test
    public void parseLongThrowsForTooLargeValue()
    {
        assertThrows(
            MalformedDataException.class,
            () ->
                Numbers.parseLong("9223372036854775808")
        );
    }


    /**
     * The {@code isPowerOf2} method should return true for all positive 32-bit values that are a
     * power of 2.
     */
    @Test
    public void isPowerOf2ReturnsTrueForPowerOf2()
    {
        for (int i=1; i<32; i++)
            assertTrue(Numbers.isPowerOf2(1 << i));
    }


    /**
     * The {@code isPowerOf2} method should return false for positive 32-bit values that are not a
     * power of 2.
     */
    @Test
    public void isPowerOf2ReturnsFalseForNonPowerOf2()
    {
        for (int i=2; i<32; i++)
        {
            assertFalse(Numbers.isPowerOf2((1 << i) - 1));
            assertFalse(Numbers.isPowerOf2((1 << i) + 1));
        }
    }


    /**
     * The {@code roundUpToPowerOf2} method should return the correct value for positive integers
     * less than or equal to the largest power of 2 that fits into a signed 32-bit integer.
     */
    @Test
    public void roundUpToPowerOf2ReturnsTheCorrectValue()
    {
        assertAll(
            () -> assertEquals(0, Numbers.roundUpToPowerOf2(0)),

            () -> assertEquals(1, Numbers.roundUpToPowerOf2(1)),

            () -> assertEquals(2, Numbers.roundUpToPowerOf2(2)),

            () -> assertEquals(4, Numbers.roundUpToPowerOf2(3)),
            () -> assertEquals(4, Numbers.roundUpToPowerOf2(4)),

            () -> assertEquals(8, Numbers.roundUpToPowerOf2(5)),
            () -> assertEquals(8, Numbers.roundUpToPowerOf2(7)),
            () -> assertEquals(8, Numbers.roundUpToPowerOf2(8)),

            () -> assertEquals(16, Numbers.roundUpToPowerOf2(9)),
            () -> assertEquals(16, Numbers.roundUpToPowerOf2(11)),
            () -> assertEquals(16, Numbers.roundUpToPowerOf2(15)),
            () -> assertEquals(16, Numbers.roundUpToPowerOf2(16)),

            () -> assertEquals(0x10000, Numbers.roundUpToPowerOf2(0xfffe)),
            () -> assertEquals(0x10000, Numbers.roundUpToPowerOf2(0xffff)),
            () -> assertEquals(0x10000, Numbers.roundUpToPowerOf2(0x10000)),
            () -> assertEquals(0x20000, Numbers.roundUpToPowerOf2(0x10001)),

            () -> assertEquals(0x40000000, Numbers.roundUpToPowerOf2(0x3fffffff)),
            () -> assertEquals(0x40000000, Numbers.roundUpToPowerOf2(0x40000000))
        );
    }


    /**
     * The {@code roundUpToPowerOf2} method should return {@code 0} for negative values.
     */
    @Test
    public void roundUpToPowerOf2ReturnsZeroForNegativeValues()
    {
        assertAll(
            () -> assertEquals(0, Numbers.roundUpToPowerOf2(-1)),
            () -> assertEquals(0, Numbers.roundUpToPowerOf2(Integer.MIN_VALUE))
        );
    }


    /**
     * The {@code roundUpToPowerOf2} method should return {@code 0} for values greater than the
     * largest power of 2 that fits into a signed 32-bit integer.
     */
    @Test
    public void roundUpToPowerOf2ReturnsZeroForTooLargeValues()
    {
        assertAll(
            () -> assertEquals(0, Numbers.roundUpToPowerOf2(0x40000001)),
            () -> assertEquals(0, Numbers.roundUpToPowerOf2(Integer.MAX_VALUE))
        );
    }

    @Test
    public void absReturnsSameValueForPositiveInt()
    {
        // Given
        int[] aValues = {0, 1, 2, 4, 7, 16, 33, 78, 255, 256, 32763, Integer.MAX_VALUE};

        // When
        for (int i : aValues)
            assertEquals(i, Numbers.abs(i));
    }

    @Test
    public void absReturnsSameValueForRandomPositiveInt()
    {
        // When
        for (int i=0; i<100; i++)
        {
            int aValue = randomPositiveInt();
            assertEquals(aValue, Numbers.abs(aValue));
        }
    }

    @Test
    public void absReturnsPositiveValueForNegativeInt()
    {
        // Given
        int[] aValues = {-1, -2, -4, -7, -16, -33, -78, -255, -256, -32763, Integer.MIN_VALUE + 1};

        // When
        for (int i : aValues)
            assertEquals(-i, Numbers.abs(i));
    }

    @Test
    public void absReturnsSameValueForRandomNegativeInt()
    {
        // When
        for (int i=0; i<100; i++)
        {
            int aValue = randomNegativeInt();
            assertEquals(-aValue, Numbers.abs(aValue));
        }
    }

    @Test
    public void absReturnsSameValueForIntegerMinValue()
    {
        // Given
        assertEquals(Integer.MIN_VALUE, Numbers.abs(Integer.MIN_VALUE));
    }

    @Test
    public void absReturnsSameValueForPositiveLong()
    {
        // Given
        long[] aValues = {0, 1, 2, 4, 7, 16, 33, 78, 255, 256, 32763, Long.MAX_VALUE};

        // When
        for (long i : aValues)
            assertEquals(i, Numbers.abs(i));
    }

    @Test
    public void absReturnsSameValueForRandomPositiveLong()
    {
        // When
        for (int i=0; i<100; i++)
        {
            long aValue = randomPositiveLong();
            assertEquals(aValue, Numbers.abs(aValue));
        }
    }

    @Test
    public void absReturnsPositiveValueForNegativeLong()
    {
        // Given
        long[] aValues = {-1, -2, -4, -7, -16, -33, -78, -255, -256, -32763, Long.MIN_VALUE + 1};

        // When
        for (long i : aValues)
            assertEquals(-i, Numbers.abs(i));
    }

    @Test
    public void absReturnsSameValueForRandomNegativeLong()
    {
        // When
        for (int i=0; i<100; i++)
        {
            long aValue = randomNegativeLong();
            assertEquals(-aValue, Numbers.abs(aValue));
        }
    }

    @Test
    public void absReturnsSameValueForLongMinValue()
    {
        // Given
        assertEquals(Long.MIN_VALUE, Numbers.abs(Long.MIN_VALUE));
    }


    static private int randomPositiveInt()
    {
        return ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    }


    static private int randomNegativeInt()
    {
        return ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE + 1, 0);
    }


    static private long randomPositiveLong()
    {
        return ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
    }


    static private long randomNegativeLong()
    {
        return ThreadLocalRandom.current().nextLong(Long.MIN_VALUE + 1, 0);
    }
}
