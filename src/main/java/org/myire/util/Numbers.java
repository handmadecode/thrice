/*
 * Copyright 2009, 2012, 2017 Peter Franzen. All rights reserved.
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
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Numbers()
    {
        // Empty default ctor, defined to override access scope.
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
}