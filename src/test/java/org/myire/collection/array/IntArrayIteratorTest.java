/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;

import static org.myire.collection.PrimitiveIterators.arrayIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.myire.collection.IteratorBaseTest;
import static org.myire.collection.CollectionTests.randomIntegerInstance;
import static org.myire.collection.CollectionTests.randomIntValues;


/**
 * Unit tests for the {@code PrimitiveIterator.OfInt} implementation returned by
 * {@link org.myire.collection.PrimitiveIterators#arrayIterator(int[], int, int)}.
 */
public class IntArrayIteratorTest extends IteratorBaseTest<Integer>
{
    static private PrimitiveIterator.OfInt createIterator(int[] pValues)
    {
        if (pValues.length == 0)
            return arrayIterator(new int[12], 10, 0);

        // Create an array twice the length of the elements to wrap over and put the elements at a
        // random range in that array.
        int[] aValues = new int[pValues.length * 2];
        int aOffset = ThreadLocalRandom.current().nextInt(pValues.length + 1);
        System.arraycopy(pValues, 0, aValues, aOffset, pValues.length);
        return arrayIterator(aValues, aOffset, pValues.length);
    }


    @Override
    protected Iterator<Integer> createIterator(Integer[] pElements)
    {
        int[] aValues = new int[pElements.length];
        for (int i=0; i<pElements.length; i++)
            aValues[i] = pElements[i].intValue();

        return createIterator(aValues);
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
     * The factory method {@code PrimitiveIterators.arrayIterator} should throw a
     * {@code NullPointerException} when passed a null array argument.
     */
    @Test
    public void factoryMethodThrowsForNullArrayArgument()
    {
        // Given
        int[] aValues = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> arrayIterator(aValues, 0, 1)
        );
    }


    /**
     * The factory method {@code PrimitiveIterators.arrayIterator} should throw an
     * {@code IndexOutOfBoundsException} when passed a negative offset argument.
     */
    @Test
    public void factoryMethodThrowsForNegativeOffsetArgument()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> arrayIterator(new int[1], -1, 1)
        );
    }


    /**
     * The factory method {@code PrimitiveIterators.arrayIterator} should throw an
     * {@code IllegalArgumentException} when passed a negative length argument.
     */
    @Test
    public void factoryMethodThrowsForNegativeLengthArgument()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> arrayIterator(new int[1], 0, -1)
        );
    }


    /**
     * The factory method {@code PrimitiveIterators.arrayIterator} should throw an
     * {@code IllegalArgumentException} when passed an invalid offset and length combination.
     */
    @Test
    public void factoryMethodThrowsForInvalidOffsetAndLength()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> arrayIterator(new int[2], 1, 2)
        );
    }


    /**
     * The {@code nextInt} method should return the values from the underlying {@code int} array in
     * the correct order.
     */
    @Test
    public void nextIntReturnsTheExpectedValues()
    {
        // Given
        int[] aValueCounts = {1, randomCollectionLength()};
        for (int aValueCount : aValueCounts)
        {
            int[] aValues = randomIntValues(aValueCount);
            PrimitiveIterator.OfInt aIterator = createIterator(aValues);

            // Then
            for (int aValue : aValues)
                assertEquals(aValue, aIterator.nextInt());
        }
    }


    /**
     * The {@code nextInt} method should throw a {@code NoSuchElementException} when called after
     * all values from the underlying {@code int} array have been returned.
     */
    @Test
    public void nextIntThrowsAfterReturningAllElements()
    {
        int[] aValueCounts = {0, 1, randomCollectionLength()};
        for (int aValueCount : aValueCounts)
        {
            // Given (an exhausted iterator)
            int[] aValues = randomIntValues(aValueCount);
            PrimitiveIterator.OfInt aIterator = createIterator(aValues);
            for (int i=0; i<aValues.length; i++)
                aIterator.nextInt();

            // Then
            assertThrows(
                NoSuchElementException.class,
                aIterator::nextInt
            );
        }
    }


    /**
     * The {@code forEachRemaining} method should pass all values to the specified action if
     * {@code nextInt} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesAllValues()
    {
        int[] aValueCounts = {0, 1, randomCollectionLength()};
        for (int aValueCount : aValueCounts)
        {
            // Given
            int[] aValues = randomIntValues(aValueCount);
            PrimitiveIterator.OfInt aIterator = createIterator(aValues);
            IntConsumer aAction = mock(IntConsumer.class);

            // When
            aIterator.forEachRemaining(aAction);

            // Then
            for (int aValue : aValues)
                verify(aAction).accept(eq(aValue));

            verifyNoMoreInteractions(aAction);
            assertFalse(aIterator.hasNext());
        }
    }


    /**
     * The {@code forEachRemaining} method should pass only the elements not already returned by
     * {@code nextInt} to the specified action.
     */
    @Test
    public void forEachRemainingProcessesOnlyRemainingElements()
    {
        // Given
        int[] aValues = randomIntValues(randomCollectionLength());
        PrimitiveIterator.OfInt aIterator = createIterator(aValues);
        IntConsumer aAction = mock(IntConsumer.class);

        // Given (some elements have been returned by nextInt())
        int aNumCallsToNext = aValues.length > 2 ? 2 : 1;
        for (int i=0; i<aNumCallsToNext; i++)
            aIterator.nextInt();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        for (int i=aNumCallsToNext; i<aValues.length; i++)
            verify(aAction).accept(eq(aValues[i]));

        verifyNoMoreInteractions(aAction);
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action for an iterator
     * with no elements left.
     */
    @Test
    public void forEachRemainingDoesNothingForExhaustedIterator()
    {
        int[] aValueCounts = {0, 1, randomCollectionLength()};
        for (int aValueCount : aValueCounts)
        {
            // Given
            int[] aValues = randomIntValues(aValueCount);
            PrimitiveIterator.OfInt aIterator = createIterator(aValues);
            IntConsumer aAction = mock(IntConsumer.class);

            // Given (an exhausted iterator)
            for (int i=0; i<aValues.length; i++)
                aIterator.nextInt();

            // When
            aIterator.forEachRemaining(aAction);

            // Then
            verifyNoInteractions(aAction);
            assertFalse(aIterator.hasNext());
        }
    }
}
