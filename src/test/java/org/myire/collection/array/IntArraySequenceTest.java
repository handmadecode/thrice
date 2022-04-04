/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import org.myire.collection.IntSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.myire.collection.IntSequenceBaseTest;


/**
 * Unit tests for the {@code PrimitiveSequence} implementation returned by
 * {@link PrimitiveSequences#wrap(int[])}.
 */
public class IntArraySequenceTest extends IntSequenceBaseTest
{
    @Override
    protected IntSequence createIntSequence(int[] pValues)
    {
        return PrimitiveSequences.wrap(pValues);
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(int[])} should throw a {@code NullPointerException}
     * when passed {@code null} as argument.
     */
    @Test
    public void wrapNullArrayThrows()
    {
        // Given
        int[] aArray = null;

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
        testWrappedArrayElements(new int[0]);
        testWrappedArrayElements(new int[]{17});
        testWrappedArrayElements(new int[]{-23, Integer.MAX_VALUE, 23746, 0, Integer.MIN_VALUE});
    }


    /**
     * An {@code IntArraySequence} wrapping an {@code int[]} should contain the same values in the
     * same order as stored in the array.
     *
     * @param pArray    The array to test.
     */
    static private void testWrappedArrayElements(int[] pArray)
    {
        // When
        IntSequence aSeq = PrimitiveSequences.wrap(pArray);

        // Then
        assertEquals(pArray.length, aSeq.size());
        for (int i=0; i<pArray.length; i++)
            assertEquals(pArray[i], aSeq.valueAt(i));
    }
}
