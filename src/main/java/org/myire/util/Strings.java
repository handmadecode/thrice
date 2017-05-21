/*
 * Copyright 2006, 2008-2009, 2011 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import static java.util.Objects.requireNonNull;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.myire.annotation.Unreachable;


/**
 * A collection of utility methods for Strings.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Strings
{
    /**
     * Private constructor to disallow instantiations of this class.
     */
    @Unreachable
    private Strings()
    {
        // Empty constructor.
    }


    /**
     * Convert a sequence of bytes to a string of hexadecimal values.
     *
     * @param pBytes    A byte array with the bytes to convert.
     *
     * @return  The bytes as hex values.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     */
    @Nonnull
    static public String bytesToHexString(@Nonnull byte[] pBytes)
    {
        return bytesToHexString(pBytes, 0, pBytes.length);
    }


    /**
     * Convert a sequence of bytes to a string of hexadecimal values.
     *
     * @param pBytes    A byte array with the bytes to convert.
     * @param pOffset   The index of the first byte to get the hexadecimal value for.
     * @param pLength   The number of bytes to get the hexadecimal values for.
     *
     * @return  The bytes as hex values.
     *
     * @throws NullPointerException         if {@code pBytes} is null.
     * @throws IndexOutOfBoundsException    if {@code pOffset} is less than 0, or if
     *                                      {@code pOffset + pLength} is greater than the size of
     *                                      the array.
     * @throws NegativeArraySizeException   if {@code pLength} is negative.
     */
    @Nonnull
    static public String bytesToHexString(
        @Nonnull byte[] pBytes,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        if (pLength == 0)
            return "";

        // Worst case size is five chars (0xnn + separator) per byte.
        StringBuilder aBuffer = new StringBuilder(pLength * 5);

        // There is at least one byte, the first should not be preceded by a space.
        appendByteAsHex(aBuffer, pBytes[pOffset]);

        // All other bytes, if any, should be preceded by a space acting as separator.
        int aLastByte = pOffset + pLength;
        for (int i=pOffset+1; i<aLastByte; i++)
        {
            aBuffer.append(' ');
            appendByteAsHex(aBuffer, pBytes[i]);
        }

        return aBuffer.toString();
    }


    /**
     * Append a byte on the format &quot;0xnn&quot; to a {@code StringBuilder}.
     *
     * @param pBuilder  The {@code StringBuilder} to append the byte to.
     * @param pByte     The byte to append.
     *
     *  @throws NullPointerException if {@code pBuilder} is null.
     */
    static private void appendByteAsHex(@Nonnull StringBuilder pBuilder, byte pByte)
    {
        pBuilder.append('0').append('x');
        if (pByte >= 0x00 && pByte < 0x10)
            pBuilder.append('0');

        // Integer.toHexString() does an implicit widening from byte to int, where negative bytes
        // become negative ints, e.g. 0xff becomes 0xffffffff, which isn't the desired behaviour.
        // Mask out all bits except the lowest 8 to avoid this.
        pBuilder.append(Integer.toHexString(pByte & 0xff));
    }


    /**
     * Check if two strings are equal (ignoring case) or if they both are null.
     *
     * @param pString1  The first string to check.
     * @param pString2  The second string to check.
     *
     * @return  True if the condition holds, false if not.
     */
    static public boolean equalsIgnoreCaseOrBothNull(
        @CheckForNull String pString1,
        @CheckForNull String pString2)
    {
        return (pString1 == pString2) || (pString1 != null && pString1.equalsIgnoreCase(pString2));
    }


    /**
     * Check if a string is empty or contains only whitespace characters.
     *
     * @param pString   The string to check.
     *
     * @return  True if the string is null, empty or contains whitespace only, false in all other
     *          cases.
     */
    static public boolean isBlankOrEmpty(@CheckForNull String pString)
    {
        if (pString == null)
            return true;

        int aLength = pString.length();
        for (int i=0; i<aLength; i++)
            if (!Character.isWhitespace(pString.charAt(i)))
                return false;

        return true;
    }


    /**
     * Get the string representation of an object, falling back to a default string if the object is
     * null.
     *
     * @param pObject           The object to get the string representation of.
     * @param pDefaultString    The string to return if {@code pObject} is null.
     *
     * @return  If {@code pObject} is non-null, {@code pObject.toString()} is returned, otherwise
     *          {@code pDefaultString} is returned.
     *
     * @throws NullPointerException if both {@code pObject} and {@code pDefaultString} is null.
     */
    @Nonnull
    static public String asString(@CheckForNull Object pObject, @Nonnull String pDefaultString)
    {
        return pObject != null ? pObject.toString() : requireNonNull(pDefaultString);
    }
}
