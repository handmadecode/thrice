/*
 * Copyright 2023, 2025 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.text.ParseException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;


/**
 * Utility methods for durations of time.
 *<p>
 *<b>String representation</b>
 *<p>
 * A duration can be expressed in one or more time units:
 *<ul>
 * <li>d - days</li>
 * <li>h - hours</li>
 * <li>m - minutes</li>
 * <li>s - seconds</li>
 * <li>ms - milliseconds</li>
 * <li>u(s) - microseconds</li>
 * <li>n(s) - nanoseconds</li>
 *</ul>
 * A duration string is a sequence of non-negative numeric scalars and time units e.g.,
 * &quot;6d&quot;, &quot;2s400ms&quot;, or &quot;3h17m42s&quot;. Scalars and units can be
 * separated by whitespace, but need not be.
 *<p>
 * The time units need not appear in any special order, but no time unit can appear more than once.
 *<p>
 * The total number of nanoseconds expressed in the time units may not exceed
 * {@link Long#MAX_VALUE}. The maximum duration that fits into a signed 64-bit nanosecond value is
 * &quot;106751 d 23 h 47 m 16 s 854 ms 775 us 807 ns&quot;, or 292.3 years.
 */
public final class Durations
{
    static private final long LONG_MAX_DIVIDED_BY_TEN = Long.MAX_VALUE / 10;


    private Durations()
    {
        // Don't allow instantiations of utility classes.
    }


    /**
     * Parse a duration string into a number of nanoseconds.
     *
     * @param pDuration The characters containing the duration to parse.
     *
     * @return  The number of nanoseconds represented by the duration.
     *
     * @throws ParseException if {@code pDuration} doesn't contain a valid duration.
     * @throws NullPointerException if {@code pDuration} is null.
     */
    @Nonnegative
    static public long durationToNanos(@Nonnull CharSequence pDuration) throws ParseException
    {
        return durationToNanos(pDuration, 0, pDuration.length());
    }


    /**
     * Parse a duration string into a number of nanoseconds.
     *
     * @param pDuration The characters containing the duration to parse.
     * @param pOffset   The offset of the duration's first character.
     * @param pLength   The number of characters in the duration.
     *
     * @return  The number of nanoseconds represented by the duration.
     *
     * @throws ParseException if {@code pDuration} doesn't contain a valid duration.
     * @throws NullPointerException if {@code pDuration} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} and/or {@code pLength} specify an
     *                                   invalid range in {@code pDuration}.
     */
    @Nonnegative
    static public long durationToNanos(
        @Nonnull CharSequence pDuration,
        @Nonnegative int pOffset,
        @Nonnegative int pLength) throws ParseException
    {
        long aDurationNanos = 0;
        long aScalar = -1;
        DurationUnit aUnit = null;
        Token aCurrentToken = null, aPrevToken = null;
        int aParsedUnits = 0;

        int aPos = pOffset;
        int aEnd = pOffset + pLength;
        while (aPos < aEnd)
        {
            char aChar = pDuration.charAt(aPos);
            if (Character.isWhitespace(aChar))
            {
                // The whitespace implicitly finishes the current token.
                if (aCurrentToken == Token.UNIT)
                {
                    // A potential two-character unit but no second character; the candidate unit is
                    // the final unit for this components
                    aDurationNanos = finishComponent(aDurationNanos, aScalar, aUnit, aPos);
                    aParsedUnits = addParsedUnit(aParsedUnits, aUnit, aPos);
                    aScalar = -1;
                    aUnit = null;
                }

                if (aCurrentToken != null)
                {
                    // First whitespace character after a token, remember that token's type.
                    aPrevToken = aCurrentToken;
                    aCurrentToken = null;
                }
            }
            else if (Ascii.isAsciiDigit(aChar))
            {
                if (aCurrentToken == null && aPrevToken == Token.SCALAR)
                    // Two consecutive scalars separated by whitespace.
                    throw new ParseException("Missing unit", aPos);

                // If the current token is a unit, the first char in a potential two-char unit has
                // been encountered; the encountered digit means that there is no second unit char,
                // and that the component is finished.
                if (aCurrentToken == Token.UNIT)
                {
                    aDurationNanos = finishComponent(aDurationNanos, aScalar, aUnit, aPos);
                    aParsedUnits = addParsedUnit(aParsedUnits, aUnit, aPos);
                    aScalar = -1;
                    aUnit = null;
                    aPrevToken = Token.UNIT;
                }

                // Add the digit's value to the current scalar value.
                int aDigitValue = aChar - '0';
                if (aScalar > 0)
                {
                    if (aScalar > LONG_MAX_DIVIDED_BY_TEN)
                        // Multiplying aScalar with 10 will overflow.
                        throw new ParseException("Too large value", aPos);

                    aScalar *= 10;
                    if (aScalar <= Long.MAX_VALUE - aDigitValue)
                        aScalar += aDigitValue;
                    else
                        // Adding aDigitValue to aScalar will overflow.
                        throw new ParseException("Too large scalar", aPos);
                }
                else
                    // First (non-zero) digit.
                    aScalar = aDigitValue;

                aCurrentToken = Token.SCALAR;
            }
            else
            {
                // Either a unit character or an invalid character, fail on a missing scalar in both
                // cases.
                if (aScalar < 0)
                    throw new ParseException("Missing scalar", aPos);

                if (aCurrentToken == Token.UNIT)
                {
                    // The second char of a unit, only 's' is allowed.
                    if (aChar != 's')
                        throw new ParseException("Invalid unit", aPos);

                    // An 'm' followed by an 's' changes the unit, whereas the 's' following an 'u'
                    // or 'n' is allowed but redundant.
                    if (aUnit == DurationUnit.MINUTE)
                        aUnit = DurationUnit.MILLISECOND;

                    aCurrentToken = null;
                }
                else
                {
                    // No current unit, either a single char unit or the first char of a two-char
                    // unit.
                    aUnit = singleCharUnit(aChar);
                    if (aUnit != null)
                        // A single char unit, the component is finished.
                        aCurrentToken = null;
                    else
                    {
                        aUnit = firstCharUnit(aChar);
                        if (aUnit != null)
                            // The first char in a potential two-char unit, must examine the next
                            // char.
                            aCurrentToken = Token.UNIT;
                        else
                            // Neither a single char unit nor a potential two-char unit.
                            throw new ParseException("Invalid character '" + aChar + '\'', aPos);
                    }
                }

                if (aCurrentToken == null)
                {
                    // The unit is fully parsed.
                    aDurationNanos = finishComponent(aDurationNanos, aScalar, aUnit, aPos);
                    aParsedUnits = addParsedUnit(aParsedUnits, aUnit, aPos);
                    aScalar = -1;
                    aUnit = null;
                    aPrevToken = Token.UNIT;
                }
            }

            // Examine the next char.
            aPos++;
        }

        // End of characters, finish any pending unit. Any error is at the last character position.
        aPos--;
        if (aCurrentToken == Token.UNIT)
        {
            aDurationNanos = finishComponent(aDurationNanos, aScalar, aUnit, aPos);
            addParsedUnit(aParsedUnits, aUnit, aPos);
        }
        else if (aScalar >= 0)
            throw new ParseException("Missing unit", aPos);

        return aDurationNanos;
    }


    /**
     * Check if a character uniquely identifies a {@code DurationUnit}.
     *
     * @param pChar The character to check.
     *
     * @return  The {@code DurationUnit} uniquely identified by {@code pChar}, or null if
     *          {@code pChar} does not uniquely identify a {@code DurationUnit}.
     */
    private static DurationUnit singleCharUnit(char pChar)
    {
        switch (pChar)
        {
            case 'd': return DurationUnit.DAY;
            case 'h': return DurationUnit.HOUR;
            case 's': return DurationUnit.SECOND;
            default:  return null;
        }
    }


    /**
     * Check if a character is the first of a two-character {@code DurationUnit}.
     *
     * @param pChar The character to check.
     *
     * @return  The {@code DurationUnit} whose first character is {@code pChar}, or null if
     *          no two-character {@code DurationUnit} starts with {@code pChar}.
     */
    private static DurationUnit firstCharUnit(char pChar)
    {
        switch (pChar)
        {
            case 'm': return DurationUnit.MINUTE;
            case 'u': return DurationUnit.MICROSECOND;
            case 'n': return DurationUnit.NANOSECOND;
            default:  return null;
        }
    }


    /**
     * Finish a parsed duration string component by adding the nanoseconds represented by the scalar
     * and duration unit to a nanosecond value.
     *
     * @param pDurationNanos    The nanosecond value.
     * @param pScalar           The scalar.
     * @param pUnit             The duration unit.
     * @param pPos              The parse position to use for error reporting.
     *
     * @return  The sum of {@code pDurationNanos} and the nanosecond value of {@code pScalar} and
     *          {@code pUnit}.
     *
     * @throws ParseException   if {@code pScalar} is too large for {@code pUnit}, or if the sum is
     *                          greater than {@code Long.MAX_VALUE}.
     * @throws NullPointerException if {@code pUnit} is null.
     */
    static private long finishComponent(
        long pDurationNanos,
        long pScalar,
        DurationUnit pUnit,
        int pPos) throws ParseException
    {
        if (pUnit.isTooLargeScalar(pScalar))
            throw new ParseException("Too large scalar", pPos);

        long aUnitNanos = pUnit.toNanos(pScalar);
        if (pDurationNanos <= Long.MAX_VALUE - aUnitNanos)
            return pDurationNanos + aUnitNanos;
        else
            // Adding aUnitNanos to pDurationNanos will overflow.
            throw new ParseException("Too large duration", pPos);
    }


    /**
     * Add the bit representing a duration unit to a bit mask.
     *
     * @param pBitMask  The bit mask.
     * @param pUnit     The duration unit.
     * @param pPos      The parse position to use for error reporting.
     *
     * @return  The bitwise OR of the bit mask and the bit representing {@code pUnit}.
     *
     * @throws ParseException   if the bit representing {@code pUnit} already is set in
     *                          {@code pBitMask}.
     * @throws NullPointerException if {@code pUnit} is null.
     */
    static private int addParsedUnit(int pBitMask, DurationUnit pUnit, int pPos) throws ParseException
    {
        int aBit = 1 << pUnit.ordinal();
        if ((pBitMask & aBit) == 0)
            return pBitMask | aBit;
        else
            throw new ParseException("Multiple instances of time unit + " + pUnit, pPos);
    }


    /** The valid units in a duration string. */
    private enum DurationUnit
    {
        NANOSECOND(1L),
        MICROSECOND(1000L),
        MILLISECOND(1_000_000L),
        SECOND(1_000_000_000L),
        MINUTE(60L * 1_000_000_000L),
        HOUR(60L * 60L * 1_000_000_000L),
        DAY(24L * 60L * 60L * 1_000_000_000L)
        ;

        private final long fMaxScalar;
        private final long fToNanosFactor;


        DurationUnit(long pToNanosFactor)
        {
            fMaxScalar = Long.MAX_VALUE / pToNanosFactor;
            fToNanosFactor = pToNanosFactor;
        }

        boolean isTooLargeScalar(long pScalar)
        {
            return pScalar > fMaxScalar;
        }

        long toNanos(long pScalar)
        {
            return pScalar * fToNanosFactor;
        }
    }


    /** The types of tokens parsed in duration strings. */
    private enum Token { SCALAR, UNIT }
}
