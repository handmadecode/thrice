/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link Sequences#wrap(Object[], int, int)}.
 */
public class ArrayRangeSequenceTest extends SequenceBaseTest
{
    @Override
    @SuppressWarnings("unchecked")
    protected <T> Sequence<T> createEmptySequence()
    {
        return Sequences.wrap((T[]) new Object[8], 4, 0);
    }


    @Override
    protected <T> Sequence<T> createSequence(T[] pElements)
    {
        // Create an array twice the length of the elements to wrap over and put the elements at a
        // random range in that array.
        @SuppressWarnings("unchecked")
        T[] aArray = (T[]) new Object[pElements.length * 2];
        int aOffset = ThreadLocalRandom.current().nextInt(pElements.length + 1);
        System.arraycopy(pElements, 0, aArray, aOffset, pElements.length);
        return Sequences.wrap(aArray, aOffset, pElements.length);
    }


    /**
     * Calling {@code Sequences.wrap(T[], int, int)} should throw a {@code NullPointerException}
     * when passed {@code null} as argument.
     */
    @Test
    public void wrapNullArrayThrows()
    {
        // Given
        Object[] aArray = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> Sequences.wrap(aArray, 0, 1)
        );
    }


    /**
     * Calling {@code Sequences.wrap(T[], int, int)} should throw an
     * {@code IllegalArgumentException} when passed a negative offset argument.
     */
    @Test
    public void wrapThrowsForNegativeOffsetArgument()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> Sequences.wrap(new Object[1], -1, 1)
        );
    }


    /**
     * Calling {@code Sequences.wrap(T[], int, int)} should throw an
     * {@code IllegalArgumentException} when passed a negative length argument.
     */
    @Test
    public void factoryMethodThrowsForNegativeLengthArgument()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> Sequences.wrap(new Object[1], 0, -1)
        );
    }


    /**
     * Calling {@code Sequences.wrap(T[], int, int)} should throw an
     * {@code IllegalArgumentException} when passed an invalid offset and length combination.
     */
    @Test
    public void factoryMethodThrowsForInvalidOffsetAndLength()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> Sequences.wrap(new Object[2], 1, 2)
        );
    }
}
