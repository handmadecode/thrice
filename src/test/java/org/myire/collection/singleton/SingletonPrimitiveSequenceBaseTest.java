/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.Iterator;
import java.util.function.Consumer;

import org.myire.collection.Sequence;
import org.myire.collection.Sequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base unit tests for {@code PrimitiveSequence} implementations that contain a single element.
 */
abstract public class SingletonPrimitiveSequenceBaseTest<T extends Number>
    extends SingletonSequenceBaseTest<T>
{
    /**
     * Create a random {@code T} element.
     *
     * @return  A new {@code T} element.
     */
    abstract protected T randomElement();

    /**
     * Create a singleton sequence.
     *
     * @param pElement  The element the new sequence should contain.
     *
     * @return  A new {@code Sequence} with the specified element.
     */
    abstract protected Sequence<T> createSingletonSequence(T pElement);


    @Override
    protected Sequence<T> createSingletonSequence()
    {
        return createSingletonSequence(randomElement());
    }


    /**
     * A primitive singleton sequence should contain an element with the same primitive value as
     * passed to the factory method.
     */
    @Test
    public void singletonPrimitiveSequenceContainsExpectedElement()
    {
        // Given
        T aElement = randomElement();

        // Then
        assertEquals(aElement, createSingletonSequence(aElement).elementAt(0));
    }


    /**
     * A primitive singleton sequence should invoke the action on its element when
     * {@code forEach()} is called.
     */
    @Test
    public void singletonPrimitiveSequenceInvokesForEachActionOnElement()
    {
        // Given
        T aElement = randomElement();
        Consumer<T> aConsumer = createMockConsumer();

        // When
        Sequences.singleton(aElement).forEach(aConsumer);

        // Then
        verify(aConsumer).accept(eq(aElement));
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A primitive singleton sequence should return an {@code Iterator} with the sequence's single
     * element.
     */
    @Test
    public void singletonPrimitiveSequenceIteratorReturnsTheExpectedElement()
    {
        // Given
        T aElement = randomElement();

        // When
        Iterator<T> aIterator = createSingletonSequence(aElement).iterator();

        // Then
        assertEquals(aElement, aIterator.next());
        assertFalse(aIterator.hasNext());
    }
}
