/*
 * Copyright 2024-2025 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit tests for the utility methods in {@code Byte}.
 */
public class BytesTest
{
    static private final ShortFixture[] SHORT_FIXTURES = {

        // 0, 1, -1
        new ShortFixture(
            (short)0,
            (byte)0, (byte)0),
        new ShortFixture(
            (short)1,
            (byte)0, (byte)1),
        new ShortFixture(
            (short)-1,
            (byte)0xFF, (byte)0xFF),

        // Max and min values
        new ShortFixture(
            Short.MAX_VALUE,
            (byte)0x7F, (byte)0xFF),
        new ShortFixture(
            Short.MIN_VALUE,
            (byte)0x80, (byte)0x00),

        // Only the most significant byte set
        new ShortFixture(
            (short)0x0100,
            (byte)0x01, (byte)0x00),

        // Only the least significant byte set
        new ShortFixture(
            (short)0x0001,
            (byte)0x00, (byte)0x01),

        // byte -1 (0xFF) should contribute 0xFF, not 0xFFFF
        new ShortFixture(
            (short)0x00FF,
            (byte)0x00, (byte)-1),
        new ShortFixture(
            (short)0xFF00,
            (byte)-1, (byte)0x00),

        // Alternating bits - 0xAAAAAAAA and 0x55555555
        new ShortFixture(
            (short)0xAAAA,
            (byte)0xAA, (byte)0xAA),
        new ShortFixture(
            (short)0x5555,
            (byte)0x55, (byte)0x55),

        // Powers of 2
        new ShortFixture(
            (short)256,
            (byte)0x01, (byte)0x00),
        new ShortFixture(
            (short)16384,
            (byte)0x40, (byte)0x00),

        // Mixed boundary values
        new ShortFixture(
            (short)0x7F01,
            (byte)0x7F, (byte)0x01),
        new ShortFixture(
            (short)0x80FF,
            (byte)0x80, (byte)0xFF),
        new ShortFixture(
            (short)0x0123,
            (byte)0x01, (byte)0x23),
        new ShortFixture(
            (short)0xB62E,
            (byte)0xB6, (byte)0x2E)
    };

    static private final IntFixture[] INT_FIXTURES = {

        // 0, 1, -1
        new IntFixture(
            0,
            (byte)0, (byte)0, (byte)0, (byte)0),
        new IntFixture(
            1,
            (byte)0, (byte)0, (byte)0, (byte)1),
        new IntFixture(
            -1,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF),

        // Max and min values
        new IntFixture(
            Integer.MAX_VALUE,
            (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF),
        new IntFixture(
            Integer.MIN_VALUE,
            (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00),
        new IntFixture(
            Short.MAX_VALUE,
            (byte)0x00, (byte)0x00, (byte)0x7F, (byte)0xFF),
        new IntFixture(
            Short.MIN_VALUE,
            (byte)0xFF, (byte)0xFF, (byte)0x80, (byte)0x00),

        // Only the most significant byte set
        new IntFixture(
            0x01000000,
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00),

        // Only the least significant byte set
        new IntFixture(
            0x00000001,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01),

        // byte -1 (0xFF) should contribute 0xFF, not 0xFFFFFFFF
        new IntFixture(
            0x000000FF,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)-1),
        new IntFixture(
            0x00FFFF00,
        (byte)0x00, (byte)-1, (byte)-1, (byte)0x00),
        new IntFixture(
            0xFF000000,
            (byte)-1, (byte)0x00, (byte)0x00, (byte)0x00),

        // Alternating bits - 0xAAAAAAAA and 0x55555555
        new IntFixture(
            0xAAAAAAAA,
            (byte)0xAA, (byte)0xAA, (byte)0xAA, (byte)0xAA),
        new IntFixture(
            0x55555555,
            (byte)0x55, (byte)0x55, (byte)0x55, (byte)0x55),

        // Powers of 2
        new IntFixture(
            256,
            (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00),
        new IntFixture(
            65536,
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00),
        new IntFixture(
            16777216,
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00),

        // Mixed boundary values
        new IntFixture(
            0x7F00FF01,
            (byte)0x7F, (byte)0x00, (byte)0xFF, (byte)0x01),
        new IntFixture(
            0x80FF00FF,
            (byte)0x80, (byte)0xFF, (byte)0x00, (byte)0xFF),
        new IntFixture(
            0x01234567,
            (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67),
        new IntFixture(
            0xB669FD2E,
            (byte)0xB6, (byte)0x69, (byte)0xFD, (byte)0x2E)
    };

    static private final LongFixture[] LONG_FIXTURES = {

        // 0, 1, -1
        new LongFixture(
            0L,
            (byte)0, (byte)0, (byte)0, (byte)0,
            (byte)0, (byte)0, (byte)0, (byte)0),
        new LongFixture(
            1L,
            (byte)0, (byte)0, (byte)0, (byte)0,
            (byte)0, (byte)0, (byte)0, (byte)1),
        new LongFixture(
            -1L,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF),

        // Max and min values
        new LongFixture(
            Long.MAX_VALUE,
            (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF),
        new LongFixture(
            Long.MIN_VALUE,
            (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00),
        new LongFixture(
            Integer.MAX_VALUE,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF),
        new LongFixture(
            Integer.MIN_VALUE,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00),
        new LongFixture(
            Short.MAX_VALUE,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x7F, (byte)0xFF),
        new LongFixture(
            Short.MIN_VALUE,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0x80, (byte)0x00),

        // Only the most significant byte set
        new LongFixture(
            0x0100000000000000L,
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00),

        // Only the least significant byte set
        new LongFixture(
            0x0000000000000001L,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01),

        // byte -1 (0xFF) should contribute 0xFF, not 0xFFFFFFFFFFFFFFFF
        new LongFixture(
            0x00000000000000FFL,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)-1),
        new LongFixture(
            0x00FFFFFFFFFFFF00L,
            (byte)0x00, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x00),
        new LongFixture(
            0xFF00000000000000L,
            (byte)-1, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00),

        // Alternating bits - 0xAAAAAAAAAAAAAAAA and 0x5555555555555555
        new LongFixture(
            0xAAAAAAAAAAAAAAAAL,
            (byte)0xAA, (byte)0xAA, (byte)0xAA, (byte)0xAA,
            (byte)0xAA, (byte)0xAA, (byte)0xAA, (byte)0xAA),
        new LongFixture(
            0x5555555555555555L,
            (byte)0x55, (byte)0x55, (byte)0x55, (byte)0x55,
            (byte)0x55, (byte)0x55, (byte)0x55, (byte)0x55),

        // Powers of 2
        new LongFixture(
            256L,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00),
        new LongFixture(
            16777216L,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00),
        new LongFixture(
            281474976710656L,
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00),

        // Mixed boundary values
        new LongFixture(
            0x7F00FF00800001FFL,
            (byte)0x7F, (byte)0x00, (byte)0xFF, (byte)0x00,
            (byte)0x80, (byte)0x00, (byte)0x01, (byte)0xFF),
        new LongFixture(
            0x80FF00FFFF00FF80L,
            (byte)0x80, (byte)0xFF, (byte)0x00, (byte)0xFF,
            (byte)0xFF, (byte)0x00, (byte)0xFF, (byte)0x80),
        new LongFixture(
            0x0123456789ABCDEFL,
            (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67,
            (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF),
        new LongFixture(
            0xEEDDABB7C2E85C7BL,
            (byte)0xEE, (byte)0xDD, (byte)0xAB, (byte)0xB7,
            (byte)0xC2, (byte)0xE8, (byte)0x5C, (byte)0x7B)
    };


    @ParameterizedTest
    @FieldSource("SHORT_FIXTURES")
    void testShortFixtures(ShortFixture pFixture)
    {
        assertAll(
            () -> assertEquals(
                pFixture.fShort,
                Bytes.toShort(pFixture.fByte1, pFixture.fByte0)),
            () -> assertEquals(pFixture.fByte1, Bytes.shortByte1(pFixture.fShort)),
            () -> assertEquals(pFixture.fByte0, Bytes.shortByte0(pFixture.fShort))
        );
    }


    @Test
    public void testShortRoundTrip()
    {
        short aValue = Short.MIN_VALUE;
        do
        {
            assertEquals(aValue, Bytes.toShort(Bytes.shortByte1(aValue), Bytes.shortByte0(aValue)));
            aValue++;
        }
        while (aValue != Short.MIN_VALUE);
    }


    @ParameterizedTest
    @FieldSource("INT_FIXTURES")
    void testIntFixtures(IntFixture pFixture)
    {
        assertAll(
            () -> assertEquals(
                pFixture.fInt,
                Bytes.toInt(
                    pFixture.fBytes[3],
                    pFixture.fBytes[2],
                    pFixture.fBytes[1],
                    pFixture.fBytes[0])),
            () -> assertEquals(pFixture.fBytes[3], Bytes.intByte3(pFixture.fInt)),
            () -> assertEquals(pFixture.fBytes[2], Bytes.intByte2(pFixture.fInt)),
            () -> assertEquals(pFixture.fBytes[1], Bytes.intByte1(pFixture.fInt)),
            () -> assertEquals(pFixture.fBytes[0], Bytes.intByte0(pFixture.fInt))
        );
    }


    @Test
    public void testIntRoundTrip()
    {
        for (int i=0; i<1000; i++)
        {
            int aValue = ThreadLocalRandom.current().nextInt();
            assertEquals(
                aValue,
                Bytes.toInt(
                    Bytes.intByte3(aValue),
                    Bytes.intByte2(aValue),
                    Bytes.intByte1(aValue),
                    Bytes.intByte0(aValue)));
        }
    }


    @ParameterizedTest
    @FieldSource("LONG_FIXTURES")
    void testLongFixtures(LongFixture pFixture)
    {
        assertAll(
            () -> assertEquals(
                pFixture.fLong,
                Bytes.toLong(
                    pFixture.fBytes[7],
                    pFixture.fBytes[6],
                    pFixture.fBytes[5],
                    pFixture.fBytes[4],
                    pFixture.fBytes[3],
                    pFixture.fBytes[2],
                    pFixture.fBytes[1],
                    pFixture.fBytes[0])),
            () -> assertEquals(pFixture.fBytes[7], Bytes.longByte7(pFixture.fLong)),
            () -> assertEquals(pFixture.fBytes[6], Bytes.longByte6(pFixture.fLong)),
            () -> assertEquals(pFixture.fBytes[5], Bytes.longByte5(pFixture.fLong)),
            () -> assertEquals(pFixture.fBytes[4], Bytes.longByte4(pFixture.fLong)),
            () -> assertEquals(pFixture.fBytes[3], Bytes.longByte3(pFixture.fLong)),
            () -> assertEquals(pFixture.fBytes[2], Bytes.longByte2(pFixture.fLong)),
            () -> assertEquals(pFixture.fBytes[1], Bytes.longByte1(pFixture.fLong)),
            () -> assertEquals(pFixture.fBytes[0], Bytes.longByte0(pFixture.fLong))
        );
    }


    @Test
    public void testLongRoundTrip()
    {
        for (int i=0; i<1000; i++)
        {
            long aValue = ThreadLocalRandom.current().nextLong();
            assertEquals(
                aValue,
                Bytes.toLong(
                    Bytes.longByte7(aValue),
                    Bytes.longByte6(aValue),
                    Bytes.longByte5(aValue),
                    Bytes.longByte4(aValue),
                    Bytes.longByte3(aValue),
                    Bytes.longByte2(aValue),
                    Bytes.longByte1(aValue),
                    Bytes.longByte0(aValue)));
        }
    }


    static private class ShortFixture
    {
        final short fShort;
        final byte fByte1;
        final byte fByte0;

        ShortFixture(short pShort, byte pByte1, byte pByte0)
        {
            fShort = pShort;
            fByte1 = pByte1;
            fByte0 = pByte0;
        }
    }


    static private class IntFixture
    {
        final int fInt;
        final byte[] fBytes = new byte[4];

        IntFixture(int pInt, byte pByte3, byte pByte2, byte pByte1, byte pByte0)
        {
            fInt = pInt;
            fBytes[3] = pByte3;
            fBytes[2] = pByte2;
            fBytes[1] = pByte1;
            fBytes[0] = pByte0;
        }
    }


    static private class LongFixture
    {
        final long fLong;
        final byte[] fBytes = new byte[8];

        LongFixture(
            long pLong,
            byte pByte7, byte pByte6, byte pByte5, byte pByte4,
            byte pByte3, byte pByte2, byte pByte1, byte pByte0)
        {
            fLong = pLong;
            fBytes[7] = pByte7;
            fBytes[6] = pByte6;
            fBytes[5] = pByte5;
            fBytes[4] = pByte4;
            fBytes[3] = pByte3;
            fBytes[2] = pByte2;
            fBytes[1] = pByte1;
            fBytes[0] = pByte0;
        }
    }
}
