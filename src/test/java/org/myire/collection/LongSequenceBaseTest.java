/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongConsumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.randomLongInstance;
import static org.myire.collection.CollectionTests.randomLongValues;


/**
 * Base class for unit tests for {@code LongSequence} implementations.
 */
abstract public class LongSequenceBaseTest extends PrimitiveSequenceBaseTest<Long>
{
    /**
     * Create an instance of the sequence to test.
     *
     * @param pValues   The sequence's values.
     *
     * @return  A {@code LongSequence} with the specified values.
     */
    abstract protected LongSequence createLongSequence(long[] pValues);


    @Override
    protected LongSequence createSequence(Long[] pElements)
    {
        long[] aValues = new long[pElements.length];
        for (int i=0; i<pElements.length; i++)
            aValues[i] = pElements[i].longValue();

        return createLongSequence(aValues);
    }


    @Override
    protected Long randomElement()
    {
        return randomLongInstance();
    }


    @Override
    protected Long[] newArray(int pLength)
    {
        return new Long[pLength];
    }


    /**
     * {@code valueAt()} should return the expected element.
     */
    @Test
    public void valueAtReturnsTheExpectedValue()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            long[] aValues = randomLongValues(aElementCount);

            // When
            LongSequence aSequence = createLongSequence(aValues);

            // Then
            for (int i=0; i<aValues.length; i++)
                assertEquals(aValues[i], aSequence.valueAt(i));
        }
    }


    /**
     * A {@code LongSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void valueAtThrowsForNegativeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            LongSequence aSequence = createLongSequence(randomLongValues(aElementCount));
            int aIndex = ThreadLocalRandom.current().nextInt(-10, 0);

            // When
            assertThrows(
                IndexOutOfBoundsException.class,
                () -> aSequence.valueAt(aIndex)
            );
        }
    }


    /**
     * A {@code LongSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a too large index.
     */
    @Test
    public void valueAtThrowsForTooLargeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            LongSequence aSequence = createLongSequence(randomLongValues(aElementCount));

            // When
            assertThrows(
                IndexOutOfBoundsException.class,
                () -> aSequence.valueAt(aElementCount)
            );
        }
    }


    /**
     * {@code forEach()} should invoke the specified action on each values in the sequence.
     */
    @Test
    public void forEachPassesAllElementsToAction()
    {
        int[] aValueCounts = {1, randomCollectionLength()};
        for (int aValueCount : aValueCounts)
        {
            // Given
            long[] aValues = randomLongValues(aValueCount);
            LongConsumer aConsumer = mock(LongConsumer.class);

            // When
            createLongSequence(aValues).forEach(aConsumer);

            // Then
            for (long aValue : aValues)
                verify(aConsumer).accept(eq(aValue));

            verifyNoMoreInteractions(aConsumer);
        }
    }


    /**
     * A sequence should return an iterator that iterates over the sequence's elements in the same
     * order as the elements are returned by {@code valueAt()}.
     */
    @Test
    public void iteratorIteratesOverAllValues()
    {
        int[] aValueCounts = {1, randomCollectionLength()};
        for (int aValueCount : aValueCounts)
        {
            // Given
            LongSequence aSequence = createLongSequence(randomLongValues(aValueCount));

            // When
            PrimitiveIterator.OfLong aIterator = aSequence.iterator();

            // Then
            for (int i = 0; i < aSequence.size(); i++)
                assertEquals(aSequence.valueAt(i), aIterator.nextLong());
        }
    }
}
