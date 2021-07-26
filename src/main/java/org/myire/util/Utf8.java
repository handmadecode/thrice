/*
 * Copyright 2018, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static org.myire.util.Numbers.requireNonNegative;


/**
 * Utilities for UTF-8 encoding and decoding.
 */
public final class Utf8
{
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    private Utf8()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Encode the characters returned by a {@code GetCharAtFunction} with UTF-8 and pass the encoded
     * bytes to a {@code PutByteAtFunction}. If the resulting number of encoded bytes exceeds the
     * specified maximum number of encoded bytes, only the number of characters for which
     * the number of encoded bytes are less than or equal to this maximum will be encoded.
     *
     * @param pSource               A function returning the characters to encode.
     * @param pSourceOffset         The offset in {@code pSource} of the first char to encode.
     * @param pSourceLength         The number of characters, starting at {@code pSourceOffset}, to
     *                              encode.
     * @param pDestination          A function accepting the encoded bytes.
     * @param pDestinationOffset    The offset in {@code pDestination} to put the first encoded byte
     *                              at.
     * @param pDestinationMaxLength The maximum number of bytes the destination can accept, counting
     *                              from offset {@code pDestinationOffset}.
     *
     * @return  The number of encoded bytes passed to {@code pDestination}.
     *
     * @throws NullPointerException if any of the reference parameters is null.
     * @throws IllegalArgumentException if any of the int parameters is negative.
     * @throws IndexOutOfBoundsException if the source offset and length or the destination offset
     *                                   and max length specify an invalid index.
     */
    static public int utf8Encode(
        @Nonnull GetCharAtFunction pSource,
        @Nonnegative int pSourceOffset,
        @Nonnegative int pSourceLength,
        @Nonnull PutByteAtFunction pDestination,
        @Nonnegative int pDestinationOffset,
        @Nonnegative int pDestinationMaxLength)
    {
        int aCharPos = requireNonNegative(pSourceOffset);
        int aLastCharPos = pSourceOffset + requireNonNegative(pSourceLength);
        int aBytePos = requireNonNegative(pDestinationOffset);
        int aLastBytePos = pDestinationOffset + requireNonNegative(pDestinationMaxLength);
        while (aCharPos < aLastCharPos)
        {
            char aChar = pSource.charAt(aCharPos++);
            if (aChar < 0x0080)
            {
                // Chars in the range 0x00 - 0x7f (i.e. Ascii chars) are encoded as single bytes.
                if (aBytePos >= aLastBytePos)
                    break;
                pDestination.putByteAt(aBytePos++, (byte) aChar);
            }
            else if (aChar < 0x0800)
            {
                // Chars in the range 0x0080 - 0x07ff, i.e. that occupy between 8 and 11 bits, are
                // encoded as two bytes. The first byte starts with the prefix 110 followed by the
                // five most significant bits, and the second byte starts with the prefix 10
                // followed by the remaining six bits.
                if (aLastBytePos - aBytePos < 2)
                    // Not enough room for two more bytes, abort the encoding.
                    break;

                // 0x07c0 is the mask for bits 6-10, which are right shifted to occupy bits 0-4 in
                // the first byte, which is prefixed with 0xc0 (110xxxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x00c0 | ((aChar & 0x07c0) >> 6)));

                // 0x003f is the mask for bits 0-5, which are put into the second byte, which is
                // prefixed with 0x80 (10xxxxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x80 | aChar & 0x003f));
            }
            else if (aChar < 0xd800 || aChar > 0xdfff)
            {
                // Chars in the ranges 0x0800 - 0xd7ff and 0xe000 - 0xffff are encoded as three
                // bytes. The first byte starts with the prefix 1110 followed by the four most
                // significant bits, the second and third byte start both with the prefix 10
                // followed by  six data bits (bits 6-11 and 0-5, respectively), thus encoding
                // 4+6+6 == 16 bits.
                if (aLastBytePos - aBytePos < 3)
                    // Not enough room for three more bytes, abort the encoding.
                    break;

                // 0xf000 is the mask for bits 12-15, which are right shifted to occupy bits 0-3 in
                // the first byte, which is prefixed with 0xe0 (1110xxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x00e0 | ((aChar & 0xf000) >> 12)));

                // 0x0fc0 is the mask for bits 6-11, which are right shifted to occupy bits 0-5 in
                // the second byte, which is prefixed with 0x80 (10xxxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x0080 | ((aChar & 0x0fc0) >> 6)));

                // 0x003f is the mask for bits 0-5, which are put into the third byte, which is
                // prefixed with 0x80 (10xxxxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x80 | aChar & 0x003f));
            }
            else
            {
                // Java chars are encoded with UTF-16, where the range 0xd800 - 0xdfff is used to
                // encode Unicode code points in the range 0x010000 - 0x10ffff. The range
                // 0xd800 - 0xdfff is reserved in the Unicode standard and characters will not be
                // assigned to code points in that range.
                // Characters in the range 0x010000 - 0x10ffff are encoded using two 16-bit values
                // called a surrogate pair as follows:
                // - 0x10000 is subtracted from the code point, leaving a 20-bit number in the
                //   range 0x00000 – 0xfffff.
                // - The high ten bits (in the range 0x000 – 0x3ff) are added to 0xd800 to give the
                //   first 16-bit value (the high surrogate), which will be in the
                //   range 0xd800 – 0xdbff.
                // - The low ten bits (also in the range 0x000–0x3dd) are added to 0xdc00 to give
                //   the second 16-bit value (the low surrogate), which will be in the
                //   range 0xdc00 – 0xdfff.
                // In UTF-8, the characters in the range 0x010000 - 0x10ffff (i.e. those with bit 20
                // set) are encoded as four bytes. The first byte starts with the prefix 11110
                // followed by the three most significant bits. The second, third and fourth  byte
                // all start with the prefix 10 followed by six data bits (bits 12-17, 6-11 and 0-5,
                // respectively), thus encoding 3+6+6+6 == 21 bits.
                if (aLastBytePos - aBytePos < 4 || aLastCharPos - aCharPos < 1)
                    // Not enough room for four more bytes, or low surrogate missing, abort the
                    // encoding.
                    break;

                // aChar holds the high surrogate, get the low surrogate in the pair.
                char aLow = pSource.charAt(aCharPos++);

                // Calculate the Unicode code point for the surrogate pair.
                int aCodePoint = Character.toCodePoint(aChar, aLow);

                // 0x1c0000 is the mask for bits 18-20, which are right shifted to occupy
                // bits 0-2 in the first byte, which is prefixed with 0xf0 (11110xxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x00f0 | ((aCodePoint & 0x1c0000) >> 18)));

                // 0x3f000 is the mask for bits 12-17, which are right shifted to occupy
                // bits 0-5 in the second byte, which is prefixed with 0x80 (10xxxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x0080 | ((aCodePoint & 0x3f000) >> 12)));

                // 0x0fc0 is the mask for bits 6-11, which are right shifted to occupy
                // bits 0-5 in the third byte, which is prefixed with 0x80 (10xxxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x0080 | ((aCodePoint & 0x0fc0) >> 6)));

                // 0x003f is the mask for bits 0-5, which are put into the fourth byte, which is
                // prefixed with 0x80 (10xxxxxx).
                pDestination.putByteAt(aBytePos++, (byte) (0x0080 | aCodePoint & 0x003f));
            }
        }

        return aBytePos - pDestinationOffset;
    }


    /**
     * Decode the bytes returned by a {@code GetByteAtFunction} as UTF-8 bytes and pass the decoded
     * chars to a {@code PutCharAtFunction}. If the resulting number of decoded chars exceeds the
     * specified maximum number of decoded chars, only the number of bytes for which the number of
     * decoded chars are less than or equal to this maximum will be decoded.
     *<p>
     * The decoding process will silently be aborted if the bytes contain an incomplete UTF-8
     * encoding, resulting in a possibly incomplete but still valid decoded character sequence.
     *
     * @param pSource               A function returning the bytes to decode.
     * @param pSourceOffset         The offset in {@code pSource} of the first byte to decode.
     * @param pSourceLength         The number of bytes, starting at {@code pSourceOffset}, to
     *                              decode.
     * @param pDestination          A function accepting the decoded chars.
     * @param pDestinationOffset    The offset in {@code pDestination} to put the first decoded char
     *                              at.
     * @param pDestinationMaxLength The maximum number of chars the destination can accept, counting
     *                              from offset {@code pDestinationOffset}.
     *
     * @return  The number of decoded characters passed to {@code pDestination}.
     *
     * @throws NullPointerException if any of the reference parameters is null.
     * @throws IllegalArgumentException if any of the int parameters is negative.
     * @throws IndexOutOfBoundsException if the source offset and length or the destination offset
     *                                   and max length specify an invalid index.
     */
    static public int utf8Decode(
        @Nonnull GetByteAtFunction pSource,
        @Nonnegative int pSourceOffset,
        @Nonnegative int pSourceLength,
        @Nonnull PutCharAtFunction pDestination,
        @Nonnegative int pDestinationOffset,
        @Nonnegative int pDestinationMaxLength)
    {
        int aBytePos = requireNonNegative(pSourceOffset);
        int aLastBytePos = pSourceOffset + requireNonNegative(pSourceLength);
        int aCharPos = requireNonNegative(pDestinationOffset);
        int aLastCharPos = pDestinationOffset + requireNonNegative(pDestinationMaxLength);
        while (aBytePos < aLastBytePos && aCharPos < aLastCharPos)
        {
            byte aByte = pSource.byteAt(aBytePos++);
            if ((aByte & 0x80) == 0)
            {
                // 7 bits (0xxxxxxx), a 7-bit char
                pDestination.putCharAt(aCharPos++, (char) aByte);
            }
            else if ((aByte & 0xe0) == 0xc0)
            {
                // 110xxxxx, the char has 11 bits encoded in two bytes, 110xxxxx 10xxxxxx
                if (aLastBytePos - aBytePos < 1)
                    // Incomplete UTF-8 sequence.
                    break;

                byte aByte2 = pSource.byteAt(aBytePos++);
                pDestination.putCharAt(aCharPos++, (char) (((aByte & 0x1f) << 6) | (aByte2 & 0x3f)));
            }
            else if ((aByte & 0xf0) == 0xe0)
            {
                // 1110xxxx, the char has 16 bits encoded in three bytes, 1110xxxx 10xxxxxx 10xxxxxx
                if (aLastBytePos - aBytePos < 2)
                    // Incomplete UTF-8 sequence.
                    break;

                byte aByte2 = pSource.byteAt(aBytePos++);
                byte aByte3 = pSource.byteAt(aBytePos++);
                pDestination.putCharAt(
                    aCharPos++,
                    (char) (((aByte & 0x0f) << 12) | ((aByte2 & 0x3f) << 6) | (aByte3 & 0x3f))
                );
            }
            else
            {
                // 11110xxx, the char has 21 bits encoded in four bytes,
                //          11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                // and decodes into two UTF-16 chars; the high and the low surrogate.
                if (aLastBytePos - aBytePos < 3)
                    // Incomplete UTF-8 sequence.
                    break;
                if (aLastCharPos - aCharPos < 2)
                    // Not enough room at the destination for two more characters.
                    break;

                byte aByte2 = pSource.byteAt(aBytePos++);
                byte aByte3 = pSource.byteAt(aBytePos++);
                byte aByte4 = pSource.byteAt(aBytePos++);
                int aCodePoint =
                    (((aByte & 0x07) << 18) | ((aByte2 & 0x3f) << 12) | ((aByte3 & 0x3f) << 6) | (aByte4 & 0x3f));
                pDestination.putCharAt(aCharPos++, Character.highSurrogate(aCodePoint));
                pDestination.putCharAt(aCharPos++, Character.lowSurrogate(aCodePoint));
            }
        }

        return aCharPos - pDestinationOffset;
    }
}
