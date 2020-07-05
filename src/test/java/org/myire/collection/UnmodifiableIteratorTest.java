/*
 * Copyright 2009, 2016-2017, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Arrays;
import java.util.Collections;
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

import static org.myire.collection.Iterators.unmodifiableIterator;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link Iterators#unmodifiableIterator(Iterator)}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class UnmodifiableIteratorTest
{
    /**
     * The factory method {@code Iterators.unmodifiableIterator} should throw a
     * {@code NullPointerException} when passed a null argument.
     */
    @SuppressWarnings("unused")
    @Test
    public void factoryMethodThrowsForNullArgument()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                unmodifiableIterator(null)
        );
    }


    /**
     * The {@code hasNext} method should return false for an empty iterable.
     */
    @Test
    public void hasNextReturnsFalseForEmptyIterable()
    {
        // Given
        Iterator<String> aIterator = unmodifiableIterator(Collections.emptyIterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());
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
     * empty iterable
     */
    @Test
    public void nextThrowsForEmptyIterable()
    {
        // Given
        Iterator<String> aIterator = unmodifiableIterator(Collections.emptyIterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());
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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());
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
     * iterable.
     */
    @Test
    public void forEachRemainingDoesNothingForEmptyIterable()
    {
        // Given
        Consumer<String> aAction = mock(StringConsumer.class);
        Iterator<String> aIterator = unmodifiableIterator(Collections.emptyIterator());

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
        Iterator<String> aIterator = unmodifiableIterator(Arrays.asList(aStrings).iterator());
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
    private interface StringConsumer extends Consumer<String>
    {
        // No additional methods.
    }
}
