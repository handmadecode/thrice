/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base test for {@code Iterator} implementations.
 *
 * @param <T>   The type of the elements returned by the iterator implementation being tested.
 *
 */
abstract public class IteratorBaseTest<T> extends CollectionBaseTest<T>
{
    /**
     * Create an instance of the {@code Iterator} to test.
     *
     * @param pElements The elements to return from the iteration.
     *
     * @return  A new {@code Iterator}.
     */
    abstract protected Iterator<T> createIterator(T[] pElements);


    /**
     * The {@code hasNext} method should return false when the iteration contains no elements.
     */
    @Test
    public void hasNextReturnsFalseForEmptyIteration()
    {
        // Given
        Iterator<T> aIterator = createIterator(randomElementArray(0));

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code hasNext} method should return true as long as there are elements left in the
     * iteration.
     */
    @Test
    public void hasNextReturnsTrueDuringIteration()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Iterator<T> aIterator = createIterator(randomElementArray(aElementCount));

            // Then
            for (int i=0; i<aElementCount; i++)
            {
                assertTrue(aIterator.hasNext());
                aIterator.next();
            }
        }
    }


    /**
     * The {@code hasNext} method should return false at the end of the iteration.
     */
    @Test
    public void hasNextReturnsFalseAtEndOfIteration()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Iterator<T> aIterator = createIterator(randomElementArray(aElementCount));

            // When
            for (int i=0; i<aElementCount; i++)
                aIterator.next();

            // Then
            assertFalse(aIterator.hasNext());
        }
    }


    /**
     * The {@code next} method should return the elements from the underlying iterable in the
     * correct order.
     */
    @Test
    public void nextReturnsTheExpectedElements()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            T[] aElements = randomElementArray(aElementCount);
            Iterator<T> aIterator = createIterator(aElements);

            // Then
            for (T aElement : aElements)
                assertEquals(aElement, aIterator.next());
        }
    }


    /**
     * The {@code next} method should throw a {@code NoSuchElementException} when called after all
     * elements from the underlying iterable have been returned.
     */
    @Test
    public void nextThrowsAfterReturningAllElements()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Iterator<T> aIterator = createIterator(randomElementArray(aElementCount));

            // Given (an exhausted iterator)
            for (int i=0; i<aElementCount; i++)
                aIterator.next();

            // Then
            assertThrows(
                NoSuchElementException.class,
                aIterator::next
            );
        }
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at any point
     * during the iteration.
     */
    @Test
    public void removeThrowsDuringIteration()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Iterator<T> aIterator = createIterator(randomElementArray(aElementCount));

            while (aIterator.hasNext())
            {
                aIterator.next();

                // Then
                assertThrows(
                    UnsupportedOperationException.class,
                    aIterator::remove
                );
            }
        }
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at the start
     * of the iteration.
     */
    @Test
    public void removeThrowsAtStartOfIteration()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Iterator<T> aIterator = createIterator(randomElementArray(aElementCount));

            // Then
            assertThrows(
                UnsupportedOperationException.class,
                aIterator::remove
            );
        }
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at the end of
     * the iteration.
     */
    @Test
    public void removeThrowsAtEndOfIteration()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Iterator<T> aIterator = createIterator(randomElementArray(aElementCount));
            for (int i=0; i<aElementCount; i++)
                aIterator.next();

            // Then
            assertThrows(
                UnsupportedOperationException.class,
                aIterator::remove
            );
        }
    }


    /**
     * The {@code forEachRemaining} method should pass all elements to the specified action if
     * {@code next} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesAllElements()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            T[] aElements = randomElementArray(aElementCount);
            Iterator<T> aIterator = createIterator(aElements);
            Consumer<T> aAction = createMockConsumer();

            // When
            aIterator.forEachRemaining(aAction);

            // Then
            for (T aElement : aElements)
                verify(aAction).accept(eq(aElement));

            verifyNoMoreInteractions(aAction);
            assertFalse(aIterator.hasNext());
        }
    }


    /**
     * The {@code forEachRemaining} method should pass only the elements not already returned by
     * {@code next} to the specified action.
     */
    @Test
    public void forEachRemainingProcessesOnlyRemainingElements()
    {
        // Given
        T[] aElements = randomElementArray(randomCollectionLength());
        Iterator<T> aIterator = createIterator(aElements);
        Consumer<T> aAction = createMockConsumer();

        // Given (some elements have been returned by next())
        int aNumCallsToNext = aElements.length > 2 ? 2 : 1;
        for (int i=0; i<aNumCallsToNext; i++)
            aIterator.next();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        for (int i=aNumCallsToNext; i<aElements.length; i++)
            verify(aAction).accept(eq(aElements[i]));

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
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Iterator<T> aIterator = createIterator(randomElementArray(aElementCount));
            Consumer<T> aAction = createMockConsumer();
            for (int i=0; i<aElementCount; i++)
                aIterator.next();

            // When
            aIterator.forEachRemaining(aAction);

            // Then
            verifyNoInteractions(aAction);
            assertFalse(aIterator.hasNext());
        }
    }
}
