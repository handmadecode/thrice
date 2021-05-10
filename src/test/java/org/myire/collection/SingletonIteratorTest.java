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

import static org.myire.collection.Iterators.singletonIterator;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link Iterators#singletonIterator(Object)}.
 */
public class SingletonIteratorTest
{
    /**
     * The {@code hasNext} method should return true after the iterator has been created.
     */
    @Test
    public void hasNextReturnsTrueForNewIterator()
    {
        // Given
        Iterator<String> aIterator = singletonIterator("");

        // Then
        assertTrue(aIterator.hasNext());
    }


    /**
     * The {@code hasNext} method should return false after the single element has been returned by
     * {@code next}.
     */
    @Test
    public void hasNextReturnsFalseAfterCallToNext()
    {
        // Given
        Iterator<String> aIterator = singletonIterator("");

        // When
        aIterator.next();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code next} method should return the element passed to the constructor.
     */
    @Test
    public void nextReturnsTheExpectedElement()
    {
        // Given
        String aString = "sole element";
        Iterator<String> aIterator = singletonIterator(aString);

        // Then
        assertSame(aString, aIterator.next());
    }


    /**
     * The {@code next} method should throw a {@code NoSuchElementException} on all calls except the
     * first.
     */
    @Test
    public void nextThrowsAfterReturningTheElement()
    {
        // Given
        Iterator<String> aIterator = singletonIterator("");

        // When
        aIterator.next();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::next
        );
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at the start
     * of the iteration.
     */
    @Test
    public void removeThrowsAtStartOfIteration()
    {
        // Given
        Iterator<String> aIterator = singletonIterator("");

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
        Iterator<String> aIterator = singletonIterator("");

        // When
        aIterator.next();

        // Then
        assertThrows(
            UnsupportedOperationException.class,
            aIterator::remove
        );
    }


    /**
     * The {@code forEachRemaining} method should pass the element to the specified action if
     * {@code next} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesTheElement()
    {
        // Given
        String aString = "xyz";
        Consumer<String> aAction = mock(IteratorBaseTest.StringConsumer.class);
        Iterator<String> aIterator = singletonIterator(aString);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verify(aAction).accept(aString);
        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action if the element has
     * been returned by the iteration.
     */
    @Test
    public void forEachRemainingDoesNothingForEmptyIterable()
    {
        // Given
        Iterator<String> aIterator = singletonIterator("");
        Consumer<String> aAction = mock(IteratorBaseTest.StringConsumer.class);

        // When
        aIterator.next();
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoMoreInteractions(aAction);
    }
}
