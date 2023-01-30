/*
 * Copyright 2013, 2015, 2017, 2020-2022 Peter Franzen. All rights reserved.
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base test class for {@code Sequence} implementations, including primitive sequences.
 *
 * @param <T>   The element type used in the tests.
 */
abstract public class SequenceBaseTest<T> extends CollectionBaseTest<T>
{
    /**
     * Create an instance of the sequence to test.
     *
     * @param pElements The sequence's elements.
     *
     * @return  A sequence with the specified elements.
     */
    abstract protected Sequence<T> createSequence(T[] pElements);

    /**
     * Create an instance of the sequence to test.
     *
     * @param pNumElements  The number of elements in the sequence.
     *
     * @return  A sequence with the specified number of random elements.
     */
    protected Sequence<T> createSequence(int pNumElements)
    {
        return createSequence(randomElementArray(pNumElements));
    }


    /**
     * {@code size()} should return the number of elements in the sequence.
     */
    @Test
    public void sizeReturnsTheExpectedValue()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // When
            Sequence<T> aSequence = createSequence(aElementCount);

            // Then
            assertEquals(aElementCount, aSequence.size());
        }
    }


    /**
     * A sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt()} is
     * called with a negative index.
     */
    @Test
    public void elementAtThrowsForNegativeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Sequence<T> aSequence = createSequence(aElementCount);
            int aIndex = ThreadLocalRandom.current().nextInt(-10, 0);

            // When
            assertThrows(
                IndexOutOfBoundsException.class,
                () -> aSequence.elementAt(aIndex)
            );
        }
    }


    /**
     * A sequence should throw an {@code IndexOutOfBoundsException} when {@code elementAt()} is
     * called with a too large index.
     */
    @Test
    public void elementAtThrowsForTooLargeIndex()
    {
        int[] aElementCounts = {0, 1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Sequence<T> aSequence = createSequence(aElementCount);

            // When
            assertThrows(
                IndexOutOfBoundsException.class,
                () -> aSequence.elementAt(aElementCount)
            );
        }
    }


    /**
     * A sequence with zero elements should not invoke the action when {@code forEach()} is called.
     */
    @Test
    public void emptySequenceDoesNotInvokeForEachAction()
    {
        // Given
        Consumer<T> aConsumer = createMockConsumer();

        // When
        createSequence(0).forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence with zero elements should return an empty iterator.
     */
    @Test
    public void emptySequenceReturnsEmptyIterator()
    {
        // Given
        Sequence<T> aSequence = createSequence(0);

        // When
        Iterator<T> aIterator = aSequence.iterator();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * A sequence's iterator should not support the remove operation.
     */
    @Test
    public void iteratorDoesNotSupportRemove()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Sequence<T> aSequence = createSequence(aElementCount);

            // When
            Iterator<T> aIterator = aSequence.iterator();
            for (int i = aElementCount-1; i >= 0; i--)
                aIterator.next();

            // Then
            assertThrows(
                UnsupportedOperationException.class,
                aIterator::remove
            );
        }
    }
}
