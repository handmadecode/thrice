/*
 * Copyright 2011, 2016, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Unit tests for {@link org.myire.util.Ascii}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class AsciiTest
{
    /**
     * Test the {@code isAscii(byte)} method with all {@code byte} values.
     */
    @Test
    public void testIsAsciiByte()
    {
        for (int b=0; b<=0x7f; b++)
            if (!Ascii.isAscii((byte) b))
                fail("isAscii returns false for 0x"+ Integer.toHexString(b));

        for (int b=0x80; b<=0xff; b++)
            if (Ascii.isAscii((byte) b))
                fail("isAscii returns true for 0x"+ Integer.toHexString(b));
    }


    /**
     * Test the {@code isAscii(char)} method with all {@code char} values.
     */
    @Test
    public void testIsAsciiChar()
    {
        for (int c=0; c<=0x007f; c++)
            if (!Ascii.isAscii((char) c))
                fail("isAscii returns false for 0x"+ Integer.toHexString(c));

        for (int c=0x0080; c<=0xffff; c++)
            if (Ascii.isAscii((char) c))
                fail("isAscii returns true for 0x"+ Integer.toHexString(c));
    }


    /**
     * Test the {@code isAsciiAZ(byte)} method with all {@code byte} values.
     */
    @Test
    public void testIsAsciiAZByte()
    {
        for (int b=0; b<'A'; b++)
            if (Ascii.isAsciiAZ((byte) b))
                fail("isAsciiAZ returns true for 0x"+ Integer.toHexString(b));

        for (int b='A'; b<='Z'; b++)
            if (!Ascii.isAsciiAZ((byte) b))
                fail("isAsciiAZ returns false for 0x"+ Integer.toHexString(b));

        for (int b='Z'+1; b<=0xff; b++)
            if (Ascii.isAsciiAZ((byte) b))
                fail("isAsciiAZ returns true for 0x"+ Integer.toHexString(b));
    }


    /**
     * Test the {@code isAsciiAZ(char)} method with all {@code char} values.
     */
    @Test
    public void testIsAsciiAZChar()
    {
        for (int c=0; c<'A'; c++)
            if (Ascii.isAsciiAZ((char) c))
                fail("isAsciiAZ returns true for 0x"+ Integer.toHexString(c));

        for (int c='A'; c<='Z'; c++)
            if (!Ascii.isAsciiAZ((char) c))
                fail("isAsciiAZ returns false for 0x"+ Integer.toHexString(c));

        for (int c='Z'+1; c<=0xffff; c++)
            if (Ascii.isAsciiAZ((char) c))
                fail("isAsciiAZ returns true for 0x"+ Integer.toHexString(c));
    }


    /**
     * Test the {@code isAsciiaz(byte)} method with all {@code byte} values.
     */
    @Test
    public void testIsAsciiazByte()
    {
        for (int b=0; b<'a'; b++)
            if (Ascii.isAsciiaz((byte) b))
                fail("isAsciiaz returns true for 0x"+ Integer.toHexString(b));

        for (int b='a'; b<='z'; b++)
            if (!Ascii.isAsciiaz((byte) b))
                fail("isAsciiaz returns false for 0x"+ Integer.toHexString(b));

        for (int b='z'+1; b<=0xff; b++)
            if (Ascii.isAsciiaz((byte) b))
                fail("isAsciiaz returns true for 0x"+ Integer.toHexString(b));
   }


    /**
     * Test the {@code isAsciiaz(char)} method with all {@code char} values.
     */
    @Test
    public void testIsAsciiazChar()
    {
        for (int c=0; c<'a'; c++)
            if (Ascii.isAsciiaz((char) c))
                fail("isAsciiaz returns true for 0x"+ Integer.toHexString(c));

        for (int c='a'; c<='z'; c++)
            if (!Ascii.isAsciiaz((char) c))
                fail("isAsciiaz returns false for 0x"+ Integer.toHexString(c));

        for (int c='z'+1; c<=0xffff; c++)
            if (Ascii.isAsciiaz((char) c))
                fail("isAsciiaz returns true for 0x"+ Integer.toHexString(c));
    }


    /**
     * Test the {@code isAsciiAZaz(byte)} method with all {@code byte} values.
     */
    @Test
    public void testIsAsciiAZazByte()
    {
        for (int b=0; b<'A'; b++)
            if (Ascii.isAsciiAZaz((byte) b))
                fail("isAsciiAZaz returns true for 0x"+ Integer.toHexString(b));

        for (int b='A'; b<='Z'; b++)
            if (!Ascii.isAsciiAZaz((byte) b))
                fail("isAsciiAZaz returns false for 0x"+ Integer.toHexString(b));

        for (int b='Z'+1; b<'a'; b++)
            if (Ascii.isAsciiAZaz((byte) b))
                fail("isAsciiAZaz returns true for 0x"+ Integer.toHexString(b));

        for (int b='a'; b<='z'; b++)
            if (!Ascii.isAsciiAZaz((byte) b))
                fail("isAsciiAZaz returns false for 0x"+ Integer.toHexString(b));

        for (int b='z'+1; b<=0xff; b++)
            if (Ascii.isAsciiAZaz((byte) b))
                fail("isAsciiAZaz returns true for 0x"+ Integer.toHexString(b));
    }


    /**
     * Test the {@code isAsciiAZaz(char)} method with all {@code char} values.
     */
    @Test
    public void testIsAsciiAZazChar()
    {
        for (int c=0; c<'A'; c++)
            if (Ascii.isAsciiAZaz((char) c))
                fail("isAsciiAZaz returns true for 0x"+ Integer.toHexString(c));

        for (int c='A'; c<='Z'; c++)
            if (!Ascii.isAsciiAZaz((char) c))
                fail("isAsciiAZaz returns false for 0x"+ Integer.toHexString(c));

        for (int c='Z'+1; c<'a'; c++)
            if (Ascii.isAsciiAZaz((char) c))
                fail("isAsciiAZaz returns true for 0x"+ Integer.toHexString(c));

        for (int c='a'; c<='z'; c++)
            if (!Ascii.isAsciiAZaz((char) c))
                fail("isAsciiAZaz returns false for 0x"+ Integer.toHexString(c));

        for (int c='z'+1; c<=0xffff; c++)
            if (Ascii.isAsciiAZaz((char) c))
                fail("isAsciiAZaz returns true for 0x"+ Integer.toHexString(c));
   }


    /**
     * Test the {@code isAsciiDigit(byte)} method {@code byte} values.
     */
    @Test
    public void testIsAsciiDigitByte()
    {
        for (int b=0; b<'0'; b++)
            if (Ascii.isAsciiDigit((byte) b))
                fail("isAsciiDigit returns true for 0x"+ Integer.toHexString(b));

        for (int b='0'; b<='9'; b++)
            if (!Ascii.isAsciiDigit((byte) b))
                fail("isAsciiDigit returns false for 0x"+ Integer.toHexString(b));

        for (int b='9'+1; b<=0xff; b++)
            if (Ascii.isAsciiDigit((byte) b))
                fail("isAsciiDigit returns true for 0x"+ Integer.toHexString(b));
    }


    /**
     * Test the {@code isAsciiDigit(char)} method with all {@code char} values.
     */
    @Test
    public void testIsAsciiDigitChar()
    {
        for (int c=0; c<'0'; c++)
            if (Ascii.isAsciiDigit((char) c))
                fail("isAsciiDigit returns true for 0x"+ Integer.toHexString(c));

        for (int c='0'; c<='9'; c++)
            if (!Ascii.isAsciiDigit((char) c))
                fail("isAsciiDigit returns false for 0x"+ Integer.toHexString(c));

        for (int c='9'+1; c<=0xffff; c++)
            if (Ascii.isAsciiDigit((char) c))
                fail("isAsciiDigit returns true for 0x"+ Integer.toHexString(c));
    }
}
