/*
 * Copyright 2011, 2016, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.myire.annotation.Unreachable;


/**
 * Utility methods for checking if a {@code byte} or a {@code char} is within the range of ASCII
 * characters ({@code 0x00-0x7F}} or a subrange thereof.
 *<p>
 * The implementations are based on bitwise logic and shift operations. This may or may not be more
 * efficient than range endpoint comparisons, depending on the execution context. Benchmarks show
 * that especially single range comparisons, e.g. {@code b>= 0x30 && b<=0x39} perform better than
 * {@link #isAsciiDigit(byte)}) on some combinations of hardware, OS, and JVM, whereas the bit
 * manipulation variant performs better in other runtime environments.
 *<p>
 * Before using the methods in this class for performance reasons it is recommended to measure if
 * they are more efficient in the targeted runtime environment (including the code from where
 * they will be called) than simple range comparison checks.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Ascii
{
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Ascii()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Check if a {@code byte} is in the ASCII range {@code 0x00-0x7F}.
     *
     * @param pByte The {@code byte} to check.
     *
     * @return  True if {@code pByte} is in the range {@code 0x00-0x7F}, false otherwise.
     */
    static public boolean isAscii(byte pByte)
    {
        // Only the 7 lowest bits may be set. Bytes with the high bit set are negative and will have
        // the sign preserved when extended to an int in the bitwise operation (JLS 5.1.2), meaning
        // that 0x80 is extended to 0xffffff80. This has no effect on the bitmask used below, since
        // it is the high bit in the least significant byte that is of interest, and that bit will
        // be set irrespective of sign preservation.
        return (pByte & 0x80) == 0;
    }


    /**
     * Check if a {@code char} is an ASCII character.
     *
     * @param pChar The {@code char} to check.
     *
     * @return  True if {@code pChar} is in the range {@code 0x00-0x7F}, false otherwise.
     */
    static public boolean isAscii(char pChar)
    {
        // Only the 7 lowest bits may be set.
        // A char is widened to an int by zero-extending the char value (JLS 5.1.2).
        return (pChar & 0xff80) == 0;
    }


    /**
     * Check if a {@code byte} is in the ASCII range A-Z ({@code 0x41-0x5A}).
     *
     * @param pByte The {@code byte} to check.
     *
     * @return  True if {@code pByte} is in the range {@code 0x41-0x5A}, false otherwise.
     */
    static public boolean isAsciiAZ(byte pByte)
    {
        // The three most significant bits must be 010, otherwise the high nibble cannot be
        // 0x4 or 0x5 (0100 or 0101).
        return (pByte & 0xe0) == 0x40
               &&
               // Check if the 5 least significant bits are correct. They must be in the range
               // 00001 - 11010  (0x41 - 0x5a), i.e. have a value between 0x01 and 0x1a (1 and 26).
               // This is checked by setting the bit corresponding to the value of these 5 bits in a
               // 32-bit value, and masking it with a 32-bit value where bits 1-26 are set
               // (0x07fffffe).
               // Example: if the 5 bits are 10010 (18) the bitwise operation is
               // 0x40000 & 0x07fffffe
               // which is non-zero, indicating that the value is in the range.
               // The value 0x5b has the value 0x1b (27) in the low 5 bits, 1 << 27 == 0x8000000,
               // 0x08000000 & 0x07fffffe == 0, i.e. the value is not in the range.
               ((1 << (pByte & 0x1f)) & 0x07fffffe) != 0;
    }


    /**
     * Check if a {@code char} is in the ASCII range A-Z ({@code 0x41-0x5A}).
     *
     * @param pChar The {@code char} to check.
     *
     * @return  True if {@code pChar} is in the range {@code 0x41-0x5A}, false otherwise.
     */
    static public boolean isAsciiAZ(char pChar)
    {
        // The high byte must be 0 and the three most significant bits in the low byte must be 010,
        // otherwise the high nibble in the low byte cannot be 0x4 or 0x5 (0100 or 0101).
        return (pChar & 0xffe0) == 0x40
               &&
               // Check if the 5 least significant bits in the low byte are correct, see
               // isAsciiAZ(byte) for an explanation of this bit operation.
               ((1 << (pChar & 0x1f)) & 0x07fffffe) != 0;
    }


    /**
     * Check if a {@code byte} is in the ASCII range a-z ({@code 0x61-0x7A}).
     *
     * @param pByte The {@code byte} to check.
     *
     * @return  True if {@code pByte} is in the range {@code 0x61-0x7A}, false otherwise.
     */
    static public boolean isAsciiaz(byte pByte)
    {
        // If the three most significant bits aren't 011 the high nibble cannot be 0x6 or 0x7 (0110
        // or 0111).
        return (pByte & 0xe0) == 0x60
               &&
               // Check if the 5 least significant bits are correct. They must have the same pattern
               // as in the isAsciiAZ(byte), and the check is therefore the same as in that method.
               ((1 << (pByte & 0x1f)) & 0x07fffffe) != 0;
    }


    /**
     * Check if a {@code char} is in the ASCII range a-z ({@code 0x61-0x7A}).
     *
     * @param pChar The {@code char} to check.
     *
     * @return  True if {@code pChar} is in the range {@code 0x61-0x7A}, false otherwise.
     */
    static public boolean isAsciiaz(char pChar)
    {
        // The high byte must be 0 and the three most significant bits in the low byte must be 011,
        // otherwise the high nibble in the low byte cannot be 0x6 or 0x7 (0110 or 0111).
        return (pChar & 0xffe0) == 0x60
               &&
               // Check if the 5 least significant bits are correct, see isAsciiAZ(byte) for an
               // explanation of this bit operation.
               ((1 << (pChar & 0x1f)) & 0x07fffffe) != 0;
    }


    /**
     * Check if a {@code byte} is in the ASCII range A-Z ({@code 0x41-0x5A}) or a-z
     * ({@code 0x61-0x7A}).
     *
     * @param pByte The {@code byte} to check.
     *
     * @return  True if {@code pByte} is in the range {@code 0x41-0x5A} or in the range
     *          {@code 0x61-0x7A}, false otherwise.
     */
    static public boolean isAsciiAZaz(byte pByte)
    {
        // If the two most significant bits aren't 01 the high nibble cannot be 0x4, 0x5, 0x6 or
        // 0x7 (0100, 0101, 0110 or 0111).
        return (pByte & 0xc0) == 0x40
               &&
               // Check if the 6 least significant bits are correct. They must be in the range
               // 000001 - 011010 (0x41 - 0x5a) or in the range 100001 - 111010 (0x61 - 0x7a), i.e.
               // have a value between 0x01 and 0x1a (1 and 26) or between 0x21 and 0x3a (33 and
               // 58). This is checked by setting the bit corresponding to the value of these 6 bits
               // in a 64-bit value, and masking it with a 64-bit value where bits 1-26 and 33-58
               // are set (0x07fffffe07fffffe).
               // Example: if the 6 bits are 10010 (18) the bitwise operation is
               // 0x40000 & 0x07fffffe07fffffe
               // which is non-zero, indicating that the value is in the range.
               // The value 0x7b has the value 0x3b (59) in the low 6 bits,
               // 1 << 59 == 0x800000000000000
               // 0x080000000 & 0x07fffffe07fffffe == 0, i.e. the value is not in the range.
               ((1L << (pByte & 0x3f)) & 0x07fffffe07fffffeL) != 0;
    }


    /**
     * Check if a {@code char} is in the ASCII range A-Z ({@code 0x41-0x5A}) or a-z
     * ({@code 0x61-0x7A}).
     *
     * @param pChar The {@code char} to check.
     *
     * @return  True if {@code pChar} is in the range {@code 0x41-0x5A} or in the range
     *          {@code 0x61-0x7A}, false otherwise.
     */
    static public boolean isAsciiAZaz(char pChar)
    {
        // The high byte must be 0 and the two most significant bits in the low byte must be 01,
        // otherwise the high nibble in the low byte cannot be  0x4, 0x5, 0x6 or 0x7 (0100, 0101,
        // 0110 or 0111).
        return (pChar & 0xffc0) == 0x40
               &&
               // Check if the 6 least significant bits are correct, see isAsciiAZaz(byte) for an
               // explanation of this bit operation.
               ((1L << (pChar & 0x3f)) & 0x07fffffe07fffffeL) != 0;
    }


    /**
     * Check if a {@code byte} is in the ASCII range 0-9 ({@code 0x30-0x39}).
     *
     * @param pByte The {@code byte} to check.
     *
     * @return  True if {@code pByte} is in the range {@code 0x30-0x39}, false otherwise.
     */
    static public boolean isAsciiDigit(byte pByte)
    {
        // If the high nibble isn't 0011 the byte cannot be in the range 0x30-0x39
        // (00110000 - 00111001).
        return (pByte & 0xf0) == 0x30
               &&
               // Check if the 4 least significant bits are correct. They must be in the range
               // 0000 - 1001 (0x30 - 0x39), i.e. have a value between 0x00 and 0x09 (0 and 9).
               // This is checked by setting the bit corresponding to the value of these 4 bits in a
               // 32-bit value, and masking it with a 32-bit value where bits 0-9 are set
               // (0x000003ff).
               // Example: if the 5 bits are 0010 (2) the bitwise operation is
               // 0x4 & 0x000003ff
               // which is non-zero, indicating that the value is in the range.
               // The value 0x3c has the value 0x0c (12) in the low 4 bits, 1 << 12 == 0x1000,
               // 0x1000 & 0x000003ff == 0, i.e. the value is not in the range.
               ((1 << (pByte & 0x0f)) & 0x000003ff) != 0;
    }


    /**
     * Check if a {@code char} is in the ASCII range 0-9 ({@code 0x30-0x39}).
     *
     * @param pChar The {@code char} to check.
     *
     * @return  True if {@code pChar} is in the range {@code 0x30-0x39}, false otherwise.
     */
    static public boolean isAsciiDigit(char pChar)
    {
        // The high byte must be 0 and the  high nibble in the low byte must be 0011, otherwise the
        // char cannot be in the range 0x30-0x39 (00110000 - 00111001).
        return (pChar & 0xfff0) == 0x30
               &&
               // Check if the 4 least significant bits are correct, see isAsciiDigit(byte) for an
               // explanation of this bit operation.
               ((1 << (pChar & 0x0f)) & 0x000003ff) != 0;
    }


    /**
     * Check if a {@code byte} is in the ASCII range 0-9 ({@code 0x30-0x39}), A-F
     * ({@code 0x41-0x46}), or a-f ({@code 0x61-0x66}).
     *
     * @param pByte The {@code byte} to check.
     *
     * @return  True if {@code pByte} is in one of the ranges {@code 0x30-0x39}, {@code 0x41-0x46}
     *          or {@code 0x61-0x66}, false otherwise.
     */
    static public boolean isAsciiHexDigit(byte pByte)
    {
        return
            (
                // Check for 0x30-0x39, see isAsciiDigits(byte)
                (pByte & 0xf0) == 0x30
                &&
                ((1 << (pByte & 0x0f)) & 0x000003ff) != 0
            )
            ||
            (
                // Check for 0x41-0x46 or 0x61-0x66.
                // The high nibble must be either 0x4 or 0x6, i.e. have the pattern 01?0, and the
                // low nibble must be in the range 0x1-0x6, i.e. have the pattern 0???, where ???
                // cannot be 111 or 000.
                (pByte & 0xd8) == 0x40
                &&
                // The five most significant bits are correct, the remaining three must be any value
                // but 0 or 7. Set the bit corresponding to the value of these three bits in a
                // 32-bit value and bitwise AND it with a 32-bit mask where bits 1-6 are set
                // (0x0000007e). If the result is non-zero, the three bits have a valid value.
                // Example 1: E (0x45) has the three least significant bits 101, shifting 1 left
                // by 5 will set bit 5 (0x20) which ANDed with 0x7e is non-zero.
                // Example 2: G (0x47) has the three least significant bits 111, shifting 1 left
                // by 7 will set bit 7 (0x80) which ANDed with 0x7e is zero.
                ((1 << (pByte & 0x07)) & 0x0000007e) != 0
            );
    }


    /**
     * Check if a {@code char} is in the ASCII range 0-9 ({@code 0x30-0x39}), A-F
     * ({@code 0x41-0x46}), or a-f ({@code 0x61-0x66}).
     *
     * @param pChar The {@code char} to check.
     *
     * @return  True if {@code pChar} is in one of the ranges {@code 0x30-0x39}, {@code 0x41-0x46}
     *          or {@code 0x61-0x66}, false otherwise.
     */
    static public boolean isAsciiHexDigit(char pChar)
    {
        return
            (
                // Check for 0x30-0x39, see isAsciiDigits(char)
                (pChar & 0xfff0) == 0x30
                &&
                ((1 << (pChar & 0x0f)) & 0x000003ff) != 0
            )
            ||
            (
                // Check for 0x41-0x46 or 0x61-0x66, se isAsciiHexDigits(byte)
                (pChar & 0xffd8) == 0x40
                &&
                ((1 << (pChar & 0x07)) & 0x0000007e) != 0
            );
    }
}
