/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base class for unit tests of {@code Iterator} implementations that are a specialization for a
 * single element.
 *
 * @param <T>   The element type used in the tests.
 */
abstract class SingletonIteratorBaseTest<T>
{
    /**
     * Create a random {@code T} instance.
     *
     * @return  A new {@code T} instance.
     */
    abstract protected T randomElement();

    /**
     * Create an instance of the {@code Iterator} to test.
     *
     * @param pElement The single element to return from the iteration.
     *
     * @return  A new {@code Iterator}.
     */
    abstract protected Iterator<T> createIterator(T pElement);


    /**
     * The {@code hasNext} method should return true after the iterator has been created.
     */
    @Test
    public void hasNextReturnsTrueForNewIterator()
    {
        // Given
        Iterator<T> aIterator = createIterator(randomElement());

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
        Iterator<T> aIterator = createIterator(randomElement());

        // When
        aIterator.next();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code next} method should throw a {@code NoSuchElementException} on all calls except the
     * first.
     */
    @Test
    public void nextThrowsAfterReturningTheElement()
    {
        // Given
        Iterator<T> aIterator = createIterator(randomElement());

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
        Iterator<T> aIterator = createIterator(randomElement());

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
        Iterator<T> aIterator = createIterator(randomElement());

        // When
        aIterator.next();

        // Then
        assertThrows(
            UnsupportedOperationException.class,
            aIterator::remove
        );
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action if the element has
     * been returned by the iteration.
     */
    @Test
    public void forEachRemainingDoesNothingWhenElementHasBeenReturned()
    {
        // Given
        Consumer<T> aAction = createMockConsumer();
        Iterator<T> aIterator = createIterator(randomElement());

        // When
        aIterator.next();
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoMoreInteractions(aAction);
    }
}
