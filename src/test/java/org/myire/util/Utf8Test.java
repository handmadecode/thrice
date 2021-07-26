/*
 * Copyright 2018, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.concurrent.ThreadLocalRandom;
import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;


/**
 * Unit tests for the utility methods in {@code Utf8}.
 */
public class Utf8Test
{
    @Test
    public void zeroLengthStringEncodesToZeroBytes()
    {
        // Given
        String aString = "not to be encoded";
        PutByteAtFunction aDestination = mock(PutByteAtFunction.class);

        // When
        int aEncodedLength =
            Utf8.utf8Encode(
                aString::charAt,
                0,
                0,
                aDestination,
                0,
                Integer.MAX_VALUE);

        // Then
        assertEquals(aEncodedLength, 0);
        verifyNoInteractions(aDestination);
    }


    @Test
    public void stringEncodesToZeroBytesWhenDestinationHasZeroMaxLength()
    {
        // Given
        String aString = "not to be encoded";
        PutByteAtFunction aDestination = mock(PutByteAtFunction.class);

        // When
        int aEncodedLength =
            Utf8.utf8Encode(
                aString::charAt,
                0,
                aString.length(),
                aDestination,
                0,
                0);

        // Then
        assertEquals(aEncodedLength, 0);
        verifyNoInteractions(aDestination);
    }


    @Test
    public void asciiCharsAreEncodedCorrectly()
    {
        assertEncoding(randomString(0x0, 0x7f));
    }


    @Test
    public void twoByteCharsAreEncodedCorrectly()
    {
        assertEncoding(randomString(0x80, 0x0800));
    }


    @Test
    public void lowThreeByteCharsAreEncodedCorrectly()
    {
        assertEncoding(randomString(0x0800, 0xd800));
    }


    @Test
    public void highThreeByteCharsAreEncodedCorrectly()
    {
        assertEncoding(randomString(0xdfff, 0xffff));
    }


    @Test
    public void surrogatePairCharsAreEncodedCorrectly()
    {
        assertEncoding(codePointsToString(0x296dc, 0x1f644, 0x29ae6));
    }


    @Test
    public void tooLongOneByteCharSequenceIsPartiallyEncoded()
    {
        // Given
        String aString = "abcdefgh";
        byte[] aUtf8Bytes = aString.getBytes(UTF_8);
        int aMaxLength = aUtf8Bytes.length - 1;
        byte[] aExpectedBytes = new byte[aMaxLength];
        System.arraycopy(aUtf8Bytes, 0, aExpectedBytes, 0, aExpectedBytes.length);

        // When (encode restricting the maximum length to one less than the full UTF-8 byte
        // sequence)
        ByteBuffer aEncodedBytes = ByteBuffer.allocate(aMaxLength);
        int aEncodedLength =
            Utf8.utf8Encode(
                aString::charAt,
                0,
                aString.length(),
                aEncodedBytes::put,
                0,
                aMaxLength);

        // Then
        assertEquals(aEncodedLength, aMaxLength);
        assertEqualBytes(aString, aExpectedBytes, aEncodedBytes);
    }


    @Test
    public void tooLongTwoByteCharSequenceIsPartiallyEncoded()
    {
        // Given
        String aString = "räksmörgås";
        byte[] aUtf8Bytes = aString.getBytes(UTF_8);
        int aMaxLength = aUtf8Bytes.length - 3;
        byte[] aExpectedBytes = new byte[aMaxLength];
        System.arraycopy(aUtf8Bytes, 0, aExpectedBytes, 0, aExpectedBytes.length);

        // When (encode restricting the maximum length to three less than the full UTF-8 byte
        // sequence)
        ByteBuffer aEncodedBytes = ByteBuffer.allocate(aMaxLength);
        int aEncodedLength =
            Utf8.utf8Encode(
                aString::charAt,
                0,
                aString.length(),
                aEncodedBytes::put,
                0,
                aMaxLength);

        // Then
        assertEquals(aEncodedLength, aMaxLength);
        assertEqualBytes(aString, aExpectedBytes, aEncodedBytes);
    }


    @Test
    public void tooLongThreeByteCharSequenceIsPartiallyEncoded()
    {
        // Given
        String aString = "123\u34c5hh";
        byte[] aUtf8Bytes = aString.getBytes(UTF_8);
        int aMaxLength = aUtf8Bytes.length - 5;
        byte[] aExpectedBytes = new byte[aMaxLength];
        System.arraycopy(aUtf8Bytes, 0, aExpectedBytes, 0, aExpectedBytes.length);

        // When (encode restricting the maximum length to five less than the full UTF-8 byte
        // sequence)
        ByteBuffer aEncodedBytes = ByteBuffer.allocate(aMaxLength);
        int aEncodedLength =
            Utf8.utf8Encode(
                aString::charAt,
                0,
                aString.length(),
                aEncodedBytes::put,
                0,
                aMaxLength);

        // Then
        assertEquals(aEncodedLength, aMaxLength);
        assertEqualBytes(aString, aExpectedBytes, aEncodedBytes);
    }


    @Test
    public void tooLongFourByteCharSequenceIsPartiallyEncoded()
    {
        // Given (a string with three codepoints that are represented as six UTF-16 characters and
        // are encoded as 12 UTF-8 bytes)
        String aString = codePointsToString(0x296dc, 0x2994b, 0x2b5b3);
        byte[] aUtf8Bytes = aString.getBytes(UTF_8);

        // When (encode restricting the maximum length to not have room for the last two bytes of
        // the last codepoint)
        int aMaxLength = aUtf8Bytes.length - 2;
        ByteBuffer aEncodedBytes = ByteBuffer.allocate(aMaxLength);
        int aEncodedLength =
            Utf8.utf8Encode(
                aString::charAt,
                0,
                aString.length(),
                aEncodedBytes::put,
                0,
                aMaxLength);

        // Then (the last codepoint, i.e. the last four UTF-8 bytes, do not fit into the maximum
        // length and should not be part of the encoding).
        byte[] aExpectedBytes = new byte[aUtf8Bytes.length - 4];
        System.arraycopy(aUtf8Bytes, 0, aExpectedBytes, 0, aExpectedBytes.length);
        assertEquals(aEncodedLength, aExpectedBytes.length);
        assertEqualBytes(aString, aExpectedBytes, aEncodedBytes);
    }


    @Test
    public void missingLowSurrogateIsSkippedInEncoding()
    {
        // Given (a string with two codepoints encoded as two surrogate pairs)
        String aString = codePointsToString(0x1f4a9, 0x296dc);
        char[] aChars = aString.toCharArray();
        byte[] aExpectedBytes = aString.substring(0, 2).getBytes(UTF_8);

        // When (the chars to encode only include the first half of the second surrogate pair)
        ByteBuffer aEncodedBytes = ByteBuffer.allocate(16);
        int aEncodedLength =
            Utf8.utf8Encode(
                i -> aChars[i],
                0,
                aChars.length - 1,
                aEncodedBytes::put,
                0,
                aEncodedBytes.limit());

        // Then
        assertEquals(aEncodedLength, aExpectedBytes.length);
        assertEqualBytes(aString, aExpectedBytes, aEncodedBytes);
    }


    @Test
    public void encodeThrowsForNegativeSourceOffset()
    {
        // Given
        String aString = "Midway in the journey of our life";

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Encode(
                    aString::charAt,
                    -1,
                    aString.length(),
                    mock(PutByteAtFunction.class),
                    0,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void encodeThrowsForNegativeSourceLength()
    {
        // Given
        String aString = "I came to myself in a dark wood";

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Encode(
                    aString::charAt,
                    0,
                    -1,
                    mock(PutByteAtFunction.class),
                    0,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void encodeThrowsForNegativeDestinationOffset()
    {
        // Given
        String aString = "for the straight way was lost";

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Encode(
                    aString::charAt,
                    0,
                    aString.length(),
                    mock(PutByteAtFunction.class),
                    -1,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void encodeThrowsForNegativeDestinationLength()
    {
        // Given
        String aString = "Ah, how hard it is to tell";

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Encode(
                    aString::charAt,
                    0,
                    aString.length(),
                    mock(PutByteAtFunction.class),
                    0,
                    -1)
        );
    }


    @Test
    public void encodeThrowsForInvalidSourceIndex()
    {
        // Given
        String aString = "the nature of that wood, savage, dense and harsh";

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Utf8.utf8Encode(
                    aString::charAt,
                    aString.length(),
                    aString.length(),
                    mock(PutByteAtFunction.class),
                    0,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void encodeThrowsForInvalidDestinationIndex()
    {
        // Given
        String aString = "the very thought of it renews my fear";
        ByteBuffer aDestination = ByteBuffer.allocate(8);

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Utf8.utf8Encode(
                    aString::charAt,
                    0,
                    aString.length(),
                    aDestination::put,
                    aDestination.limit(),
                    aDestination.limit())
        );
    }


    @Test
    public void zeroLengthByteSourceDecodesToZeroChars()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.wrap("not to be decoded".getBytes(UTF_8));
        PutCharAtFunction aDestination = mock(PutCharAtFunction.class);

        // When
        int aDecodedLength =
            Utf8.utf8Decode(
                aBytes::get,
                0,
                0,
                aDestination,
                0,
                Integer.MAX_VALUE);

        // Then
        assertEquals(aDecodedLength, 0);
        verifyNoInteractions(aDestination);
    }


    @Test
    public void bytesDecodeToZeroCharsWhenDestinationHasZeroMaxLength()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.wrap("not to be decoded".getBytes(UTF_8));
        PutCharAtFunction aDestination = mock(PutCharAtFunction.class);

        // When
        int aDecodedLength =
            Utf8.utf8Decode(
                aBytes::get,
                0,
                aBytes.limit(),
                aDestination,
                0,
                0);

        // Then
        assertEquals(aDecodedLength, 0);
        verifyNoInteractions(aDestination);
    }


    @Test
    public void asciiCharsAreDecodedCorrectly()
    {
        assertDecoding(randomString(0x0, 0x7f));
    }


    @Test
    public void twoByteCharsAreDecodedCorrectly()
    {
        assertDecoding(randomString(0x80, 0x0800));
    }


    @Test
    public void lowThreeByteCharsAreDecodedCorrectly()
    {
        assertDecoding(randomString(0x0800, 0xd800));
    }


    @Test
    public void highThreeByteCharsAreDecodedCorrectly()
    {
        assertDecoding(randomString(0xdfff, 0xffff));
    }


    @Test
    public void surrogatePairCharsAreDecodedCorrectly()
    {
        assertDecoding(codePointsToString(0x296dc, 0x2994b));
    }


    @Test
    public void incompleteTwoByteSequenceIsPartiallyDecoded()
    {
        // Given
        String aString = "uppepå";
        ByteBuffer aUtf8Bytes = ByteBuffer.wrap(aString.getBytes(UTF_8));

        // When (decode all UTF-8 bytes except the last one, making the encoded bytes end with an
        // incomplete two-byte sequence)
        StringBuilder aStringBuilder = new StringBuilder();
        aStringBuilder.setLength(aString.length());
        int aNumChars = Utf8.utf8Decode(
            aUtf8Bytes::get,
            0,
            aUtf8Bytes.limit() - 1,
            aStringBuilder::setCharAt,
            0,
            aStringBuilder.length() + 10);

        // Then (the incomplete last char should not be decoded)
        String aExpected = aString.substring(0, aString.length() - 1);
        assertEquals(aExpected.length(), aNumChars);
        aStringBuilder.setLength(aNumChars);
        assertEquals(aExpected, aStringBuilder.toString());
    }


    @Test
    public void incompleteThreeByteSequenceIsPartiallyDecoded()
    {
        // Given
        String aString = "\ucf73\ucc1d";
        ByteBuffer aUtf8Bytes = ByteBuffer.wrap(aString.getBytes(UTF_8));

        // When (decode all UTF-8 bytes except the last two, making the encoded bytes end with an
        // incomplete three-byte sequence)
        StringBuilder aStringBuilder = new StringBuilder();
        aStringBuilder.setLength(aString.length());
        int aNumChars = Utf8.utf8Decode(
            aUtf8Bytes::get,
            0,
            aUtf8Bytes.limit() - 2,
            aStringBuilder::setCharAt,
            0,
            aStringBuilder.length() + 10);

        // Then (the incomplete last char should not be decoded)
        String aExpected = aString.substring(0, aString.length() - 1);
        assertEquals(aExpected.length(), aNumChars);
        aStringBuilder.setLength(aNumChars);
        assertEquals(aExpected, aStringBuilder.toString());
    }


    @Test
    public void incompleteSurrogatePairIsSkippedInDecoding()
    {
        // Given
        String aChars = "abcdef" + codePointsToString(0x1f4a9);
        String aSurrogatePair = codePointsToString(0x296dc);
        ByteBuffer aUtf8Bytes = ByteBuffer.wrap((aChars + aSurrogatePair).getBytes(UTF_8));

        // When (decode all UTF-8 bytes except the last one, making the encoded bytes end with an
        // incomplete surrogate pair)
        StringBuilder aStringBuilder = new StringBuilder();
        aStringBuilder.setLength(aChars.length());
        int aNumChars = Utf8.utf8Decode(
            aUtf8Bytes::get,
            0,
            aUtf8Bytes.limit() - 1,
            aStringBuilder::setCharAt,
            0,
            aStringBuilder.length() + 10);

        // Then (the incomplete last char should not be decoded)
        assertEquals(aChars.length(), aNumChars);
        assertEquals(aChars, aStringBuilder.toString());
    }


    @Test
    public void surrogatePairIsSkippedInDecodingWhenDestinationCannotAcceptTwoMoreChars()
    {
        // Given
        String aChars = "abcdef" + codePointsToString(0x1f4a9);
        String aSurrogatePair = codePointsToString(0x296dc);
        ByteBuffer aUtf8Bytes = ByteBuffer.wrap((aChars + aSurrogatePair).getBytes(UTF_8));

        // When (decode all UTF-8 bytes into a destination that doesn't have room for the second
        // half of the last surrogate pair)
        StringBuilder aStringBuilder = new StringBuilder();
        aStringBuilder.setLength(aChars.length() + 32);
        int aNumChars = Utf8.utf8Decode(
            aUtf8Bytes::get,
            0,
            aUtf8Bytes.limit(),
            aStringBuilder::setCharAt,
            0,
            aChars.length() + 1);

        // Then
        assertEquals(aChars.length(), aNumChars);
        aStringBuilder.setLength(aNumChars);
        assertEquals(aChars, aStringBuilder.toString());
    }


    @Test
    public void decodeThrowsForNegativeSourceOffset()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.allocate(16);

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Decode(
                    aBytes::get,
                    -1,
                    aBytes.limit(),
                    mock(PutCharAtFunction.class),
                    0,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void decodeThrowsForNegativeSourceLength()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.allocate(16);

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Decode(
                    aBytes::get,
                    0,
                    -1,
                    mock(PutCharAtFunction.class),
                    0,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void decodeThrowsForNegativeDestinationOffset()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.allocate(16);

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Decode(
                    aBytes::get,
                    0,
                    aBytes.limit(),
                    mock(PutCharAtFunction.class),
                    -1,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void decodeThrowsForNegativeDestinationLength()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.allocate(16);

        // When
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Utf8.utf8Decode(
                    aBytes::get,
                    0,
                    aBytes.limit(),
                    mock(PutCharAtFunction.class),
                    0,
                    -1)
        );
    }


    @Test
    public void decodeThrowsForInvalidSourceIndex()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.allocate(16);

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Utf8.utf8Decode(
                    aBytes::get,
                    aBytes.limit(),
                    aBytes.limit(),
                    mock(PutCharAtFunction.class),
                    0,
                    Integer.MAX_VALUE)
        );
    }


    @Test
    public void decodeThrowsForInvalidDestinationIndex()
    {
        // Given
        ByteBuffer aBytes = ByteBuffer.wrap("bound to crash".getBytes(UTF_8));
        CharBuffer aDestination = CharBuffer.allocate(8);

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Utf8.utf8Decode(
                    aBytes::get,
                    0,
                    aBytes.limit(),
                    aDestination::put,
                    aDestination.limit(),
                    aDestination.limit())
        );
    }


    static private void assertEncoding(String pString)
    {
        // Given
        byte[] aExpectedBytes = pString.getBytes(UTF_8);
        ByteBuffer aEncodedBytes = ByteBuffer.allocate(aExpectedBytes.length);

        // When (encode from offset 0 into offset 0)
        int aEncodedLength =
            Utf8.utf8Encode(
                pString::charAt,
                0,
                pString.length(),
                aEncodedBytes::put,
                0,
                aEncodedBytes.limit());

        // Then
        assertEquals(aEncodedLength, aExpectedBytes.length, pString);
        assertEqualBytes(pString, aExpectedBytes, aEncodedBytes);

        // Given
        String aPrefix = "ignoreMe";
        StringBuilder aCharsWithPrefix = new StringBuilder(aPrefix).append(pString);
        int aByteOffset = 17;
        aEncodedBytes = ByteBuffer.allocate(aExpectedBytes.length + aByteOffset);

        // When (encode from an offset in the char source into an offset in the byte buffer).
        aEncodedLength =
            Utf8.utf8Encode(
                aCharsWithPrefix::charAt,
                aPrefix.length(),
                pString.length(),
                aEncodedBytes::put,
                aByteOffset,
                aEncodedBytes.limit() - aByteOffset);

        // Then
        assertEquals(aEncodedLength, aExpectedBytes.length, pString);
        assertEqualBytes(pString, aExpectedBytes, aEncodedBytes, aByteOffset);
    }


    static private void assertDecoding(String pString)
    {
        // Given
        byte[] aEncodedBytes = pString.getBytes(UTF_8);
        ByteBuffer aByteBuffer = ByteBuffer.wrap(aEncodedBytes);
        StringBuilder aStringBuilder = new StringBuilder();
        aStringBuilder.setLength(pString.length());

        // When (decode from offset 0 into offset 0)
        int aNumChars = Utf8.utf8Decode(
            aByteBuffer::get,
            0,
            aEncodedBytes.length,
            aStringBuilder::setCharAt,
            0,
            pString.length());

        // Then
        assertEquals(pString.length(), aNumChars);
        assertEquals(pString, aStringBuilder.toString());

        // Given
        int aByteOffset = 66;
        aByteBuffer = ByteBuffer.allocate(aByteOffset + aEncodedBytes.length);
        aByteBuffer.position(aByteOffset);
        aByteBuffer.put(aEncodedBytes);
        int aCharOffset = 7;
        aStringBuilder.setLength(aCharOffset + pString.length());

        // When (decode from an offset in the byte source into an offset in the byte buffer).
        aNumChars = Utf8.utf8Decode(
            aByteBuffer::get,
            aByteOffset,
            aEncodedBytes.length,
            aStringBuilder::setCharAt,
            aCharOffset,
            pString.length());

        // Then
        assertEquals(pString.length(), aNumChars);
        assertEquals(pString, aStringBuilder.substring(aCharOffset));
    }


    static private void assertEqualBytes(
        String pMessage,
        byte[] pExpected,
        ByteBuffer pActual)
    {
        assertEqualBytes(pMessage, pExpected, pActual, 0);
    }


    static private void assertEqualBytes(
        String pMessage,
        byte[] pExpected,
        ByteBuffer pActual,
        int pActualOffset)
    {
        for (int i=0; i<pExpected.length; i++)
            if (pExpected[i] != pActual.get(pActualOffset + i))
                fail(pMessage + ": element " + i + ": " + pExpected[i] + " != " + pActual.get(pActualOffset + i));
    }


    /**
     * Create a string with a length between 1 and 100 containing characters in the specified range.
     *
     * @param pLowUtf16Char     The low character, inclusive.
     * @param pHighUtf16Char    The high character, exclusive.
     *
     * @return  A new string.
     */
    static private String randomString(int pLowUtf16Char, int pHighUtf16Char)
    {
        int aLength = ThreadLocalRandom.current().nextInt(1, 100);
        StringBuilder aBuilder = new StringBuilder(aLength);
        for (int i=0; i<aLength; i++)
            aBuilder.append((char) ThreadLocalRandom.current().nextInt(pLowUtf16Char, pHighUtf16Char));

        return aBuilder.toString();
    }


    /**
     * Create a string form a sequence of code points.
     *
     * @param pCodePoints   The code points.
     *
     * @return  A new string.
     */
    static private String codePointsToString(int... pCodePoints)
    {
        StringBuilder aBuilder = new StringBuilder(pCodePoints.length * 2);
        for (int aCodePoint : pCodePoints)
            aBuilder.append(Character.highSurrogate(aCodePoint)).append(Character.lowSurrogate(aCodePoint));

        return aBuilder.toString();
    }
}
