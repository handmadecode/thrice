/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleConsumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.randomDoubleInstance;
import static org.myire.collection.CollectionTests.randomDoubleValues;


/**
 * Base class for unit tests for {@code DoubleSequence} implementations.
 */
abstract public class DoubleSequenceBaseTest extends PrimitiveSequenceBaseTest<Double>
{
    /**
     * Create an instance of the sequence to test.
     *
     * @param pValues   The sequence's values.
     *
     * @return  A {@code DoubleSequence} with the specified values.
     */
    abstract protected DoubleSequence createDoubleSequence(double[] pValues);


    @Override
    protected DoubleSequence createSequence(Double[] pElements)
    {
        double[] aValues = new double[pElements.length];
        for (int i=0; i<pElements.length; i++)
            aValues[i] = pElements[i].doubleValue();

        return createDoubleSequence(aValues);
    }


    @Override
    protected Double randomElement()
    {
        return randomDoubleInstance();
    }


    @Override
    protected Double[] newArray(int pLength)
    {
        return new Double[pLength];
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
            double[] aValues = randomDoubleValues(aElementCount);

            // When
            DoubleSequence aSequence = createDoubleSequence(aValues);

            // Then
            for (int i=0; i<aValues.length; i++)
                assertEquals(aValues[i], aSequence.valueAt(i));
        }
    }


    /**
     * A {@code DoubleSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void valueAtThrowsForNegativeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            DoubleSequence aSequence = createDoubleSequence(randomDoubleValues(aElementCount));
            int aIndex = ThreadLocalRandom.current().nextInt(-10, 0);

            // When
            assertThrows(
                IndexOutOfBoundsException.class,
                () -> aSequence.valueAt(aIndex)
            );
        }
    }


    /**
     * A {@code DoubleSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a too large index.
     */
    @Test
    public void valueAtThrowsForTooLargeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            DoubleSequence aSequence = createDoubleSequence(randomDoubleValues(aElementCount));

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
            double[] aValues = randomDoubleValues(aValueCount);
            DoubleConsumer aConsumer = mock(DoubleConsumer.class);

            // When
            createDoubleSequence(aValues).forEach(aConsumer);

            // Then
            for (double aValue : aValues)
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
            DoubleSequence aSequence = createDoubleSequence(randomDoubleValues(aValueCount));

            // When
            PrimitiveIterator.OfDouble aIterator = aSequence.iterator();

            // Then
            for (int i = 0; i < aSequence.size(); i++)
                assertEquals(aSequence.valueAt(i), aIterator.nextDouble());
        }
    }
}
