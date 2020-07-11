/*
 * Copyright 2016, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Base class for unit tests of {@link org.myire.util.HashComposer} implementations.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
abstract public class HashComposerTest
{
    /**
     * Get the {@code HashComposer} implementation to test.
     *
     * @return  The implementation to test.
     */
    abstract protected HashComposer getInstance();


    /**
     * The {@code initialValue()} method should return the same value on multiple invocations.
     */
    @Test
    public void initialValueIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();

        // Then
        assertEquals(
            aHashComposer.initialValue(),
            aHashComposer.initialValue());
    }


    /**
     * The {@code update(boolean)} method should return the same value on multiple invocations with
     * the same hash code and boolean value.
     */
    @Test
    public void updateBooleanIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();

        // Then
        assertAll(
            () ->
                assertEquals(
                    aHashComposer.update(aHashCode, false),
                    aHashComposer.update(aHashCode, false)),
            () ->
                assertEquals(
                    aHashComposer.update(aHashCode, true),
                    aHashComposer.update(aHashCode, true))
        );
    }


    /**
     * The {@code update(byte)} method should return the same value on multiple invocations with the
     * same hash code and byte value.
     */
    @Test
    public void updateByteIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        byte[] aValues = rndBytes(16);

        // Then
        for (byte aValue : aValues)
            assertEquals(
                    aHashComposer.update(aHashCode, aValue),
                    aHashComposer.update(aHashCode, aValue),
                    String.valueOf(aValue));
    }


    /**
     * The {@code update(short)} method should return the same value on multiple invocations with
     * the same hash code and short value.
     */
    @Test
    public void updateShortIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        short[] aValues = rndShorts(16);

        // Then
        for (short aValue : aValues)
            assertEquals(
                    aHashComposer.update(aHashCode, aValue),
                    aHashComposer.update(aHashCode, aValue),
                    String.valueOf(aValue));
    }


    /**
     * The {@code update(char)} method should return the same value on multiple invocations with the
     * same hash code and char value.
     */
    @Test
    public void updateCharIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        char[] aValues = rndChars(16);

        // Then
        for (char aValue : aValues)
            assertEquals(
                    aHashComposer.update(aHashCode, aValue),
                    aHashComposer.update(aHashCode, aValue),
                    String.valueOf(aValue));
    }


    /**
     * The {@code update(int)} method should return the same value on multiple invocations with the
     * same hash code and int value.
     */
    @Test
    public void updateIntIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        int[] aValues = rndInts(16);

        // Then
        for (int aValue : aValues)
            assertEquals(
                    aHashComposer.update(aHashCode, aValue),
                    aHashComposer.update(aHashCode, aValue),
                    String.valueOf(aValue));
    }


    /**
     * The {@code update(long)} method should return the same value on multiple invocations with the
     * same hash code and long value.
     */
    @Test
    public void updateLongIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        long[] aValues = rndLongs(16);

        // Then
        for (long aValue : aValues)
            assertEquals(
                    aHashComposer.update(aHashCode, aValue),
                    aHashComposer.update(aHashCode, aValue),
                    String.valueOf(aValue));
    }


    /**
     * The {@code update(float)} method should return the same value on multiple invocations with
     * the same hash code and float value.
     */
    @Test
    public void updateFloatIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        float[] aValues = rndFloats(16);

        // Then
        for (float aValue : aValues)
            assertEquals(
                    aHashComposer.update(aHashCode, aValue),
                    aHashComposer.update(aHashCode, aValue),
                    String.valueOf(aValue));
    }


    /**
     * The {@code update(double)} method should return the same value on multiple invocations with
     * the same hash code and double value.
     */
    @Test
    public void updateDoubleIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        double[] aValues = rndDoubles(16);

        // Then
        for (double aValue : aValues)
            assertEquals(
                    aHashComposer.update(aHashCode, aValue),
                    aHashComposer.update(aHashCode, aValue),
                    String.valueOf(aValue));
    }


    /**
     * The {@code update(Object)} method should return the same value on multiple invocations with
     * the same hash code and Object.
     */
    @Test
    public void updateObjectIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();

        // Then
        Object aObject = new Object();
        assertEquals(aHashComposer.update(aHashCode, aObject), aHashComposer.update(aHashCode, aObject));
        aObject = null;
        assertEquals(aHashComposer.update(aHashCode, aObject), aHashComposer.update(aHashCode, aObject));
    }


    /**
     * The {@code update(boolean[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateBooleanArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        boolean[] aValue = {true, false, false, true, false};

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(byte[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateByteArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        byte[] aValue = rndBytes(32);

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(short[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateShortArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        short[] aValue = rndShorts(32);

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(char[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateCharArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        char[] aValue = rndChars(32);

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(int[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateIntArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        int[] aValue = rndInts(32);

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(long[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateLongArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        long[] aValue = rndLongs(32);

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(float[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateFloatArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        float[] aValue = rndFloats(32);

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(double[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateDoubleArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        double[] aValue = rndDoubles(32);

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    /**
     * The {@code update(Object[])} method should return the same value on multiple invocations with
     * the same hash code and array.
     */
    @Test
    public void updateObjectArrayIsInvariant()
    {
        // Given
        HashComposer aHashComposer = getInstance();
        int aHashCode = rndHashCode();
        String[] aValue = {"", null, "waist", "waste"};

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));

        // Given
        aValue = null;

        // Then
        assertEquals(aHashComposer.update(aHashCode, aValue), aHashComposer.update(aHashCode, aValue));
    }


    static private int rndHashCode()
    {
        return ThreadLocalRandom.current().nextInt();
    }


    static private byte[] rndBytes(int pSize)
    {
        byte[] aValues = new byte[pSize];
        ThreadLocalRandom.current().nextBytes(aValues);
        return aValues;
    }


    static private short[] rndShorts(int pSize)
    {
        short[] aValues = new short[pSize];
        for (int i=0; i<pSize; i++)
            aValues[i] = (short) (ThreadLocalRandom.current().nextInt(1 << 16));

        return aValues;
    }


    static private char[] rndChars(int pSize)
    {
        char[] aValues = new char[pSize];
        for (int i=0; i<pSize; i++)
            aValues[i] = (char) ThreadLocalRandom.current().nextInt(1 << 16);

        return aValues;
    }


    static private int[] rndInts(int pSize)
    {
        int[] aValues = new int[pSize];
        for (int i=0; i<pSize; i++)
            aValues[i] = ThreadLocalRandom.current().nextInt();

        return aValues;
    }


    static private long[] rndLongs(int pSize)
    {
        long[] aValues = new long[pSize];
        for (int i=0; i<pSize; i++)
            aValues[i] = ThreadLocalRandom.current().nextLong();

        return aValues;
    }


    static private float[] rndFloats(int pSize)
    {
        float[] aValues = new float[pSize];
        for (int i=0; i<pSize; i++)
            aValues[i] = ThreadLocalRandom.current().nextFloat();

        return aValues;
    }


    static private double[] rndDoubles(int pSize)
    {
        double[] aValues = new double[pSize];
        for (int i=0; i<pSize; i++)
            aValues[i] = ThreadLocalRandom.current().nextDouble();

        return aValues;
    }
}
