/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base class for unit tests of {@code PrimitiveSequence} subclasses. Note that the tests in this
 * base class are almost identical to the ones in {@code ReferenceSequenceBaseTest}. The difference
 * is that the tests in this class must use {@code assertEquals} instead of {@code assertSame} when
 * comparing elements in sequences created from arrays, since the elements are boxed primitives that
 * may not be the same instance in the array and in the sequence.
 */
abstract public class PrimitiveSequenceBaseTest<T extends Number> extends SequenceBaseTest<T>
{
    /**
     * {@code elementAt()} should return the expected element.
     */
    @Test
    public void elementAtReturnsTheExpectedElement()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            T[] aElements = randomElementArray(aElementCount);

            // When
            Sequence<T> aSequence = createSequence(aElements);

            // Then
            for (int i=0; i<aElements.length; i++)
                assertEquals(aElements[i], aSequence.elementAt(i));
        }
    }


    /**
     * {@code forEach()} should invoke the specified action on each element in the sequence.
     */
    @Test
    public void forEachPassesAllElementsToAction()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            T[] aElements = randomElementArray(aElementCount);
            Consumer<T> aConsumer = createMockConsumer();

            // When
            createSequence(aElements).forEach(aConsumer);

            // Then
            for (T aElement : aElements)
                verify(aConsumer).accept(eq(aElement));

            verifyNoMoreInteractions(aConsumer);
        }
    }


    /**
     * A sequence should return an iterator that iterates over the sequence's elements in the same
     * order as the elements are returned by {@code elementAt()}.
     */
    @Test
    public void iteratorIteratesOverAllElements()
    {
        int[] aElementCounts = {1, randomCollectionLength()};
        for (int aElementCount : aElementCounts)
        {
            // Given
            Sequence<T> aSequence = createSequence(aElementCount);

            // When
            Iterator<T> aIterator = aSequence.iterator();

            // Then
            for (int i=0; i<aSequence.size(); i++)
                assertEquals(aSequence.elementAt(i), aIterator.next());
        }
    }
}
