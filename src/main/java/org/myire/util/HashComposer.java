/*
 * Copyright 2016-2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nullable;


/**
 * A {@code HashComposer} calculates a 32-bit hash code for a combination of values. This is done by
 * repeatedly updating a 32-bit value with the hash code of a primitive value or object reference
 * and returning the updated hash code. The hash function used to calculate the hash code for the
 * individual values is part of the implementation, as is the algorithm used to update the
 * intermediate value with a hash code.
 *<p>
 * A {@code HashComposer} is stateless, meaning that the intermediate value that is updated during a
 * calculation must be stored by the caller. This allows implementations to be immutable and
 * allocation free.
 *<p>
 * The typical use case is calculating the hash code for an object from its fields. An example:
 *<pre>
 * class Example
 * {
 *   private int f1;
 *   private String f2;
 *   private double[] f3;
 *   ...
 *
 *   public int hashCode()
 *   {
 *       HashComposer c = ...
 *       int h = c.initialValue();
 *       h = c.update(h, f1);
 *       h = c.update(h, f2);
 *       h = c.update(h, f3);
 *       return h;
 *   }
 * }
 *</pre>
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public interface HashComposer
{
    /**
     * Get a 32-bit value suitable to pass to the first {@code update} call.
     *
     * @return  A suitable initial value for this implementation.
     */
    int initialValue();

    /**
     * Update a hash code with a boolean value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, boolean pValue);

    /**
     * Update a hash code with a byte value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, byte pValue);

    /**
     * Update a hash code with a short value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, short pValue);

    /**
     * Update a hash code with a char value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, char pValue);

    /**
     * Update a hash code with an int value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, int pValue);

    /**
     * Update a hash code with a long value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, long pValue);

    /**
     * Update a hash code with a float value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, float pValue);

    /**
     * Update a hash code value with a double value.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The value to update the hash code with.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, double pValue);

    /**
     * Update a hash code with an object.
     *
     * @param pHashCode The hash code to update.
     * @param pObject   The object to update the hash code with, possibly null.
     *
     * @return  The updated hash code.
     */
    int update(int pHashCode, @Nullable Object pObject);

    /**
     * Update a hash code with an array of boolean values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable boolean[] pValue);

    /**
     * Update a hash code with an array of byte values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable byte[] pValue);

    /**
     * Update a hash code with an array of short values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable short[] pValue);

    /**
     * Update a hash code with an array of char values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable char[] pValue);

    /**
     * Update a hash code with an array of int values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable int[] pValue);

    /**
     * Update a hash code with an array of long values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable long[] pValue);

    /**
     * Update a hash code with an array of float values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable float[] pValue);

    /**
     * Update a hash code with an array of double values.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable double[] pValue);

    /**
     * Update a hash code with an array of objects. Whether or not this method allows arrays that
     * have themselves as an element (either directly or indirectly through one or more levels of
     * arrays) is up to the implementation.
     *
     * @param pHashCode The hash code to update.
     * @param pValue    The array to update the hash code with, possibly null.
     *
     * @return The updated hash code.
     */
    int update(int pHashCode, @Nullable Object[] pValue);
}
