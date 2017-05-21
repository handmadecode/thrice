/*
 * Copyright 2009, 2011, 2014, 2016-2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.Arrays;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;


/**
 * A hash composer that updates a hash code by multiplying the intermediate value with a multiplier
 * (traditionally a prime number other than {@code 2} is used) and adding the hash code of the new
 * value.
 *<p>
 * The hash function used for primitive values is the {@code hashCode} method in the corresponding
 * JDK class, e.g. {@code Long.HashCode(long)}. For objects it is the {@code hashCode()} method
 * inherited from {@code java.lang.Object}, and for arrays the {@code java.util.Arrays::hashCode}
 * methods are used.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@Immutable
public class MultiplyAddHashComposer implements HashComposer
{
    /**
     * Instance using the value {@code 31} as multiplier. This value is widely used throughout the
     * JDK when calculating hash codes using the multiply and add method. A multiplication with 31
     * can be replaced by a shift and a subtraction: {@code 31 * x == (x << 5) - x}.
     */
    static public final MultiplyAddHashComposer FACTOR31 = new MultiplyAddHashComposer(31);


    private final int fMultiplier;


    /**
     * Create a new {@code MultiplyAddHashComposer}.
     *
     * @param pMultiplier   The value to multiply the current hash code with when updating.
     */
    public MultiplyAddHashComposer(int pMultiplier)
    {
        fMultiplier = pMultiplier;
    }


    @Override
    public int initialValue()
    {
        return 1;
    }


    @Override
    public int update(int pHashCode, boolean pValue)
    {
        return fMultiplier * pHashCode + Boolean.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, byte pValue)
    {
        return fMultiplier * pHashCode + Byte.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, short pValue)
    {
        return fMultiplier * pHashCode + Short.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, char pValue)
    {
        return fMultiplier * pHashCode + Character.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, int pValue)
    {
        return fMultiplier * pHashCode + Integer.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, long pValue)
    {
        return fMultiplier * pHashCode + Long.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, float pValue)
    {
        return fMultiplier * pHashCode + Float.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, double pValue)
    {
        return fMultiplier * pHashCode + Double.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable Object pObject)
    {
        return fMultiplier * pHashCode + Objects.hashCode(pObject);
    }


    @Override
    public int update(int pHashCode, @Nullable boolean[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable byte[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable short[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable char[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable int[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable long[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable float[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    @Override
    public int update(int pHashCode, @Nullable double[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }


    /**
     * Update a hash code with an array of objects. This method uses
     * {code java.util.Arrays.hashCode(Object[])} to get the hash code for the array, meaning that
     * the same considerations for an array of arrays apply as described in the documentation for
     * that method.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    @Override
    public int update(int pHashCode, @Nullable Object[] pValue)
    {
        return fMultiplier * pHashCode + Arrays.hashCode(pValue);
    }
}
