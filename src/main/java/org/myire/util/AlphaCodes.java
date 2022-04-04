/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;


/**
 * Methods for encoding and decoding two-, three, and four-character alpha codes as 32-bit integer
 * values. Examples of alpha codes are ISO 4217 currency codes, ISO 3166 country codes, and ISO 639
 * language codes.
 *<p>
 * The characters in the alpha codes are encoded as 8-bit values, making the encoding suitable for
 * alpha codes using only the first 256 characters in the UTF-16 character set. The characters are
 * encoded from most to least significant byte, i.e. the last character in the code is encoded in
 * byte 0, the second to last character in byte 1, and so on.
 *<p>
 * Examples:
 *<pre>
 *  Alpha code      32-bit value
 *  ----------      ------------
 *  sv              0x00007376
 *  EUR             0x00455552
 *  XHEL            0x5848454c
 *</pre>
 */
public final class AlphaCodes
{
    private AlphaCodes()
    {
        // Don't allow instantiations of utility classes.
    }


    /**
     * Encode a two-character code as a 32-bit integer.
     *
     * @param pAlpha2Code   The two-character code to encode.
     *
     * @return  The first two characters in {@code pAlpha2Code} encoded as a 32-bit value.
     *
     * @throws NullPointerException if {@code pAlpha2Code} is null.
     * @throws IndexOutOfBoundsException if {@code pAlpha2Code} has less than two characters.
     */
    static public int encodeAlpha2(@Nonnull CharSequence pAlpha2Code)
    {
        return encodeAlpha2(pAlpha2Code, 0);
    }


    /**
     * Encode a two-character code as a 32-bit integer.
     *
     * @param pAlpha2Code   The two-character code to encode.
     * @param pOffset       The offset in {@code pAlpha2Code} of the code's first character.
     *
     * @return  The two characters starting at {@code pOffset} encoded as a 32-bit value.
     *
     * @throws NullPointerException if {@code pAlpha2Code} is null.
     * @throws IndexOutOfBoundsException if {@code pAlpha2Code} has less than two characters
     *                                   counting from {@code pOffset}, or if {@code pOffset} is
     *                                   negative.
     */
    static public int encodeAlpha2(@Nonnull CharSequence pAlpha2Code, @Nonnegative int pOffset)
    {
        return ((pAlpha2Code.charAt(pOffset) & 0xff) << 8) | (pAlpha2Code.charAt(pOffset+1) & 0xff);
    }


    /**
     * Encode a three-character code as a 32-bit integer.
     *
     * @param pAlpha3Code   The three-character code to encode.
     *
     * @return  The first three characters in {@code pAlpha3Code} encoded as a 32-bit value.
     *
     * @throws NullPointerException if {@code pAlpha3Code} is null.
     * @throws IndexOutOfBoundsException if {@code pAlpha3Code} has less than three characters.
     */
    static public int encodeAlpha3(@Nonnull CharSequence pAlpha3Code)
    {
        return encodeAlpha3(pAlpha3Code, 0);
    }


    /**
     * Encode a three-character code as a 32-bit integer.
     *
     * @param pAlpha3Code   The three-character code to encode.
     * @param pOffset       The offset in {@code pAlpha3Code} of the code's first character.
     *
     * @return  The three characters starting at {@code pOffset} encoded as a 32-bit value.
     *
     * @throws NullPointerException if {@code pAlpha3Code} is null.
     * @throws IndexOutOfBoundsException if {@code pAlpha3Code} has less than three characters
     *                                   counting from {@code pOffset}, or if {@code pOffset} is
     *                                   negative.
     */
    static public int encodeAlpha3(@Nonnull CharSequence pAlpha3Code, @Nonnegative int pOffset)
    {
        return (encodeAlpha2(pAlpha3Code, pOffset) << 8) | (pAlpha3Code.charAt(pOffset + 2) & 0xff);
    }


    /**
     * Encode a four-character code as a 32-bit integer.
     *
     * @param pAlpha4Code   The four-character code to encode.
     *
     * @return  The first four characters in {@code pAlpha4Code} encoded as a 32-bit value.
     *
     * @throws NullPointerException if {@code pAlpha4Code} is null.
     * @throws IndexOutOfBoundsException if {@code pAlpha4Code} has less than four characters.
     */
    static public int encodeAlpha4(@Nonnull CharSequence pAlpha4Code)
    {
        return encodeAlpha4(pAlpha4Code, 0);
    }


    /**
     * Encode a four-character code as a 32-bit integer.
     *
     * @param pAlpha4Code   The four-character code to encode.
     * @param pOffset       The offset of the first character.
     *
     * @return  The four characters starting at {@code pOffset} encoded as a 32-bit value.
     *
     * @throws NullPointerException if {@code pAlpha4Code} is null.
     * @throws IndexOutOfBoundsException if {@code pAlpha4Code} has less than four characters
     *                                   counting from {@code pOffset}, or if {@code pOffset} is
     *                                   negative.
     */
    static public int encodeAlpha4(@Nonnull CharSequence pAlpha4Code, @Nonnegative int pOffset)
    {
        return (encodeAlpha3(pAlpha4Code, pOffset) << 8) | (pAlpha4Code.charAt(pOffset+3) & 0xff);
    }


    /**
     * Decode a 32-bit value into two characters.
     *
     * @param pEncodedValue The encoded two characters.
     * @param pDestination  The destination for the decoded characters. The first character will be
     *                      passed to the destination with index 0, and the second with index 1.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     */
    static public void decodeAlpha2(int pEncodedValue, @Nonnull PutCharAtFunction pDestination)
    {
        pDestination.putCharAt(0, (char) ((pEncodedValue >> 8) & 0xff));
        pDestination.putCharAt(1, (char) (pEncodedValue & 0xff));
    }


    /**
     * Decode a 32-bit value into two characters.
     *
     * @param pEncodedValue The encoded two characters.
     * @param pDestination  The destination for the decoded characters. The first character will be
     *                      put into the array at offset {@code pOffset}, and the second at offset
     *                      {@code pOffset + 1}.
     * @param pOffset       The offset in {@code pDestination} where to put the first decoded
     *                      character.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, of if {@code pDestination}
     *                                   has a length less than {@code pOffset + 2}.
     */
    static public void decodeAlpha2(
        int pEncodedValue,
        @Nonnull char[] pDestination,
        @Nonnegative int pOffset)
    {
        pDestination[pOffset] = (char) ((pEncodedValue >> 8) & 0xff);
        pDestination[pOffset+1] = (char) (pEncodedValue & 0xff);
    }


    /**
     * Decode a 32-bit value into three characters.
     *
     * @param pEncodedValue The encoded three characters.
     * @param pDestination  The destination for the decoded characters. The first character will be
     *                      passed to the destination with index 0, the second with index 1, and the
     *                      third and last with index 2.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     */
    static public void decodeAlpha3(int pEncodedValue, @Nonnull PutCharAtFunction pDestination)
    {
        pDestination.putCharAt(0, (char) ((pEncodedValue >> 16) & 0xff));
        pDestination.putCharAt(1, (char) ((pEncodedValue >> 8) & 0xff));
        pDestination.putCharAt(2, (char) (pEncodedValue & 0xff));
    }


    /**
     * Decode a 32-bit value into three characters.
     *
     * @param pEncodedValue The encoded three characters.
     * @param pDestination  The destination for the decoded characters. The first character will be
     *                      put into the array at offset {@code pOffset}, the second at offset
     *                      {@code pOffset + 1}, and the third at {@code pOffset + 2}.
     * @param pOffset       The offset in {@code pDestination} where to put the first decoded
     *                      character.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, of if {@code pDestination}
     *                                   has a length less than {@code pOffset + 3}.
     */
    static public void decodeAlpha3(
        int pEncodedValue,
        @Nonnull char[] pDestination,
        @Nonnegative int pOffset)
    {
        pDestination[pOffset] = (char) ((pEncodedValue >> 16) & 0xff);
        decodeAlpha2(pEncodedValue, pDestination, pOffset+1);
    }


    /**
     * Decode a 32-bit value into four characters.
     *
     * @param pEncodedValue The encoded four characters.
     * @param pDestination  The destination for the decoded characters. The first character will be
     *                      passed to the destination with index 0, the second with index 1, the
     *                      third with index 2, and the fourth and last with index 3.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     */
    static public void decodeAlpha4(int pEncodedValue, @Nonnull PutCharAtFunction pDestination)
    {
        pDestination.putCharAt(0, (char) ((pEncodedValue >> 24) & 0xff));
        pDestination.putCharAt(1, (char) ((pEncodedValue >> 16) & 0xff));
        pDestination.putCharAt(2, (char) ((pEncodedValue >> 8) & 0xff));
        pDestination.putCharAt(3, (char) (pEncodedValue & 0xff));
    }


    /**
     * Decode a 32-bit value into four characters.
     *
     * @param pEncodedValue The encoded four characters.
     * @param pDestination  The destination for the decoded characters. The first character will be
     *                      put into the array at offset {@code pOffset}, the second at offset
     *                      {@code pOffset + 1}, the third at {@code pOffset + 2}, and the fourth at
     *                      {@code pOffset + 3}.
     * @param pOffset       The offset in {@code pDestination} where to put the first decoded
     *                      character.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, of if {@code pDestination}
     *                                   has a length less than {@code pOffset + 4}.
     */
    static public void decodeAlpha4(
        int pEncodedValue,
        @Nonnull char[] pDestination,
        @Nonnegative int pOffset)
    {
        pDestination[pOffset] = (char) ((pEncodedValue >> 24) & 0xff);
        decodeAlpha3(pEncodedValue, pDestination, pOffset+1);
    }
}
