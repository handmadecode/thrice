/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.randomIntegerInstance;
import static org.myire.collection.CollectionTests.randomIntValues;


/**
 * Base class for unit tests for {@code IntSequence} implementations.
 */
abstract public class IntSequenceBaseTest extends PrimitiveSequenceBaseTest<Integer>
{
    /**
     * Create an instance of the sequence to test.
     *
     * @param pValues   The sequence's values.
     *
     * @return  An {@code IntSequence} with the specified values.
     */
    abstract protected IntSequence createIntSequence(int[] pValues);


    @Override
    protected IntSequence createSequence(Integer[] pElements)
    {
        int[] aValues = new int[pElements.length];
        for (int i=0; i<pElements.length; i++)
            aValues[i] = pElements[i].intValue();

        return createIntSequence(aValues);
    }


    @Override
    protected Integer randomElement()
    {
        return randomIntegerInstance();
    }


    @Override
    protected Integer[] newArray(int pLength)
    {
        return new Integer[pLength];
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
            int[] aValues = randomIntValues(aElementCount);

            // When
            IntSequence aSequence = createIntSequence(aValues);

            // Then
            for (int i=0; i<aValues.length; i++)
                assertEquals(aValues[i], aSequence.valueAt(i));
        }
    }


    /**
     * An {@code IntSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void valueAtThrowsForNegativeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            IntSequence aSequence = createIntSequence(randomIntValues(aElementCount));
            int aIndex = ThreadLocalRandom.current().nextInt(-10, 0);

            // When
            assertThrows(
                IndexOutOfBoundsException.class,
                () -> aSequence.valueAt(aIndex)
            );
        }
    }


    /**
     * An {@code IntSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a too large index.
     */
    @Test
    public void valueAtThrowsForTooLargeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            IntSequence aSequence = createIntSequence(randomIntValues(aElementCount));

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
            int[] aValues = randomIntValues(aValueCount);
            IntConsumer aConsumer = mock(IntConsumer.class);

            // When
            createIntSequence(aValues).forEach(aConsumer);

            // Then
            for (int aValue : aValues)
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
            IntSequence aSequence = createIntSequence(randomIntValues(aValueCount));

            // When
            PrimitiveIterator.OfInt aIterator = aSequence.iterator();

            // Then
            for (int i=0; i<aSequence.size(); i++)
                assertEquals(aSequence.valueAt(i), aIterator.nextInt());
        }
    }
}
