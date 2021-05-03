/*
 * Copyright 2013, 2015, 2017, 2020, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Base test class for {@code Sequence} implementations.
 */
abstract public class SequenceBaseTest
{
    /**
     * Create an instance of the sequence to test that contains no elements.
     *
     * @param <T>   The type of the elements in the sequence.
     *
     * @return  An empty sequence.
     */
    abstract protected <T> Sequence<T> createEmptySequence();

    /**
     * Create an instance of the sequence to test.
     *
     * @param pElements The sequence's elements.
     * @param <T>   The type of the elements in the sequence.
     *
     * @return  A sequence with the specified elements.
     */
    abstract protected <T> Sequence<T> createSequence(T[] pElements);


    /**
     * {@code size()} should return 0 for an empty sequence.
     */
    @Test
    public void sizeReturnsZeroForEmptySequence()
    {
        assertEquals(0, createEmptySequence().size());
    }


    /**
     * {@code size()} should return the number of elements in the sequence.
     */
    @Test
    public void sizeReturnsTheExpectedValue()
    {
        // Given
        Object[] aElements = randomLengthElementArray();

        // When
        Sequence<Object> aSequence = createSequence(aElements);

        // Then
        assertEquals(aElements.length, aSequence.size());
    }


    /**
     * {@code elementAt()} should return the expected element.
     */
    @Test
    public void elementAtReturnsTheExpectedElement()
    {
        // Given
        Object[] aElements = randomLengthElementArray();

        // When
        Sequence<Object> aSequence = createSequence(aElements);

        // Then
        for (int i=0; i<aElements.length; i++)
            assertSame(aElements[i], aSequence.elementAt(i));
    }


    /**
     * A sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt()} is
     * called with a negative index.
     */
    @Test
    public void elementAThrowsForNegativeIndex()
    {
        // Given
        Sequence<Object> aSequence = createSequence(randomLengthElementArray());

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> aSequence.elementAt(-2)
        );
    }


    /**
     * A sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt()} is
     * called with a too large index.
     */
    @Test
    public void elementAThrowsForTooLargeIndex()
    {
        // Given
        Object[] aElements = randomLengthElementArray();
        Sequence<Object> aSequence = createSequence(aElements);

        // When
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> aSequence.elementAt(aElements.length)
        );
    }


    /**
     * An empty sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt()}
     * is called with any index.
     */
    @Test
    public void elementAThrowsForEmptySequence()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> createEmptySequence().elementAt(0)
        );
    }


    /**
     *  {@code forEach()} should invoke the specified action on each element in the sequence.
     */
    @Test
    public void forEachPassesAllElementsToAction()
    {
        // Given
        Object[] aElements = randomLengthElementArray();
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        createSequence(aElements).forEach(aConsumer);

        // Then
        for (Object aElement : aElements)
            verify(aConsumer).accept(aElement);

        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * An empty sequence should not invoke the action when {@code forEach()} is called.
     */
    @Test
    public void emptySequenceDoesNotInvokeForEachAction()
    {
        // Given
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        createEmptySequence().forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence should return an iterator that iterates over the sequence's elements in the same
     * order as the elements are returned by {@code elementAt()}.
     */
    @Test
    public void iteratorIteratesOverAllElements()
    {
        // Given
        Sequence<Object> aSequence = createSequence(randomLengthElementArray());

        // When
        Iterator<Object> aIterator = aSequence.iterator();

        // Then
        for (int i=0; i<aSequence.size(); i++)
            assertSame(aSequence.elementAt(i), aIterator.next());
    }


    /**
     * An empty sequence should return an empty iterator.
     */
    @Test
    public void emptySequenceReturnsEmptyIterator()
    {
        // Given
        Sequence<Object> aSequence = createEmptySequence();

        // When
        Iterator<Object> aIterator = aSequence.iterator();

        // Then
        assertFalse(aIterator.hasNext());
    }


    static private Object[] randomLengthElementArray()
    {
        Object[] aElements = new Object[ThreadLocalRandom.current().nextInt(1, 256)];
        for (int i=0; i<aElements.length; i++)
            aElements[i] = new Object();

        return aElements;
    }


    /**
     * Mock a {@code Consumer<T>}, suppressing the unchecked cast.
     *
     * @param <T>   The type to consume.
     *
     * @return  A mocked {@code Consumer<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Consumer<T> newMockConsumer()
    {
        return (Consumer<T>) mock(Consumer.class);
    }
}
