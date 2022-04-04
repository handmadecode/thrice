/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verifyNoInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base class for unit tests of {@code Iterator} implementations that are a specialization for an
 * iteration over zero elements.
 *
 * @param <T>   The element type used in the tests.
 */
abstract class EmptyIteratorBaseTest<T>
{
    /**
     * Create an instance of the {@code Iterator} to test.
     *
     * @return  A new {@code Iterator} that has no elements to return.
     */
    abstract protected Iterator<T> createIterator();


    /**
     * The {@code hasNext} method should return false.
     */
    @Test
    public void hasNextReturnsFalse()
    {
        // Given
        Iterator<T> aIterator = createIterator();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code next} method should throw a {@code NoSuchElementException}.
     */
    @Test
    public void nextThrows()
    {
        // Given
        Iterator<T> aIterator = createIterator();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::next
        );
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException}.
     */
    @Test
    public void removeThrows()
    {
        // Given
        Iterator<T> aIterator = createIterator();

        // Then
        assertThrows(
            UnsupportedOperationException.class,
            aIterator::remove
        );
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action.
     */
    @Test
    public void forEachRemainingDoesNothing()
    {
        // Given
        Consumer<T> aAction = createMockConsumer();
        Iterator<T> aIterator = createIterator();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoInteractions(aAction);
    }
}
