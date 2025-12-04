/*
 * Copyright 2024-2025 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;


/**
 * Utilities for converting between primitive integer values and individual bytes.
 */
public final class Bytes
{
    // Implementation notes:
    // The to{Short|Int|Long} methods correspond to the private methods make{Short|Int|Long} in
    // java.nio.Bits.
    // The {short|int|long}Byte[n] methods correspond to the private methods {short|int|long}[n] in
    // java.nio.Bits.

    private Bytes()
    {
        // Don't allow instantiations of utility classes.
    }


    /**
     * Convert two bytes to a 16-bit integer value.
     *
     * @param pByte1    The most significant byte.
     * @param pByte0    The least significant byte.
     *
     * @return  The two bytes converted to a {@code short}.
     */
    static public short toShort(byte pByte1, byte pByte0)
    {
        return (short) ((pByte1 << 8) | (pByte0 & 0xff));
    }


    /**
     * Convert four bytes to a 32-bit integer value.
     *
     * @param pByte3    The most significant byte.
     * @param pByte2    The second most significant byte.
     * @param pByte1    The second least significant byte.
     * @param pByte0    The least significant byte.
     *
     * @return  The four bytes converted to an {@code int}.
     */
    static public int toInt(byte pByte3, byte pByte2, byte pByte1, byte pByte0)
    {
        return
            ( pByte3         << 24) |
            ((pByte2 & 0xff) << 16) |
            ((pByte1 & 0xff) <<  8) |
            ((pByte0 & 0xff));
    }


    /**
     * Convert eight bytes to a 64-bit integer value.
     *
     * @param pByte7    The most significant byte.
     * @param pByte6    The second most significant byte.
     * @param pByte5    The third most significant byte.
     * @param pByte4    The fourth most significant byte.
     * @param pByte3    The fourth least significant byte.
     * @param pByte2    The third least significant byte.
     * @param pByte1    The second least significant byte.
     * @param pByte0    The least significant byte.
     *
     * @return  The eight bytes converted to a {@code long}.
     */
    static public long toLong(
        byte pByte7,
        byte pByte6,
        byte pByte5,
        byte pByte4,
        byte pByte3,
        byte pByte2,
        byte pByte1,
        byte pByte0)
    {
        return
            (((long) pByte7)        << 56) |
            (((long) pByte6 & 0xff) << 48) |
            (((long) pByte5 & 0xff) << 40) |
            (((long) pByte4 & 0xff) << 32) |
            (((long) pByte3 & 0xff) << 24) |
            (((long) pByte2 & 0xff) << 16) |
            (((long) pByte1 & 0xff) <<  8) |
            (((long) pByte0 & 0xff));
    }


    /**
     * Get the most significant byte of a 16-bit integer value.
     *
     * @param pValue    The 16-bit integer value.
     *
     * @return  The most significant byte of {@code pValue}.
     */
    static public byte shortByte1(short pValue)
    {
        return (byte) (pValue >> 8);
    }


    /**
     * Get the least significant byte of a 16-bit integer value.
     *
     * @param pValue    The 16-bit integer value.
     *
     * @return  The least significant byte of {@code pValue}.
     */
    static public byte shortByte0(short pValue)
    {
        return (byte) pValue;
    }


    /**
     * Get the most significant byte of a 32-bit integer value.
     *
     * @param pValue    The 32-bit integer value.
     *
     * @return  The most significant byte of {@code pValue}.
     */
    static public byte intByte3(int pValue)
    {
        return (byte) (pValue >> 24);
    }


    /**
     * Get the second most significant byte of a 32-bit integer value.
     *
     * @param pValue    The 32-bit integer value.
     *
     * @return  The second most significant byte of {@code pValue}.
     */
    static public byte intByte2(int pValue)
    {
        return (byte) (pValue >> 16);
    }


    /**
     * Get the second least significant byte of a 32-bit integer value.
     *
     * @param pValue    The 32-bit integer value.
     *
     * @return  The second least significant byte of {@code pValue}.
     */
    static public byte intByte1(int pValue)
    {
        return (byte) (pValue >> 8);
    }


    /**
     * Get the least significant byte of a 32-bit integer value.
     *
     * @param pValue    The 32-bit integer value.
     *
     * @return  The least significant byte of {@code pValue}.
     */
    static public byte intByte0(int pValue)
    {
        return (byte) pValue;
    }


    /**
     * Get the most significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The most significant byte of {@code pValue}.
     */
    static public byte longByte7(long pValue)
    {
        return (byte) (pValue >> 56);
    }


    /**
     * Get the second most significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The second most significant byte of {@code pValue}.
     */
    static public byte longByte6(long pValue)
    {
        return (byte) (pValue >> 48);
    }


    /**
     * Get the third most significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The third most significant byte of {@code pValue}.
     */
    static public byte longByte5(long pValue)
    {
        return (byte) (pValue >> 40);
    }


    /**
     * Get the fourth most significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The fourth most significant byte of {@code pValue}.
     */
    static public byte longByte4(long pValue)
    {
        return (byte) (pValue >> 32);
    }


    /**
     * Get the fourth least significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The fourth least significant byte of {@code pValue}.
     */
    static public byte longByte3(long pValue)
    {
        return (byte) (pValue >> 24);
    }


    /**
     * Get the third least significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The third least significant byte of {@code pValue}.
     */
    static public byte longByte2(long pValue)
    {
        return (byte) (pValue >> 16);
    }


    /**
     * Get the second least significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The second least significant byte of {@code pValue}.
     */
    static public byte longByte1(long pValue)
    {
        return (byte) (pValue >> 8);
    }


    /**
     * Get the least significant byte of a 64-bit integer value.
     *
     * @param pValue    The 64-bit integer value.
     *
     * @return  The least significant byte of {@code pValue}.
     */
    static public byte longByte0(long pValue)
    {
        return (byte) pValue;
    }
}
