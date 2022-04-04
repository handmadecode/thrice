/*
 * Copyright 2013, 2015, 2017, 2020-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.Iterator;
import java.util.function.Consumer;

import org.myire.collection.Sequence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base unit tests for {@code Sequence} implementations that never contain any elements.
 *
 * @param <T>   The element type used in the tests.
 */
abstract public class EmptySequenceBaseTest<T>
{
    /**
     * Create an instance of the empty sequence implementation.
     *
     * @return  A new empty sequence.
     */
    abstract Sequence<T> createEmptySequence();


    /**
     * An empty sequence should have size 0.
     */
    @Test
    public void emptySequenceHasSize0()
    {
        assertEquals(0, createEmptySequence().size());
    }


    /**
     * An empty sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt(0)}
     * is called.
     */
    @Test
    public void emptySequenceThrowsForElementAtIndex0()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> createEmptySequence().elementAt(0)
        );
    }


    /**
     * An empty sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt()}
     * is called with a positive index.
     */
    @Test
    public void emptySequenceThrowsForElementAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> createEmptySequence().elementAt(1)
        );
    }


    /**
     * An empty sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt()}
     * is called with a negative index.
     */
    @Test
    public void emptySequenceThrowsForElementAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> createEmptySequence().elementAt(-1)
        );
    }


    /**
     * An empty sequence should not invoke the action when {@code forEach()} is called.
     */
    @Test
    public void emptySequenceDoesNotInvokeForEachAction()
    {
        // Given
        Consumer<T> aConsumer = createMockConsumer();

        // When
        createEmptySequence().forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * An empty sequence should return an empty {@code Iterator} from its {@code iterator()}
     * method.
     */
    @Test
    public void emptySequenceIteratorHasNoElements()
    {
        // Given
        Iterator<T> aIterator = createEmptySequence().iterator();

        // Then
        assertFalse(aIterator.hasNext());
    }
}
