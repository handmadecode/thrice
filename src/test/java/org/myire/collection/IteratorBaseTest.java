/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Base test for {@code Iterator} implementations.
 */
abstract public class IteratorBaseTest
{
    /**
     * Create an instance of the {@code Iterator} to test.
     *
     * @param pElements The elements to return from the iteration.
     * @param <T>   The type of the elements.
     *
     * @return  A new {@code Iterator}.
     */
    abstract protected <T> Iterator<T> createIterator(T[] pElements);


   /**
     * The {@code hasNext} method should return false when the iteration contains no elements.
     */
    @Test
    public void hasNextReturnsFalseForEmptyIteration()
    {
        // Given
        Iterator<Object> aIterator = createIterator(new Object[0]);

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
        // Given
        String[] aStrings = {"a", "b", "c", "d", "e"};
        Iterator<String> aIterator = createIterator(aStrings);

        // Then
        for (int i=0; i<aStrings.length; i++)
        {
            assertTrue(aIterator.hasNext());
            aIterator.next();
        }
    }


    /**
     * The {@code hasNext} method should return false at the end of the iteration.
     */
    @Test
    public void hasNextReturnsFalseAtEndOfIteration()
    {
        // Given
        String[] aStrings = {"a", "b", "c", "d", "e"};
        Iterator<String> aIterator = createIterator(aStrings);

        // When
        for (int i=0; i<aStrings.length; i++)
            aIterator.next();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code next} method should return the elements from the underlying iterable in the
     * correct order.
     */
    @Test
    public void nextReturnsTheExpectedElements()
    {
        // Given
        String[] aStrings = {"a", "b", "c", "d", "e"};
        Iterator<String> aIterator = createIterator(aStrings);

        // Then
        for (String aString : aStrings)
            assertSame(aString, aIterator.next());
    }


    /**
     * The {@code next} method should throw a {@code NoSuchElementException} when called after all
     * elements from the underlying iterable have been returned.
     */
    @Test
    public void nextThrowsAfterReturningAllElements()
    {
        // Given
        String[] aStrings = {"a", "b", "c", "d", "e"};
        Iterator<String> aIterator = createIterator(aStrings);
        for (int i=0; i< aStrings.length; i++)
            aIterator.next();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::next
        );
    }


    /**
     * The {@code next} method should throw a {@code NoSuchElementException} at any point for an
     * empty iteration
     */
    @Test
    public void nextThrowsForEmptyIteration()
    {
        // Given
        Iterator<Object> aIterator = createIterator(new Object[0]);

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::next
        );
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at any point
     * during the iteration.
     */
    @Test
    public void removeThrowsDuringIteration()
    {
        // Given
        String[] aStrings = {"a", "b", "c"};
        Iterator<String> aIterator = createIterator(aStrings);

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


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at the start
     * of the iteration.
     */
    @Test
    public void removeThrowsAtStartOfIteration()
    {
        // Given
        String[] aStrings = {"a", "b", "c"};
        Iterator<String> aIterator = createIterator(aStrings);

        // Then
        assertThrows(
            UnsupportedOperationException.class,
            aIterator::remove
        );
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at the end of
     * the iteration.
     */
    @Test
    public void removeThrowsAtEndOfIteration()
    {
        // Given
        String[] aStrings = {"a", "b", "c"};
        Iterator<String> aIterator = createIterator(aStrings);
        for (int i=0; i< aStrings.length; i++)
            aIterator.next();

        // Then
        assertThrows(
            UnsupportedOperationException.class,
            aIterator::remove
        );
    }


    /**
     * The {@code forEachRemaining} method should pass all elements to the specified action if
     * {@code next} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesAllElements()
    {
        // Given
        String[] aStrings = {"a", "b", "c", "d", "e"};
        Consumer<String> aAction = mock(StringConsumer.class);
        Iterator<String> aIterator = createIterator(aStrings);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        for (String aString : aStrings)
            verify(aAction).accept(aString);

        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should pass only the elements not already returned by
     * {@code next} to the specified action.
     */
    @Test
    public void forEachRemainingProcessesOnlyRemainingElements()
    {
        // Given
        String[] aStrings = {"a", "b", "c", "d", "e", "f"};
        Consumer<String> aAction = mock(StringConsumer.class);
        Iterator<String> aIterator = createIterator(aStrings);
        int aNumCallsToNext = 2;
        for (int i=0; i<aNumCallsToNext; i++)
            aIterator.next();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        for (int i=aNumCallsToNext; i<aStrings.length; i++)
            verify(aAction).accept(aStrings[i]);

        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action for an empty
     * iteration.
     */
    @Test
    public void forEachRemainingDoesNothingForEmptyIteration()
    {
        // Given
        Consumer<String> aAction = mock(StringConsumer.class);
        Iterator<String> aIterator = createIterator(new String[0]);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action for an iterator
     * with no elements left.
     */
    @Test
    public void forEachRemainingDoesNothingForExhaustedIterator()
    {
        // Given
        Consumer<String> aAction = mock(StringConsumer.class);
        String[] aStrings = {"a", "b", "c"};
        Iterator<String> aIterator = createIterator(aStrings);
        for (int i=0; i< aStrings.length; i++)
            aIterator.next();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoMoreInteractions(aAction);
    }


    /**
     * For type-safe mocking.
     */
    interface StringConsumer extends Consumer<String>
    {
        // No additional methods.
    }
}
