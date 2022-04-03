/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import org.myire.collection.LongSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.myire.collection.LongSequenceBaseTest;


/**
 * Unit tests for the {@code PrimitiveSequence} implementation returned by
 * {@link PrimitiveSequences#wrap(long[])}.
 */
public class LongArraySequenceTest extends LongSequenceBaseTest
{
    @Override
    protected LongSequence createLongSequence(long[] pValues)
    {
        return PrimitiveSequences.wrap(pValues);
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(long[])} should throw a {@code NullPointerException}
     * when passed {@code null} as argument.
     */
    @Test
    public void wrapNullArrayThrows()
    {
        // Given
        long[] aArray = null;

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
        testWrappedArrayElements(new long[0]);
        testWrappedArrayElements(new long[]{17});
        testWrappedArrayElements(new long[]{-4711L, Long.MAX_VALUE, 451257634L, 0L, Long.MIN_VALUE});
    }


    /**
     * A {@code LongArraySequence} wrapping an {@code long[]} should contain the same values in the
     * same order as stored in the array.
     *
     * @param pArray    The array to test.
     */
    static private void testWrappedArrayElements(long[] pArray)
    {
        // When
        LongSequence aSeq = PrimitiveSequences.wrap(pArray);

        // Then
        assertEquals(pArray.length, aSeq.size());
        for (int i=0; i<pArray.length; i++)
            assertEquals(pArray[i], aSeq.valueAt(i));
    }
}
