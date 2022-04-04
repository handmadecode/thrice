/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import java.util.concurrent.ThreadLocalRandom;

import org.myire.collection.DoubleSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.myire.collection.DoubleSequenceBaseTest;


/**
 * Unit tests for the {@code PrimitiveSequence} implementation returned by
 * {@link PrimitiveSequences#wrap(double[], int, int)}.
 */
public class DoubleArrayRangeSequenceTest extends DoubleSequenceBaseTest
{
    @Override
    protected DoubleSequence createDoubleSequence(double[] pValues)
    {
        if (pValues.length == 0)
            return PrimitiveSequences.wrap(new double[666], 211, 0);

        // Create an array twice the length of the elements to wrap over and put the elements at a
        // random range in that array.
        double[] aValues = new double[pValues.length * 2];
        int aOffset = ThreadLocalRandom.current().nextInt(pValues.length + 1);
        System.arraycopy(pValues, 0, aValues, aOffset, pValues.length);
        return PrimitiveSequences.wrap(aValues, aOffset, pValues.length);
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(double[], int, int)} should throw a
     * {@code NullPointerException} when passed {@code null} as argument.
     */
    @Test
    public void wrapNullArrayThrows()
    {
        // Given
        double[] aArray = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> PrimitiveSequences.wrap(aArray, 0, 1)
        );
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(double[], int, int)} should throw an
     * {@code IndexOutOfBoundsException} when passed a negative offset argument.
     */
    @Test
    public void wrapThrowsForNegativeOffsetArgument()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.wrap(new double[1], -1, 1)
        );
    }


    /**
     * Calling {@code Sequences.wrap(double[], int, int)} should throw an
     * {@code IndexOutOfBoundsException} when passed a negative length argument.
     */
    @Test
    public void wrapThrowsForNegativeLengthArgument()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.wrap(new double[1], 0, -1)
        );
    }


    /**
     * Calling {@code PrimitiveSequences.wrap(double[], int, int)} should throw an
     * {@code IndexOutOfBoundsException} when passed an invalid offset and length combination.
     */
    @Test
    public void wrapThrowsForInvalidOffsetAndLength()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.wrap(new double[2], 1, 2)
        );
    }
}
