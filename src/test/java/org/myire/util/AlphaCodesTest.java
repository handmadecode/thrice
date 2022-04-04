/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for {@link org.myire.util.AlphaCodes}.
 */
public class AlphaCodesTest
{
    static private final String[] ISO4217_CODES = {"SEK", "EUR", "NOK", "DKK", "ISK", "USD", "GBP"};
    static private final String[] ISO3166_ALPHA2_CODES = {"SE", "FI", "NO", "DK", "IS", "US", "GB"};
    static private final String[] ISO3166_ALPHA3_CODES = {"SWE", "FIN", "NOR", "DNK", "ISL", "USA", "GBR"};
    static private final String[] ISO639_ALPHA2_CODES = {"sv", "fi", "no", "da", "is", "en", "fr"};
    static private final String[] FOUR_LETTER_CODES = {"SORA", "WHIM", "SOSP", "BEKI", "BWHA", "YBCU"};


    @Test
    public void encodeAlpha2ThrowsForTooShortCharSequence()
    {
        assertAll(
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha2("")
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha2("x")
            )
        );
    }


    @Test
    public void encodeAlpha2AtOffsetThrowsForTooShortCharSequence()
    {
        assertAll(
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha2("QW", 1)
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha2("QW", 2)
            )
        );
    }


    @Test
    public void encodeAlpha3ThrowsForTooShortCharSequence()
    {
        assertAll(
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha3("")
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha3("x")
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha3("xy")
            )
        );
    }


    @Test
    public void encodeAlpha3AtOffsetThrowsForTooShortCharSequence()
    {
        assertAll(
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha3("XYZ", 1)
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha3("XYZ", 2)
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha3("XYZ", 3)
            )
        );
    }


    @Test
    public void encodeAlpha4ThrowsForTooShortCharSequence()
    {
        assertAll(
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("")
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("x")
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("xy")
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("xyz")
            )
        );
    }


    @Test
    public void encodeAlpha4AtOffsetThrowsForTooShortCharSequence()
    {
        assertAll(
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("ABCD", 1)
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("ABCD", 2)
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("ABCD", 3)
            ),
            () -> assertThrows(
                IndexOutOfBoundsException.class,
                () -> AlphaCodes.encodeAlpha4("ABCD", 4)
            )
        );
    }


    @Test
    public void decodeAlpha2DecodesValueFromEncodeAlpha2()
    {
        int aCodeLength = 2;
        for (int i=0; i<100; i++)
        {
            // Given
            String aAlpha2Code = randomAlphaCode(aCodeLength);
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(aCodeLength);

            // When
            int aEncoded = AlphaCodes.encodeAlpha2(aAlpha2Code);
            AlphaCodes.decodeAlpha2(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aAlpha2Code, aDecodeBuffer.toString());

            // When
            char[] aDecodedChars = new char[aCodeLength];
            AlphaCodes.decodeAlpha2(aEncoded, aDecodedChars, 0);

            // Then
            assertEquals(aAlpha2Code, new String(aDecodedChars));
        }
    }


    @Test
    public void decodeAlpha2DecodesValueFromEncodeAlpha2AtOffset()
    {
        int aCodeLength = 2;
        for (int i=0; i<100; i++)
        {
            // Given
            int aOffset = ThreadLocalRandom.current().nextInt(1, 32);
            String aOffsetAlpha2Code = randomAlphaCode(aCodeLength, aOffset);
            String aExpectedAlpha2Code = aOffsetAlpha2Code.substring(aOffset);

            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(aCodeLength);

            // When
            int aEncoded = AlphaCodes.encodeAlpha2(aOffsetAlpha2Code, aOffset);
            AlphaCodes.decodeAlpha2(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(
                aExpectedAlpha2Code,
                aDecodeBuffer.toString(),
                "Decode failed: " + aOffsetAlpha2Code);

            // When
            char[] aChars = new char[aCodeLength + aOffset];
            AlphaCodes.decodeAlpha2(aEncoded, aChars, aOffset);

            // Then
            assertEquals(
                aExpectedAlpha2Code,
                new String(aChars, aOffset, aCodeLength),
                "Decode failed: " + aOffsetAlpha2Code);
        }
    }


    @Test
    public void decodeAlpha3DecodesValueFromEncodeAlpha3()
    {
        int aCodeLength = 3;
        for (int i=0; i<100; i++)
        {
            // Given
            String aAlpha3Code = randomAlphaCode(aCodeLength);
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(aCodeLength);

            // When
            int aEncoded = AlphaCodes.encodeAlpha3(aAlpha3Code);
            AlphaCodes.decodeAlpha3(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aAlpha3Code, aDecodeBuffer.toString());

            // When
            char[] aDecodedChars = new char[aCodeLength];
            AlphaCodes.decodeAlpha3(aEncoded, aDecodedChars, 0);

            // Then
            assertEquals(aAlpha3Code, new String(aDecodedChars));
        }
    }


    @Test
    public void decodeAlpha3DecodesValueFromEncodeAlpha3AtOffset()
    {
        int aCodeLength = 3;
        for (int i=0; i<100; i++)
        {
            // Given
            int aOffset = ThreadLocalRandom.current().nextInt(1, 32);
            String aOffsetAlpha3Code = randomAlphaCode(aCodeLength, aOffset);
            String aExpectedAlpha3Code = aOffsetAlpha3Code.substring(aOffset);

            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(aCodeLength);

            // When
            int aEncoded = AlphaCodes.encodeAlpha3(aOffsetAlpha3Code, aOffset);
            AlphaCodes.decodeAlpha3(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(
                aExpectedAlpha3Code,
                aDecodeBuffer.toString(),
                "Decode failed: " + aOffsetAlpha3Code);

            // When
            char[] aChars = new char[aCodeLength + aOffset];
            AlphaCodes.decodeAlpha3(aEncoded, aChars, aOffset);

            // Then
            assertEquals(
                aExpectedAlpha3Code,
                new String(aChars, aOffset, aCodeLength),
                "Decode failed: " + aOffsetAlpha3Code);
        }
    }


    @Test
    public void decodeAlpha4DecodesValueFromEncodeAlpha4()
    {
        int aCodeLength = 4;
        for (int i=0; i<100; i++)
        {
            // Given
            String aAlpha4Code = randomAlphaCode(aCodeLength);
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(aCodeLength);

            // When
            int aEncoded = AlphaCodes.encodeAlpha4(aAlpha4Code);
            AlphaCodes.decodeAlpha4(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aAlpha4Code, aDecodeBuffer.toString());

            // When
            char[] aDecodedChars = new char[aCodeLength];
            AlphaCodes.decodeAlpha4(aEncoded, aDecodedChars, 0);

            // Then
            assertEquals(aAlpha4Code, new String(aDecodedChars));
        }
    }


    @Test
    public void decodeAlpha4DecodesValueFromEncodeAlpha4AtOffset()
    {
        int aCodeLength = 4;
        for (int i=0; i<100; i++)
        {
            // Given
            int aOffset = ThreadLocalRandom.current().nextInt(1, 32);
            String aOffsetAlpha4Code = randomAlphaCode(aCodeLength, aOffset);
            String aExpectedAlpha4Code = aOffsetAlpha4Code.substring(aOffset);

            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(aCodeLength);

            // When
            int aEncoded = AlphaCodes.encodeAlpha4(aOffsetAlpha4Code, aOffset);
            AlphaCodes.decodeAlpha4(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(
                aExpectedAlpha4Code,
                aDecodeBuffer.toString(),
                "Decode failed: " + aOffsetAlpha4Code);

            // When
            char[] aChars = new char[aCodeLength + aOffset];
            AlphaCodes.decodeAlpha4(aEncoded, aChars, aOffset);

            // Then
            assertEquals(
                aExpectedAlpha4Code,
                new String(aChars, aOffset, aCodeLength),
                "Decode failed: " + aOffsetAlpha4Code);
        }
    }


    @Test
    public void decodeAlpha3DecodesEncodedIso4127Code()
    {
        for (String aIso4217Code : ISO4217_CODES)
        {
            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(3);

            // When
            int aEncoded = AlphaCodes.encodeAlpha3(aIso4217Code);
            AlphaCodes.decodeAlpha3(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aIso4217Code, aDecodeBuffer.toString());
        }
    }


    @Test
    public void decodeAlpha2DecodesEncodedIso3166Alpha2Code()
    {
        for (String aIso3166Code : ISO3166_ALPHA2_CODES)
        {
            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(2);

            // When
            int aEncoded = AlphaCodes.encodeAlpha2(aIso3166Code);
            AlphaCodes.decodeAlpha2(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aIso3166Code, aDecodeBuffer.toString());
        }
    }


    @Test
    public void decodeAlpha3DecodesEncodedIso3166Alpha3Code()
    {
        for (String aIso3166Code : ISO3166_ALPHA3_CODES)
        {
            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(3);

            // When
            int aEncoded = AlphaCodes.encodeAlpha3(aIso3166Code);
            AlphaCodes.decodeAlpha3(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aIso3166Code, aDecodeBuffer.toString());
        }
    }


    @Test
    public void decodeAlpha2DecodesEncodedIso639Alpha2Code()
    {
        for (String aIso639Code : ISO639_ALPHA2_CODES)
        {
            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(2);

            // When
            int aEncoded = AlphaCodes.encodeAlpha2(aIso639Code);
            AlphaCodes.decodeAlpha2(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aIso639Code, aDecodeBuffer.toString());
        }
    }


    @Test
    public void decodeAlpha4DecodesEncodedFourLetterCode()
    {
        for (String aFourLetterCode : FOUR_LETTER_CODES)
        {
            // Given
            StringBuilder aDecodeBuffer = new StringBuilder();
            aDecodeBuffer.setLength(4);

            // When
            int aEncoded = AlphaCodes.encodeAlpha4(aFourLetterCode);
            AlphaCodes.decodeAlpha4(aEncoded, aDecodeBuffer::setCharAt);

            // Then
            assertEquals(aFourLetterCode, aDecodeBuffer.toString());
        }
    }


    static private String randomAlphaCode(int pNumChars)
    {
        return randomAlphaCode(pNumChars, 0);
    }


    static private String randomAlphaCode(int pNumChars, int pOffset)
    {
        StringBuilder aBuilder = new StringBuilder();

        for (int i=0; i<pOffset; i++)
            aBuilder.append('?');
        for (int i=0; i<pNumChars; i++)
            aBuilder.append(randomAsciiChar());

        return aBuilder.toString();
    }


    static private char randomAsciiChar()
    {
        return (char) ThreadLocalRandom.current().nextInt(128);
    }
}
