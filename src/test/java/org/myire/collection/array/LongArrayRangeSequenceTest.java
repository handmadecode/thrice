/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import java.util.concurrent.ThreadLocalRandom;

import org.myire.collection.LongSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.myire.collection.LongSequenceBaseTest;


/**
 * Unit tests for the {@code PrimitiveSequence} implementation returned by
 * {@link PrimitiveSequences#wrap(long[], int, int)}.
 */
public class LongArrayRangeSequenceTest extends LongSequenceBaseTest
{
    @Override
    protected LongSequence createLongSequence(long[] pValues)
    {
        if (pValues.length == 0)
            return PrimitiveSequences.wrap(new long[64], 63, 0);

        // Create an array twice the length of the elements to wrap over and put the elements at a
        // random range in that array.
        long[] aValues = new long[pValues.length * 2];
        int aOffset = ThreadLocalRandom.current().nextInt(pValues.length + 1);
        System.arraycopy(pValues, 0, aValues, aOffset, pValues.length);
        return PrimitiveSequences.wrap(aValues, aOffset, pValues.length);
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(long[], int, int)} should throw a
     * {@code NullPointerException} when passed {@code null} as argument.
     */
    @Test
    public void wrapNullArrayThrows()
    {
        // Given
        long[] aArray = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> PrimitiveSequences.wrap(aArray, 0, 1)
        );
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(long[], int, int)} should throw an
     * {@code IndexOutOfBoundsException} when passed a negative offset argument.
     */
    @Test
    public void wrapThrowsForNegativeOffsetArgument()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.wrap(new long[1], -1, 1)
        );
    }


    /**
     * Calling {@code Sequences.wrap(int[], int, int)} should throw an
     * {@code IndexOutOfBoundsException} when passed a negative length argument.
     */
    @Test
    public void wrapThrowsForNegativeLengthArgument()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.wrap(new long[1], 0, -1)
        );
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(long[], int, int)} should throw an
     * {@code IndexOutOfBoundsException} when passed an invalid offset and length combination.
     */
    @Test
    public void wrapThrowsForInvalidOffsetAndLength()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.wrap(new long[2], 1, 2)
        );
    }
}
