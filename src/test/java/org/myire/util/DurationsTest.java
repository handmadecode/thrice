/*
 * Copyright 2025 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * JUnit tests for the utility methods in {@link Durations}.
 */
public class DurationsTest
{
    // Fixtures with single digit scalar and one unit
    static private final ValidFixture[] SINGLE_DIGIT_FIXTURES = {
        new ValidFixture("1d", 86400000000000L),         // 1 day
        new ValidFixture("1h", 3600000000000L),          // 1 hour
        new ValidFixture("1m", 60000000000L),            // 1 minute
        new ValidFixture("1s", 1000000000L),             // 1 second
        new ValidFixture("1ms", 1000000L),               // 1 millisecond
        new ValidFixture("1us", 1000L),                  // 1 microsecond
        new ValidFixture("1ns", 1L),                     // 1 nanosecond
        new ValidFixture("1u", 1000L) ,                  // 1 microsecond
        new ValidFixture("1n", 1L),                      // 1 nanosecond
        new ValidFixture("0d", 0L),                      // 0 days
        new ValidFixture("0h", 0L),                      // 0 hours
        new ValidFixture("0m", 0L),                      // 0 minutes
        new ValidFixture("0s", 0L),                      // 0 seconds
        new ValidFixture("0ms", 0L),                     // 0 milliseconds
        new ValidFixture("0us", 0L),                     // 0 microseconds
        new ValidFixture("0ns", 0L),                     // 0 nanoseconds
        new ValidFixture("0u", 0L),                      // 0 microseconds
        new ValidFixture("0n", 0L)                       // 0 nanoseconds
    };

    // Fixtures with multiple digits scalar and one unit
    static private final ValidFixture[] MULTIPLE_DIGITS_FIXTURES = {
        new ValidFixture("365d", 31536000000000000L),    // 1 year in days
        new ValidFixture("24h", 86400000000000L),        // 1 day in hours
        new ValidFixture("60m", 3600000000000L),         // 1 hour in minutes
        new ValidFixture("3600s", 3600000000000L),       // 1 hour in seconds
        new ValidFixture("1000ms", 1000000000L),         // 1 second in milliseconds
        new ValidFixture("1000000us", 1000000000L),      // 1 second in microseconds
        new ValidFixture("1000000000ns", 1000000000L),   // 1 second in nanoseconds
        new ValidFixture("1000000u", 1000000000L),       // 1 second in microseconds
        new ValidFixture("1000000000n", 1000000000L),    // 1 second in nanoseconds
        new ValidFixture("999d", 86313600000000000L),    // 999 days
        new ValidFixture("100h", 360000000000000L),      // 100 hours
        new ValidFixture("500m", 30000000000000L),       // 500 minutes
        new ValidFixture("1000s", 1000000000000L),       // 1000 seconds
        new ValidFixture("500ms", 500000000L),           // 500 milliseconds
        new ValidFixture("2000us", 2000000L),            // 2000 microseconds
        new ValidFixture("174711ns", 174711L),           // 174711 nanoseconds
        new ValidFixture("999999998u", 999999998000L),   // Large microseconds
        new ValidFixture("999999999n", 999999999L)       // Large nanoseconds
    };

    // Fixtures with two units
    static private final ValidFixture[] TWO_UNITS_FIXTURES = {
        new ValidFixture("1d1h", 90000000000000L),       // 1 day + 1 hour
        new ValidFixture("1d1m", 86460000000000L),       // 1 day + 1 minute
        new ValidFixture("1d1s", 86401000000000L),       // 1 day + 1 second
        new ValidFixture("1d1ms", 86400001000000L),      // 1 day + 1 millisecond
        new ValidFixture("1d1us", 86400000001000L),      // 1 day + 1 microsecond
        new ValidFixture("1d1ns", 86400000000001L),      // 1 day + 1 nanosecond
        new ValidFixture("1h1m", 3660000000000L),        // 1 hour + 1 minute
        new ValidFixture("1h1s", 3601000000000L),        // 1 hour + 1 second
        new ValidFixture("1h1ms", 3600001000000L),       // 1 hour + 1 millisecond
        new ValidFixture("1h1us", 3600000001000L),       // 1 hour + 1 microsecond
        new ValidFixture("1h1ns", 3600000000001L),       // 1 hour + 1 nanosecond
        new ValidFixture("1m1s", 61000000000L),          // 1 minute + 1 second
        new ValidFixture("1m1ms", 60001000000L),         // 1 minute + 1 millisecond
        new ValidFixture("1m1us", 60000001000L),         // 1 minute + 1 microsecond
        new ValidFixture("1m1ns", 60000000001L),         // 1 minute + 1 nanosecond
        new ValidFixture("1s1ms", 1001000000L),          // 1 second + 1 millisecond
        new ValidFixture("1s1us", 1000001000L),          // 1 second + 1 microsecond
        new ValidFixture("1s1ns", 1000000001L),          // 1 second + 1 nanosecond
        new ValidFixture("1ms1us", 1001000L),            // 1 millisecond + 1 microsecond
        new ValidFixture("1ms1ns", 1000001L),            // 1 millisecond + 1 nanosecond
        new ValidFixture("1us1ns", 1001L)                // 1 microsecond + 1 nanosecond
    };

    // Fixtures with multiple units
    static private final ValidFixture[] MULTIPLE_UNITS_FIXTURES = {
        new ValidFixture("1d2h3m", 93780000000000L),     // Missing s, ms, us, ns
        new ValidFixture("1d2h", 93600000000000L),       // Missing m, s, ms, us, ns
        new ValidFixture("2h3m4s", 7384000000000L),      // Missing d, ms, us, ns
        new ValidFixture("3m4s5ms", 184005000000L),      // Missing d, h, us, ns
        new ValidFixture("4s5ms6us", 4005006000L),       // Missing d, h, m, ns
        new ValidFixture("5ms6us7ns", 5006007L),         // Missing d, h, m, s
        new ValidFixture("100d50h", 8820000000000000L),  // Days and hours only
        new ValidFixture("30m15s", 1815000000000L),      // Minutes and seconds only
        new ValidFixture("500ms250us", 500250000L),      // Milliseconds and microseconds only
        new ValidFixture("1000us500ns", 1000500L),       // Microseconds and nanoseconds only
        new ValidFixture("1s500ms", 1500000000L),        // Seconds and milliseconds only
        new ValidFixture("1m30s250ms", 90250000000L),    // Minutes, seconds, and milliseconds,
        new ValidFixture("2d3h4m5s6ms7us8ns", 183845006007008L), // All units
        new ValidFixture("1d2h3m4s5ms6us7ns", 93784005006007L),  // All units different values
        new ValidFixture("7d0h0m0s0ms0us0ns", 604800000000000L), // 7 days with zeros
        new ValidFixture("0d24h0m0s0ms0us0ns", 86400000000000L), // 0 days + 24 hours
        new ValidFixture("0d0h0m0s0ms0u0n", 0L),                 // All zeroes
        new ValidFixture("10d10h10m10s10ms10us10ns", 900610010010010L) // All 10s
    };

    // Fixtures with whitespace
    static private final ValidFixture[] WHITESPACE_FIXTURES = {
        new ValidFixture(" 1d2h3m4s5ms6u7n", 93784005006007L),       // Leading whitespace
        new ValidFixture("1d2h3m4s5ms6u7n ", 93784005006007L),       // Trailing whitespace
        new ValidFixture(" 1d2h3m4s5ms6u7n ", 93784005006007L),      // Leading and trailing whitespace
        new ValidFixture("1d 2h 3m 4s 5ms 6u 7n", 93784005006007L),  // Whitespace between units
        new ValidFixture("1 d2 h3 m4 s5 ms6 u7n", 93784005006007L),  // Whitespace between scalars
        new ValidFixture("1d\t2h\t \t3m\t\t\t4s    5ms \n 6u\r7n", 93784005006007L),  // Whitespace between units
        new ValidFixture("1 d 2 h 3 m 4 s 5 ms 6 u 7 n", 93784005006007L)  // Whitespace between scalar and unit
    };

    // Fixtures with maximum allowed values for single and multiple units
    static private final ValidFixture[] MAXIMUM_VALUE_FIXTURES = {
        // Maximum days
        new ValidFixture("106751d", 9223286400000000000L),
        // Maximum hours
        new ValidFixture("2562047h", 9223369200000000000L),
        // Maximum minutes
        new ValidFixture("153722867m", 9223372020000000000L),
        // Maximum seconds
        new ValidFixture("9223372036s", 9223372036000000000L),
        // Maximum milliseconds
        new ValidFixture("9223372036854ms", 9223372036854000000L),
        // Maximum microseconds
        new ValidFixture("9223372036854775us", 9223372036854775000L),
        // Maximum nanoseconds
        new ValidFixture("9223372036854775807ns", 9223372036854775807L),
        // Maximum value, all units
        new ValidFixture("106751 d 23 h 47 m 16 s 854 ms 775 us 807 ns", Long.MAX_VALUE),
        // Maximum value, all units, reverse order
        new ValidFixture("807 ns 775 us 854 ms 16 s 47 m 23 h 106751 d", Long.MAX_VALUE),
        // Maximum value, no day unit
        new ValidFixture("2562047 h 47 m 16 s 854 ms 775 us 807 ns", Long.MAX_VALUE),
        // Maximum value, no hour unit
        new ValidFixture("153722867 m 16 s 854 ms 775 us 807 ns", Long.MAX_VALUE),
        // Maximum value, no minute unit
        new ValidFixture("9223372036 s 854 ms 775 us 807 ns", Long.MAX_VALUE),
        // Maximum value, only seconds and nanoseconds
        new ValidFixture("9223372036 s 854775807 n", Long.MAX_VALUE)
    };

    // Fixtures with the units in different order
    static private final ValidFixture[] UNITS_IN_DIFFERENT_ORDER_FIXTURES = {
        new ValidFixture("12d 2h 20m 0s 55ms 666u 747ns", 1045200055666747L),
        new ValidFixture("747n 666us 55ms 0s 20m 2h  12d", 1045200055666747L),
        new ValidFixture("20m 747ns 55ms 12d 666u 2h", 1045200055666747L),
        new ValidFixture("0s 20m 2h 12d 55ms 747ns 666u", 1045200055666747L),
        new ValidFixture("666us 2h 20m 12d 747ns 55ms", 1045200055666747L)
    };

    // Fixtures with missing scalars or units
    static private final InvalidFixture[] MISSING_TOKEN_FIXTURES = {
        new InvalidFixture("1", 0),         // Missing unit
        new InvalidFixture("s", 0),         // Missing scalar
        new InvalidFixture("1h 34", 4),     // Missing unit
        new InvalidFixture("7 3s", 2),      // Missing unit
        new InvalidFixture("6s m", 3),      // Missing scalar
        new InvalidFixture("d 4h", 0)       // Missing scalar
    };

    // Fixtures with invalid characters
    static private final InvalidFixture[] INVALID_CHAR_FIXTURES = {
        new InvalidFixture("1x", 1),         // Invalid unit 'x' at position 1
        new InvalidFixture("1day", 2),       // Should be 'd', not 'day' - fail at 'a'
        new InvalidFixture("1hour", 2),      // Should be 'h', not 'hour' - fail at 'o'
        new InvalidFixture("1min", 2),       // Should be 'm', not 'min' - fail at 'i'
        new InvalidFixture("1sec", 2),       // Should be 's', not 'sec' - fail at 'e'
        new InvalidFixture("1milli", 2),     // Should be 'ms', not 'milli' - fail at 'i'
        new InvalidFixture("1micro", 2),     // Should be 'us', not 'micro' - fail at 'i'
        new InvalidFixture("1nano", 2),      // Should be 'ns', not 'nano' - fail at 'a'
        new InvalidFixture("1D", 1),         // Uppercase 'D' - fail at position 1
        new InvalidFixture("1H", 1),         // Uppercase 'H' - fail at position 1
        new InvalidFixture("1M", 1),         // Uppercase 'M' - fail at position 1
        new InvalidFixture("1S", 1),         // Uppercase 'S' - fail at position 1
        new InvalidFixture("1MS", 1),        // Uppercase 'MS' - fail at position 1
        new InvalidFixture("1US", 1),        // Uppercase 'US' - fail at position 1
        new InvalidFixture("1NS", 1),        // Uppercase 'NS' - fail at position 1
        new InvalidFixture("1d2", 2),        // Number without unit - fail at end
        new InvalidFixture("abc", 0),        // Non-numeric - fail at 'a'
        new InvalidFixture("1.5d", 1),       // Decimal numbers - fail at '.'
        new InvalidFixture("-1d", 0),        // Negative numbers - fail at '-'
        new InvalidFixture("+1d", 0),        // Explicit positive sign - fail at '+'
        new InvalidFixture("1d-2h", 2),      // Negative in combination - fail at '-'
        new InvalidFixture("1sm", 2),        // Wrong order within 'ms' - fail at 'm'
        new InvalidFixture("1su", 2),        // Wrong order within 'us' - fail at 'u'
        new InvalidFixture("1md", 2),        // Invalid second unit char - fail at 'd'
        new InvalidFixture("1ux", 2),        // Invalid second unit char - fail at 'x'
        new InvalidFixture("1nh", 2),        // Invalid second unit char - fail at 'h'
        new InvalidFixture("1ds", 2)         // Invalid second unit char - fail at 's'
    };

    // Fixtures with duplicate units
    static private final InvalidFixture[] DUPLICATE_UNIT_FIXTURES = {
        new InvalidFixture("1d2d", 3),       // Two day specs - fail at second 'd'
        new InvalidFixture("1h2h", 3),       // Two hour specs - fail at second 'h'
        new InvalidFixture("1m2m", 3),       // Two minute specs - fail at second 'm'
        new InvalidFixture("1s2s", 3),       // Two second specs - fail at second 's'
        new InvalidFixture("1ms2ms", 5),     // Two millisecond specs - fail at second 'ms'
        new InvalidFixture("1u2us5n", 4),    // Two microsecond specs - fail at second 'us'
        new InvalidFixture("1ns2ns", 5),     // Two nanosecond specs - fail at second 'ns'
        new InvalidFixture("11d2h1d5m", 6),  // Days repeated - fail at second 'd'
        new InvalidFixture("1h1m1h", 5),     // Hours repeated in middle - fail at second 'h'
        new InvalidFixture("1d1d1h1s1s1ms1us1ns", 3), // Multiple duplicates - fail at first duplicate
        new InvalidFixture("1s1ms1s", 6),    // Seconds repeated after milliseconds - fail at second 's'
        new InvalidFixture("1ms1us1ms", 8),  // Milliseconds repeated after microseconds - fail at second 'ms'
        new InvalidFixture("1u2ms3s4us", 9), // Microseconds with and without 's'
        new InvalidFixture("17h1ns2n", 7)    // Nanoseconds with and without 's'
    };

    // Fixtures with overflow in a single unit
    static private final InvalidFixture[] SINGLE_UNIT_OVERFLOW_FIXTURES = {
        new InvalidFixture("106752d", 6),                // Too large days scalar
        new InvalidFixture("2562048h", 7),               // Too large hours scalar
        new InvalidFixture("153722868m", 9),            // Too large minutes scalar (fail at end)
        new InvalidFixture("9223372037s", 10),           // Too large seconds scalar
        new InvalidFixture("9223372036855ms", 14),       // Too large milliseconds scalar
        new InvalidFixture("9223372036854776us", 17),    // Too large microseconds scalar
        new InvalidFixture("9223372036854775808ns", 18), // Too large nanoseconds scalar
        new InvalidFixture("9223372036854775807d", 19),  // Long.MAX_VALUE days
        new InvalidFixture("9223372036854775807h", 19),  // Long.MAX_VALUE hours
        new InvalidFixture("9223372036854775807m", 19),  // Long.MAX_VALUE minutes (fail at end)
        new InvalidFixture("9223372036854775807s", 19),  // Long.MAX_VALUE seconds
        new InvalidFixture("9223372036854775807ms", 20), // Long.MAX_VALUE milliseconds
        new InvalidFixture("9223372036854775807u", 19),  // Long.MAX_VALUE microseconds
        new InvalidFixture("18446744073709551615ns", 19) // 2 * Long.MAX_VALUE nanoseconds
    };

    // Fixtures with overflow in the total number of nanoseconds
    static private final InvalidFixture[] TOTAL_NANOS_OVERFLOW_FIXTURES = {
        new InvalidFixture("106751d24h", 9),                        // 1 hour over max
        new InvalidFixture("106751d23h48m", 12),                    // 1 minute over max
        new InvalidFixture("106751d23h47m17s", 15),                 // 1 second over max
        new InvalidFixture("106751d23h47m16s855ms", 20),            // 1 millisecond over max
        new InvalidFixture("106751d23h47m16s854ms776u", 24),        // 1 microsecond over max
        new InvalidFixture("106751d23h47m16s854ms775u808n", 28),    // 1 nanosecond over max
        new InvalidFixture("153722868m1u", 10),                     // Minute overflow (detected at next scalar)
        new InvalidFixture("1h153722867m16s", 12),                  // Hour + minute overflow (detected at next scalar)
        new InvalidFixture("105000d50000h", 12),                    // Days and hours overflow together
        new InvalidFixture("2562047h60m", 10),                      // Hours and minutes overflow together
        new InvalidFixture("9223372036s1000ms", 16),                // Seconds and milliseconds overflow together
        new InvalidFixture("9223372036s1000100us", 19),             // Seconds and microseconds overflow together
        new InvalidFixture("9223372036s1000000000ns", 22),          // Max seconds + 1 second in ns
        new InvalidFixture("106751 d 23 h 47 m 16 s 854 ms 775 us 808 ns", 43)
    };


    @Test
    void durationToNanosThrowsForNullDuration()
    {
        assertAll(
            () -> assertThrows(
                NullPointerException.class,
                () -> Durations.durationToNanos(null)),
            () -> assertThrows(
                NullPointerException.class,
                () -> Durations.durationToNanos(null, 0, 1))
        );
    }


    @Test
    void durationToNanosThrowsForInvalidIndices()
    {
        assertAll(
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> Durations.durationToNanos("2d", -1, 2)),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> Durations.durationToNanos(" 2d", 1, 3)),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> Durations.durationToNanos("2d", 14, 1))
        );
    }


    @Test
    void durationToNanosReturnsZeroForEmptyDuration()
    {
        assertAll(
            () -> assertEquals(0, Durations.durationToNanos("")),
            () -> assertEquals(0, Durations.durationToNanos(" ")),
            () -> assertEquals(0, Durations.durationToNanos("\t \n \r   "))
        );
    }


    @ParameterizedTest
    @FieldSource("SINGLE_DIGIT_FIXTURES")
    void durationToNanosParsesSingleDigitSingleUnit(ValidFixture pFixture) throws ParseException
    {
        assertValidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("MULTIPLE_DIGITS_FIXTURES")
    void durationToNanosParsesMultipleDigitsSingleUnit(ValidFixture pFixture) throws ParseException
    {
        assertValidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("TWO_UNITS_FIXTURES")
    void durationToNanosParsesTwoUnits(ValidFixture pFixture) throws ParseException
    {
        assertValidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("MULTIPLE_UNITS_FIXTURES")
    void durationToNanosParsesMultipleUnits(ValidFixture pFixture) throws ParseException
    {
        assertValidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("WHITESPACE_FIXTURES")
    void durationToNanosParsesWhitespace(ValidFixture pFixture) throws ParseException
    {
        assertValidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("MAXIMUM_VALUE_FIXTURES")
    void durationToNanosParsesMaxValues(ValidFixture pFixture) throws ParseException
    {
        assertValidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("UNITS_IN_DIFFERENT_ORDER_FIXTURES")
    void durationToNanosParseUnitsInDifferentOrder(ValidFixture pFixture) throws ParseException
    {
        assertValidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("MISSING_TOKEN_FIXTURES")
    void durationToNanosThrowsOnMissingToken(InvalidFixture pFixture)
    {
        assertInvalidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("INVALID_CHAR_FIXTURES")
    void durationToNanosThrowsOnInvalidChar(InvalidFixture pFixture)
    {
        assertInvalidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("DUPLICATE_UNIT_FIXTURES")
    void durationToNanosThrowsOnDuplicateUnit(InvalidFixture pFixture)
    {
        assertInvalidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("SINGLE_UNIT_OVERFLOW_FIXTURES")
    void durationToNanosThrowsOnSingleUnitOverflow(InvalidFixture pFixture)
    {
        assertInvalidFixture(pFixture);
    }


    @ParameterizedTest
    @FieldSource("TOTAL_NANOS_OVERFLOW_FIXTURES")
    void durationToNanosThrowsOnTotalNanosOverflow(InvalidFixture pFixture)
    {
        assertInvalidFixture(pFixture);
    }


    static private void assertValidFixture(ValidFixture pFixture) throws ParseException
    {
        long aNanos = Durations.durationToNanos(pFixture.fDuration);
        assertEquals(pFixture.fNanos, aNanos);
    }


    static private void assertInvalidFixture(InvalidFixture pFixture)
    {
        try
        {
            Durations.durationToNanos(pFixture.fDuration);
            fail(pFixture.fDuration + " does not throw ParseException");
        }
        catch (ParseException pe)
        {
            assertEquals(pFixture.fErrorOffset, pe.getErrorOffset(), pFixture.fDuration);
        }
    }


    static private class ValidFixture
    {
        final String fDuration;
        final long fNanos;

        ValidFixture(String pDuration, long pNanos)
        {
            fDuration = pDuration;
            fNanos = pNanos;
        }
    }

    static private class InvalidFixture
    {
        final String fDuration;
        final int fErrorOffset;

        InvalidFixture(String pDuration, int pErrorOffset)
        {
            fDuration = pDuration;
            fErrorOffset = pErrorOffset;
        }
    }
}
