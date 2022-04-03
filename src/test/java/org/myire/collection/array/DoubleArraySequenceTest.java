/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import org.myire.collection.DoubleSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.myire.collection.DoubleSequenceBaseTest;


/**
 * Unit tests for the {@code PrimitiveSequence} implementation returned by
 * {@link PrimitiveSequences#wrap(double[])}.
 */
public class DoubleArraySequenceTest extends DoubleSequenceBaseTest
{
    @Override
    protected DoubleSequence createDoubleSequence(double[] pValues)
    {
        return PrimitiveSequences.wrap(pValues);
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(double[])} should throw a {@code NullPointerException}
     * when passed {@code null} as argument.
     */
    @Test
    public void wrapNullArrayThrows()
    {
        // Given
        double[] aArray = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> PrimitiveSequences.wrap(aArray)
        );
    }


    /**
     * A sequence wrapping an array should contain the same values in the same order as stored in
     * the array.
     */
    @Test
    public void sequenceContainsElementsFromWrappedArray()
    {
        testWrappedArrayElements(new double[0]);
        testWrappedArrayElements(new double[]{-34278.45d});
        testWrappedArrayElements(new double[]{-0.0d, Double.MAX_VALUE, 3.14159d, 0.0d, Double.MIN_VALUE});
    }


    /**
     * A {@code DoubleSequence} wrapping an {@code double[]} should contain the same values in the
     * same order as stored in the array.
     *
     * @param pArray    The array to test.
     */
    static private void testWrappedArrayElements(double[] pArray)
    {
        // When
        DoubleSequence aSeq = PrimitiveSequences.wrap(pArray);

        // Then
        assertEquals(pArray.length, aSeq.size());
        for (int i=0; i<pArray.length; i++)
            assertEquals(pArray[i], aSeq.valueAt(i));
    }
}
