/*
 * Copyright 2009, 2012, 2017, 2020-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.myire.annotation.Unreachable;


/**
 * Integer and floating-point related utility methods.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Numbers
{
    // The digit in the tens position for values 0-99.
    static private final char[] DIGIT_TENS =
    {
        '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
        '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
        '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
        '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
        '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
        '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
        '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
        '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
        '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
        '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
    };

    // The digit in the ones position for values 0-99.
    @SuppressWarnings("CPD-START")
    static private final char[] DIGIT_ONES =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    // The positive ints that consist only of the digit '9'. The last element is Integer.MAX_VALUE.
    static private final int[] INT_NINES_ONLY = createIntNinesArray();

    // The positive longs that consist only of the digit '9'. The last element is Long.MAX_VALUE.
    static private final long[] LONG_NINES_ONLY = createLongNinesArray();

    // The characters of the string representation of Integer.MIN_VALUE and Long.MIN_VALUE.
    static private final char[] INTEGER_MIN_VALUE_CHARS = String.valueOf(Integer.MIN_VALUE).toCharArray();
    static private final char[] LONG_MIN_VALUE_CHARS = String.valueOf(Long.MIN_VALUE).toCharArray();


    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Numbers()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Check if the specified {@code byte} value is {@code 0} or positive and throw an
     * {@code IllegalArgumentException} if it isn't.
     *<p>
     * This method is similar to {@code java.util.Objects::requireNonNull}, meaning it is primarily
     * intended for parameter validation as in the example below:
     * <pre>
     * public void foo(@Nonnegative byte pValue)
     * {
     *     fValue = Numbers.requireNonNegative(pValue);
     * }
     * </pre>
     *
     * @param pValue    The value to check for negativity.
     *
     * @return  {@code pValue} (if not negative).
     *
     * @throws IllegalArgumentException if {@code pValue} is negative.
     */
    static public byte requireNonNegative(@Nonnegative byte pValue)
    {
        if (pValue < 0)
            throw new IllegalArgumentException(String.valueOf(pValue));
        return pValue;
    }


    /**
     * Check if the specified {@code short} value is {@code 0} or positive and throw an
     * {@code IllegalArgumentException} if it isn't.
     *<p>
     * This method is similar to {@code java.util.Objects::requireNonNull}, meaning it is primarily
     * intended for parameter validation as in the example below:
     * <pre>
     * public void foo(@Nonnegative short pValue)
     * {
     *     fValue = Numbers.requireNonNegative(pValue);
     * }
     * </pre>
     *
     * @param pValue    The value to check for negativity.
     *
     * @return  {@code pValue} (if not negative).
     *
     * @throws IllegalArgumentException if {@code pValue} is negative.
     */
    static public short requireNonNegative(@Nonnegative short pValue)
    {
        if (pValue < 0)
            throw new IllegalArgumentException(String.valueOf(pValue));
        return pValue;
    }


    /**
     * Check if the specified {@code int} value is {@code 0} or positive and throw an
     * {@code IllegalArgumentException} if it isn't.
     *<p>
     * This method is similar to {@code java.util.Objects::requireNonNull}, meaning it is primarily
     * intended for parameter validation as in the example below:
     * <pre>
     * public void foo(@Nonnegative int pValue)
     * {
     *     fValue = Numbers.requireNonNegative(pValue);
     * }
     * </pre>
     *
     * @param pValue    The value to check for negativity.
     *
     * @return  {@code pValue} (if not negative).
     *
     * @throws IllegalArgumentException if {@code pValue} is negative.
     */
    static public int requireNonNegative(@Nonnegative int pValue)
    {
        if (pValue < 0)
            throw new IllegalArgumentException(String.valueOf(pValue));
        return pValue;
    }


    /**
     * Check if the specified {@code long} value is {@code 0} or positive and throw an
     * {@code IllegalArgumentException} if it isn't.
     *<p>
     * This method is similar to {@code java.util.Objects::requireNonNull}, meaning it is primarily
     * intended for parameter validation as in the example below:
     * <pre>
     * public Foo(@Nonnegative long pValue)
     * {
     *     fValue = Numbers.requireNonNegative(pValue);
     * }
     * </pre>
     *
     * @param pValue    The value to check for negativity.
     *
     * @return  {@code pValue} if not negative.
     *
     * @throws IllegalArgumentException if {@code pValue} is negative.
     */
    static public long requireNonNegative(@Nonnegative long pValue)
    {
        if (pValue < 0)
            throw new IllegalArgumentException(String.valueOf(pValue));
        return pValue;
    }


    /**
     * Check if the specified {@code float} value is {@code 0.0f} or positive and throw an
     * {@code IllegalArgumentException} if it isn't.
     *<p>
     * This method is similar to {@code java.util.Objects::requireNonNull}, meaning it is primarily
     * intended for parameter validation as in the example below:
     * <pre>
     * public Foo(@Nonnegative float pValue)
     * {
     *     fValue = Numbers.requireNonNegative(pValue);
     * }
     * </pre>
     *
     * @param pValue    The value to check for negativity.
     *
     * @return  {@code pValue} if not negative.
     *
     * @throws IllegalArgumentException if {@code pValue} is negative.
     */
    static public float requireNonNegative(@Nonnegative float pValue)
    {
        // -0.0f == 0.0f, but Float.compare(-0.0f, 0.0f) == -1
        if (Float.compare(pValue, 0.0f) < 0)
            throw new IllegalArgumentException(String.valueOf(pValue));
        return pValue;
    }


    /**
     * Check if the specified {@code double} value is {@code 0.0d} or positive and throw an
     * {@code IllegalArgumentException} if it isn't.
     *<p>
     * This method is similar to {@code java.util.Objects::requireNonNull}, meaning it is primarily
     * intended for parameter validation as in the example below:
     * <pre>
     * public Foo(@Nonnegative double pValue)
     * {
     *     fValue = Numbers.requireNonNegative(pValue);
     * }
     * </pre>
     *
     * @param pValue    The value to check for negativity.
     *
     * @return  {@code pValue} if not negative.
     *
     * @throws IllegalArgumentException if {@code pValue} is negative.
     */
    static public double requireNonNegative(@Nonnegative double pValue)
    {
        // -0.0d == 0.0d, but Double.compare(-0.0d, 0.0d) == -1
        if (Double.compare(pValue, 0.0d) < 0)
            throw new IllegalArgumentException(String.valueOf(pValue));
        return pValue;
    }


    /**
     * Check if the sub-range from {@code pOffset} (inclusive) to {@code pOffset + pLength}
     * (exclusive) is within the bounds of the range from {@code 0} (inclusive) to
     * {@code pUpperBound} (exclusive).
     *<p>
     * The sub-range is defined to be out of bounds if any of the following inequalities is true:
     *<ul>
     * <li>{@code pOffset < 0}</li>
     * <li>{@code pLength < 0}</li>
     * <li>{@code pOffset + pLength > pUpperBound}, taking into account integer overflow</li>
     * <li>{@code pUpperBound < 0}, which is implied from the former inequalities</li>
     *</ul>
     * This is a backport of {@code java.util.Objects::checkFromIndexSize}.
     *
     * @param pOffset       The lower bound (inclusive) of the sub-range.
     * @param pLength       The pLength of the sub-range.
     * @param pUpperBound   The upper bound (exclusive) of the range.
     *
     * @return  {@code pOffset} if the sub-range within bounds of the range.
     *
     * @throws IndexOutOfBoundsException if the sub-range is out of bounds.
     */
    static public int requireRangeWithinBounds(int pOffset, int pLength, int pUpperBound)
    {
        if ((pUpperBound | pOffset | pLength) < 0 || pOffset > pUpperBound - pLength)
            throw new IndexOutOfBoundsException(
                "[" + pOffset + ", " + (pOffset+pLength) + "[ is not within [0, " + pUpperBound + '[');

        return pOffset;
    }


    /**
     * Wrapper around {@code Integer.parseInt()} that throws a checked exception instead of the
     * unchecked {@code NumberFormatException} in case of a malformed input string.
     *
     * @param pString   The string to parse into an {@code int}.
     *
     * @return  The string argument parsed into a signed decimal integer.
     *
     * @throws MalformedDataException   if {@code pString} does not contain a parsable integer.
     * @throws NullPointerException     if {@code pString} is null.
     */
    static public int parseInt(@Nonnull String pString) throws MalformedDataException
    {
        try
        {
            // Integer.parseInt throws a NumberFormatException for null input, call requireNonNull
            // to make an NPE be thrown instead,
            return Integer.parseInt(requireNonNull(pString));
        }
        catch (NumberFormatException nfe)
        {
            throw new MalformedDataException(nfe);
        }
    }


    /**
     * Wrapper around {@code Long.parseLong()} that throws a checked  exception instead of the
     * unchecked {@code NumberFormatException} in case of a malformed input string.
     *
     * @param pString   The string to parse into a {@code long}.
     *
     * @return  The string argument parsed into a signed decimal long integer.
     *
     * @throws MalformedDataException   if {@code pString} does not contain a parsable long integer.
     * @throws NullPointerException     if {@code pString} is null.
     */
    static public long parseLong(String pString) throws MalformedDataException
    {
        try
        {
            // Long.parseLong throws a NumberFormatException for null input, call requireNonNull
            // to make an NPE be thrown instead,
            return Long.parseLong(requireNonNull(pString));
        }
        catch (NumberFormatException nfe)
        {
            throw new MalformedDataException(nfe);
        }
    }


    /**
     * Format an {@code int} value by passing its digits to a {@code PutCharAtFunction}.
     *
     * @param pValue        The value to format.
     * @param pDestination  The destination for the digits the formatting results in.
     * @param pOffset       The index in {@code pDestination} to put the first digit at.
     *
     * @return  The number of digits passed to the destination.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is an invalid index in the destination,
     *                                   or the number of digits in {@code pValue} is greater than
     *                                   what can be put into the destination from {@code pOffset}.
     */
    static public int formatInt(
        int pValue,
        @Nonnull PutCharAtFunction pDestination,
        @Nonnegative int pOffset)
    {
        // Integer.MIN_VALUE is a special case.
        if (pValue == Integer.MIN_VALUE)
        {
            for (int i=0; i<INTEGER_MIN_VALUE_CHARS.length; i++)
                pDestination.putCharAt(pOffset + i, INTEGER_MIN_VALUE_CHARS[i]);

            return INTEGER_MIN_VALUE_CHARS.length;
        }
        else if (pValue < 0)
        {
            // Format negative values as the digits of the absolute value preceded by a minus sign.

            pValue = -pValue;
            int aNumAbsoluteDigits = countDigits(pValue);
            formatPositiveInt(pValue, aNumAbsoluteDigits, pDestination, pOffset + 1);
            pDestination.putCharAt(pOffset, '-');
            return aNumAbsoluteDigits + 1;
        }
        else
        {
            int aNumDigits = countDigits(pValue);
            formatPositiveInt(pValue, aNumDigits, pDestination, pOffset);
            return aNumDigits;
        }
    }


    /**
     * Format a {@code long} value by passing its digits to a {@code PutCharAtFunction}.
     *
     * @param pValue        The value to format.
     * @param pDestination  The destination for the digits the formatting results in.
     * @param pOffset       The index in {@code pDestination} to put the first digit at.
     *
     * @return  The number of digits passed to the destination.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is an invalid index in the destination,
     *                                   or the number of digits in {@code pValue} is greater than
     *                                   what can be put into the destination from {@code pOffset}.
     */
    static public int formatLong(
        long pValue,
        @Nonnull PutCharAtFunction pDestination,
        @Nonnegative int pOffset)
    {
        // This code is derived from java.lang.Long::getChars.

        // Long.MIN_VALUE is a special case.
        if (pValue == Long.MIN_VALUE)
        {
            for (int i=0; i<LONG_MIN_VALUE_CHARS.length; i++)
                pDestination.putCharAt(pOffset + i, LONG_MIN_VALUE_CHARS[i]);

            return LONG_MIN_VALUE_CHARS.length;
        }

        boolean aNegative = pValue < 0;
        if (aNegative)
            pValue = -pValue;

        // Put the digits from "right to left", starting with the least significant digit at the
        // last position of the fixed length.
        int aNumDigits = countDigits(pValue);
        int aLastPos = pOffset + aNumDigits - 1;
        if (aNegative)
            aLastPos++;

        int aPos = aLastPos;

        // Get the 2 least significant digits per iteration using longs until the quotient fits into
        // an int.
        long aQuotient;
        int aRemainder;
        while (pValue > Integer.MAX_VALUE)
        {
            // q = n/100, r = n - q * 100
            // Substitute q*100 == q*(64 + 32 + 4) == q*64 + q*32 + q*4
            aQuotient = pValue / 100;
            aRemainder = (int) (pValue - ((aQuotient << 6) + (aQuotient << 5) + (aQuotient << 2)));

            // The remainder is in the range 0-99 holding the two least significant digits.
            pDestination.putCharAt(aPos--, DIGIT_ONES[aRemainder]);
            pDestination.putCharAt(aPos--, DIGIT_TENS[aRemainder]);

            pValue = aQuotient;
        }

        // Format the rest of the value as an int.
        int aNumIntDigits = aNumDigits - (aLastPos - aPos);
        if (aNegative)
        {
            formatPositiveInt((int) pValue, aNumIntDigits, pDestination, pOffset + 1);
            pDestination.putCharAt(pOffset, '-');
            return aNumDigits + 1;
        }
        else
        {
            formatPositiveInt((int) pValue, aNumIntDigits, pDestination, pOffset);
            return aNumDigits;
        }
    }


    /**
     * Format a positive {@code int} value by passing its digits to a {@code PutCharAtFunction}.
     *
     * @param pValue        The {@code int} value to format.
     * @param pNumDigits    The number of digits in the value.
     * @param pDestination  The destination for the digits the formatting results in.
     * @param pOffset       The index in {@code pDestination} to put the first digit at.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is an invalid index in the destination,
     *                                   or {@code pOffset + pNumDigits} is greater than or equal to
     *                                   the largest valid index in {@code pDestination}.
     */
    static private void formatPositiveInt(
        int pValue,
        @Nonnegative int pNumDigits,
        @Nonnull PutCharAtFunction pDestination,
        @Nonnegative int pOffset)
    {
        // This code is derived from java.lang.Long::getChars.
        // Put the digits from "right to left", starting with the least significant digit at the
        // last position counted from the offset.
        int aPos = pOffset + pNumDigits - 1;

        // Get the 2 least significant digits per iteration until the quotient fits into 16 bits.
        int aQuotient, aRemainder;
        while (pValue >= 65536)
        {
            // q = n/100, r = n - q * 100
            // Substitute q*100 == q*(64 + 32 + 4) == q*64 + q*32 + q*4
            aQuotient = pValue / 100;
            aRemainder = pValue - ((aQuotient << 6) + (aQuotient << 5) + (aQuotient << 2));

            // The remainder is in the range 0-99 holding the two least significant digits.
            pDestination.putCharAt(aPos--, DIGIT_ONES[aRemainder]);
            pDestination.putCharAt(aPos--, DIGIT_TENS[aRemainder]);

            pValue = aQuotient;
        }

        // Get the least significant digit per iteration for values that fit into 16 bits
        do
        {
            // n * 52429 / 2^^19 == n * 52429 / 524288 == n / 10 (for values less than 262149).
            // See e.g. https://stackoverflow.com/questions/5558492/divide-by-10-using-bit-shifts#56217740
            aQuotient = (pValue * 52429) >>> 19;

            // r = n - q * 10
            // Substitute q*10 == q*(8 + 2) == q*8 + q*2
            aRemainder = pValue - ((aQuotient << 3) + (aQuotient << 1));

            pDestination.putCharAt(aPos--, DIGIT_ONES[aRemainder]);

            pValue = aQuotient;
        }
        while (pValue != 0);
    }


    /**
     * Check if a positive 32-bit value is a power of two.
     *
     * @param pValue    The value to check.
     *
     * @return  True if {@code pValue} is a power of 2, false otherwise. Since {@code 2**0 == 1},
     *          true if returned for {@code 1}. If {@code pValue} is zero or negative the result is
     *          possibly incorrect.
     */
    static public boolean isPowerOf2(@Nonnegative int pValue)
    {
        // If a value is a power of 2 only one bit is set, and the value minus one has all bits
        // lower than this bit set, meaning that the two have no bits set in common.
        return (pValue & (pValue - 1)) == 0;
    }


    /**
     * Get the nearest power of 2 that is greater than or equal to an integer value.
     *
     * @param pValue    The int value to get the nearest greater power of 2 for.
     *
     * @return  The smallest power of 2 that is greater than or equal to {@code pValue}. 0 is
     *          returned for negative values and for positive values greater than the largest power
     *          of 2 that can be represented by a signed 32-bit integer, i.e. {@code 0x40000000}.
     */
    @Nonnegative
    static public int roundUpToPowerOf2(int pValue)
    {
        // This code is taken from Henry Warren's Hacker's Delight,
        // http://www.hackersdelight.org/hdcodetxt/clp2.c.txt
        if (pValue < 0 || pValue > 0x40000000)
            return 0;

        pValue = pValue - 1;
        pValue = pValue | (pValue >> 1);
        pValue = pValue | (pValue >> 2);
        pValue = pValue | (pValue >> 4);
        pValue = pValue | (pValue >> 8);
        pValue = pValue | (pValue >> 16);
        return pValue + 1;
    }


    /**
     * Get the absolute value of a 32-bit integer value. If the value is not negative, the value
     * itself is returned. If the value is negative, the negation of the value is returned.
     *<p>
     * Note that if the value is {@code Integer.MIN_VALUE}, the result will be
     * {@code Integer.MIN_VALUE}, since its positive counterpart cannot be represented with a
     * 32-bit integer.
     *
     * @param pValue    The value to get the absolute value of.
     *
     * @return  The absolute value of {@code pValue}.
     */
    @Nonnegative
    static public int abs(int pValue)
    {
        // Hacker's Delight 2-4
        int aMask = pValue >> 31;
        return (aMask ^ pValue) - aMask;
    }


    /**
     * Get the absolute value of a 64-bit integer value. If the value is not negative, the value
     * itself is returned. If the value is negative, the negation of the value is returned.
     *<p>
     * Note that if the value is {@code Long.MIN_VALUE}, the result will be {@code Long.MIN_VALUE},
     * since its positive counterpart cannot be represented with a 64-bit integer.
     *
     * @param pValue    The value to get the absolute value of.
     *
     * @return  The absolute value of {@code pValue}.
     */
    @Nonnegative
    static public long abs(long pValue)
    {
        // Hacker's Delight 2-4
        long aMask = pValue >> 63;
        return (aMask ^ pValue) - aMask;
    }


    /**
     * Get the number of digits in a positive 32-bit value.
     *
     * @param pValue    The value.
     *
     * @return  The number of digits in {@code pValue}. If {@code pValue} is negative the value 1
     *          is returned.
     */
    static public int countDigits(int pValue)
    {
        int aNumDigits = 0;
        while (true)
        {
            if (pValue <= INT_NINES_ONLY[aNumDigits++])
                return aNumDigits;
        }
    }


    /**
     * Get the number of digits in a positive 64-bit value.
     *
     * @param pValue    The value.
     *
     * @return  The number of digits in {@code pValue}. If {@code pValue} is negative the value 1
     *          is returned.
     */
    static public int countDigits(long pValue)
    {
        int aNumDigits = 0;
        while (true)
        {
            if (pValue <= LONG_NINES_ONLY[aNumDigits++])
                return aNumDigits;
        }
    }


    /**
     * Create an array of all positive 32-bit integer values having only the digit '9'.
     *
     * @return  An array with the values 9, 99, 999, etc. The last element will be
     *          {@code Integer.MAX_VALUE}.
     */
    @Nonnull
    static private int[] createIntNinesArray()
    {
        // Element n in the array will have the value 10^^(n+1) - 1, e.g.
        // element 0 will have the value 10^^1 - 1 == 9
        // element 2 will have the value 10^^3 - 1 == 999
        // The largest power of 10 that fits into an int is the integral part of the 10-logarithm of
        // Integer.MAX_VALUE:
        //   n = (int) log10(Integer.MAX_VALUE) ---> 10^^n <= Integer.MAX_VALUE < 10^^(n+1)
        int aLargestPower = (int) Math.log10(Integer.MAX_VALUE);
        // Allocate room for all nines-only integers and Integer.MAX_VALUE.
        int[] aNines = new int[aLargestPower + 1];
        int aPower10 = 10; // 10^^1
        for (int i=0; i<aLargestPower; i++)
        {
            aNines[i] = aPower10 - 1;
            aPower10 *= 10;
        }

        aNines[aNines.length - 1] = Integer.MAX_VALUE;
        return aNines;
    }


    /**
     * Create an array of all positive 64-bit integer values having only the digit '9'.
     *
     * @return  An array with the values 9L, 99L, 999L, etc. The last element will be
     *          {@code Long.MAX_VALUE}.
     */
    static private long[] createLongNinesArray()
    {
        // Element n in the array will have the value 10^^(n+1) - 1, e.g.
        // element 0 will have the value 10^^1 - 1 == 9
        // element 2 will have the value 10^^3 - 1 == 999
        // The largest power of 10 that fits into a long is the integral part of the 10-logarithm of
        // Long.MAX_VALUE:
        //   n = (int) log10(Long.MAX_VALUE) ---> 10^^n <= Long.MAX_VALUE < 10^^(n+1)
        int aLargestPower = (int) Math.log10(Long.MAX_VALUE);
        // Allocate room for all nines-only long integers and Long.MAX_VALUE.
        long[] aNines = new long[aLargestPower + 1];
        long aPower10 = 10; // 10^^1
        for (int i=0; i<aLargestPower; i++)
        {
            aNines[i] = aPower10 - 1;
            aPower10 *= 10L;
        }

        aNines[aNines.length - 1] = Long.MAX_VALUE;
        return aNines;
    }
}
