/*
 * Copyright 2013, 2015, 2017, 2020-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Base test class for non-primitive {@code Sequence} implementations.
 */
abstract public class ReferenceSequenceBaseTest extends SequenceBaseTest<Object>
{
    @Override
    protected Object randomElement()
    {
        return new Object();
    }


    @Override
    protected Object[] newArray(int pLength)
    {
        return new Object[pLength];
    }


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
            Object[] aElements = randomElementArray(aElementCount);

            // When
            Sequence<Object> aSequence = createSequence(aElements);

            // Then (the sequence should return the instance from the underlying collection)
            for (int i=0; i<aElements.length; i++)
                assertSame(aElements[i], aSequence.elementAt(i));

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
            Object[] aElements = randomElementArray(aElementCount);
            Consumer<Object> aConsumer = createMockConsumer();

            // When
            createSequence(aElements).forEach(aConsumer);

            // Then (the sequence should pass the instance from the underlying collection to the
            // consumer)
            for (Object aElement : aElements)
                verify(aConsumer).accept(same(aElement));

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
            Object[] aElements = randomElementArray(aElementCount);
            Sequence<Object> aSequence = createSequence(aElements);

            // When
            Iterator<Object> aIterator = aSequence.iterator();

            // Then
            for (int i=0; i<aSequence.size(); i++)
                assertSame(aSequence.elementAt(i), aIterator.next());
        }
    }
}
