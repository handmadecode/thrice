/*
 * Copyright 2006, 2008-2009, 2016 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Unit tests for {@link org.myire.util.Strings}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class StringsTest
{
    /**
     * The {@code bytesToHexString} method should return the expected string for bytes
     * {@code 0x00-0x0f}.
     */
    @Test
    public void bytesToHexStringReturnsCorrectStringForBytes00To0F()
    {
        // Given
        byte[] aBytes = new byte[16];
        for (byte i=0; i<aBytes.length; i++)
            aBytes[i] = i;

        // When
        String aHexString = Strings.bytesToHexString(aBytes);

        // Then
        assertEquals(
            "0x00 0x01 0x02 0x03 0x04 0x05 0x06 0x07 0x08 0x09 0x0a 0x0b 0x0c 0x0d 0x0e 0x0f",
            aHexString);
    }


    /**
     * The {@code bytesToHexString} method should return the expected string for bytes
     * {@code 0xf0-0xff}.
     */
    @Test
    public void bytesToHexStringReturnsCorrectStringForBytesF0ToFF()
    {
        // Given
        byte[] aBytes = new byte[16];
        for (int i=0; i<aBytes.length; i++)
            aBytes[i] = (byte) (0x000000ff - i);

        // When
        String aHexString = Strings.bytesToHexString(aBytes);

        // Then
        assertEquals(
            "0xff 0xfe 0xfd 0xfc 0xfb 0xfa 0xf9 0xf8 0xf7 0xf6 0xf5 0xf4 0xf3 0xf2 0xf1 0xf0",
            aHexString);
    }


    /**
     * The {@code bytesToHexString} method should return the expected string for a sequence of
     * non-consecutive bytes .
     */
    @Test
    public void bytesToHexStringReturnsCorrectStringForNonConsecutiveBytes()
    {
        // Given
        byte[] aBytes = {(byte) 0x35, (byte) 0x64, (byte) 0x99, (byte) 0x03, (byte) 0xbb};

        // When
        String aHexString = Strings.bytesToHexString(aBytes);

        // Then
        assertEquals("0x35 0x64 0x99 0x03 0xbb", aHexString);
    }


    /**
     * The {@code bytesToHexString} method should return the expected string for a subarray.
     */
    @Test
    public void bytesToHexStringReturnsCorrectStringForSubArray()
    {
        // Given
        byte[] aBytes = {(byte) 0x35, (byte) 0x64, (byte) 0x99, (byte) 0x03, (byte) 0xbb};

        // Then
        assertEquals("0x99 0x03 0xbb", Strings.bytesToHexString(aBytes, 2, 3));
        assertEquals("0x64 0x99", Strings.bytesToHexString(aBytes, 1, 2));
    }


    /**
     * The {@code bytesToHexString} method should return the expected string for an array of
     * size 1.
     */
    @Test
    public void bytesToHexStringReturnsCorrectStringForArrayOfLength1()
    {
        // Given
        byte[] aBytes = {(byte) 0x64};

        // Then
        assertEquals("0x64", Strings.bytesToHexString(aBytes));
    }


    /**
     * The {@code bytesToHexString} method should return the expected string for a subarray of
     * size 1.
     */
    @Test
    public void bytesToHexStringReturnsCorrectStringForSubArrayOfLength1()
    {
        // Given
        byte[] aBytes = {(byte) 0x35, (byte) 0x64, (byte) 0x99, (byte) 0x03, (byte) 0xbb};

        // Then
        assertEquals("0xbb", Strings.bytesToHexString(aBytes, 4, 1));
    }


    /**
     * The {@code bytesToHexString} method should return an empty string for an empty array.
     */
    @Test
    public void bytesToHexStringReturnsEmptyStringForEmptyArray()
    {
        // When
        assertEquals("", Strings.bytesToHexString(new byte[0]));
    }


    /**
     * The {@code bytesToHexString} method should return an empty string for a subarray of size 0.
     */
    @Test
    public void bytesToHexStringReturnsEmptyStringForEmptySubArray()
    {
        // When
        assertEquals("", Strings.bytesToHexString(new byte[20], 10, 0));
    }


    /**
     * The {@code bytesToHexString} method should throw a {@code NullPointerException} for a null
     * array.
     */
    @Test(expected=NullPointerException.class)
    public void bytesToHexStringThrowsForNullArray()
    {
        // When
        Strings.bytesToHexString(null);
    }


    /**
     * The {@code bytesToHexString} method should throw a {@code NullPointerException} for a null
     * subarray.
     */
    @Test(expected=NullPointerException.class)
    public void bytesToHexStringThrowsForNullSubArray()
    {
        // When
        Strings.bytesToHexString(null, 0, 1);
    }


    /**
     * The {@code bytesToHexString} method should throw an {@code IndexOutOfBoundsException} for a
     * negative offset.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void bytesToHexStringThrowsForNegativeOffset()
    {
        // When
        Strings.bytesToHexString(new byte[5], -1, 4);
    }


    /**
     * The {@code bytesToHexString} method should throw an {@code IndexOutOfBoundsException} when
     * the offset plus length is too large.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void bytesToHexStringThrowsForTooLargeLength()
    {
        // When
        Strings.bytesToHexString(new byte[32], 29, 4);
    }


    /**
     * The {@code bytesToHexString} method should throw a {@code NegativeArraySizeException} for a
     * negative length.
     */
    @Test(expected=NegativeArraySizeException.class)
    public void bytesToHexStringThrowsForNegativeLength()
    {
        // When
        Strings.bytesToHexString(new byte[256], 3, -5);
    }


    /**
     * The {@code equalsIgnoreCaseOrBothNull} method should return true when passed two null
     * strings.
     */
    @Test
    public void equalsIgnoreCaseOrBothNullReturnsTrueForNullStrings()
    {
        assertTrue(Strings.equalsIgnoreCaseOrBothNull(null, null));
    }


    /**
     * The {@code equalsIgnoreCaseOrBothNull} method should return false when passed one null string
     * and one non-null string.
     */
    @Test
    public void equalsIgnoreCaseOrBothNullReturnsFalseForOneNullString()
    {
        assertFalse(Strings.equalsIgnoreCaseOrBothNull("", null));
        assertFalse(Strings.equalsIgnoreCaseOrBothNull(null, "x"));
    }


    /**
     * The {@code equalsIgnoreCaseOrBothNull} method should return true when passed two equal
     * strings.
     */
    @Test
    public void equalsIgnoreCaseOrBothNullReturnsTrueForEqualStrings()
    {
        assertTrue(Strings.equalsIgnoreCaseOrBothNull("SuperTrouper", "SuperTrouper"));
    }


    /**
     * The {@code equalsIgnoreCaseOrBothNull} method should return true when passed two strings
     * that differ only in case.
     */
    @Test
    public void equalsIgnoreCaseOrBothNullReturnsTrueForCaseUnequalStrings()
    {
        assertTrue(Strings.equalsIgnoreCaseOrBothNull("SuperTrouper", "supeRtrOUper"));
    }


    /**
     * The {@code equalsIgnoreCaseOrBothNull} method should return false when passed two strings
     * that differ in more than case.
     */
    @Test
    public void equalsIgnoreCaseOrBothNullReturnsFalseForUnequalStrings()
    {
        assertFalse(Strings.equalsIgnoreCaseOrBothNull("SuperTrouper", "supeRtrOUpeer"));
    }


    /**
     * The {@code isBlankOrEmpty} method should return true for a null string.
     */
    @Test
    public void isBlankOrEmptyReturnsTrueForNullString()
    {
        assertTrue(Strings.isBlankOrEmpty(null));
    }


    /**
     * The {@code isBlankOrEmpty} method should return true for an empty string.
     */
    @Test
    public void isBlankOrEmptyReturnsTrueForEmptyString()
    {
        assertTrue(Strings.isBlankOrEmpty(""));
    }


    /**
     * The {@code isBlankOrEmpty} method should return true for a string containing only whitespace.
     */
    @Test
    public void isBlankOrEmptyReturnsTrueForWhitespaceOnly()
    {
        assertTrue(Strings.isBlankOrEmpty(" \t\r\f"));
    }


    /**
     * The {@code isBlankOrEmpty} method should return false for a string containing whitespace
     * followed by non-whitespace.
     */
    @Test
    public void isBlankOrEmptyReturnsFalseForWhitespaceAtBeginning()
    {
        assertFalse(Strings.isBlankOrEmpty(" \t\r\fx"));
    }


    /**
     * The {@code isBlankOrEmpty} method should return false for a string ending in whitespace.
     */
    @Test
    public void isBlankOrEmptyReturnsFalseForWhitespaceAtEnd()
    {
        assertFalse(Strings.isBlankOrEmpty("A \t "));
    }


    /**
     * The {@code isBlankOrEmpty} method should return false for a string starting and ending in
     * whitespace but having non-whitespace in the middle.
     */
    @Test
    public void isBlankOrEmptyReturnsFalseForNonWhitespaceInMiddle()
    {
        assertFalse(Strings.isBlankOrEmpty(" \t\r x y\t"));
    }


    /**
     * The {@code isBlankOrEmpty} method should return false for a string containing whitespace in
     * the middle but not at the beginning or the end.
     */
    @Test
    public void isBlankOrEmptyReturnsFalseForWhitespaceInMiddle()
    {
        assertFalse(Strings.isBlankOrEmpty("x \t\r x"));
    }


    /**
     * The {@code isBlankOrEmpty} method should return false for a string containing no whitespace
     * at all.
     */
    @Test
    public void isBlankOrEmptyReturnsFalseForStringWithoutWhitespace()
    {
        assertFalse(Strings.isBlankOrEmpty("xyzzy"));
    }


    /**
     * The {@code asString} method should return the default value when passed a null object.
     */
    @Test
    public void asStringReturnsDefaultValueForNullObject()
    {
        // Given
        String aDefault = "When everything else fails";

        // Then
        assertEquals(aDefault, Strings.asString(null, aDefault));
    }


    /**
     * The {@code asString} method should return the object's string representation when passed a
     * non-null object.
     */
    @Test
    public void asStringReturnsObjectStringForNonnullObject()
    {
        // Given
        Integer aObject = 4711;

        // Then
        assertEquals(aObject.toString(), Strings.asString(aObject, "Should not be returned"));
    }


    /**
     * The {@code asString} method should throw a {@code NullPointerException} when passed both a
     * null object and a null default value.
     */
    @Test(expected=NullPointerException.class)
    public void asStringThrowsForNullDefaultValue()
    {
        Strings.asString(null, null);
    }
}
