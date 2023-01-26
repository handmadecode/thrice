/*
 * Copyright 2013, 2015, 2017, 2020-2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
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
abstract public class ReferenceSequenceBaseTest extends SequenceBaseTest<String>
{
    @Override
    protected String randomElement()
    {
        char[] aChars = new char[ThreadLocalRandom.current().nextInt(1, 64)];
        for (int i=0; i<aChars.length; i++)
            aChars[i] = (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1);

        return new String(aChars);
    }


    @Override
    protected String[] newArray(int pLength)
    {
        return new String[pLength];
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
            String[] aElements = randomElementArray(aElementCount);

            // When
            Sequence<String> aSequence = createSequence(aElements);

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
            String[] aElements = randomElementArray(aElementCount);
            Consumer<String> aConsumer = createMockConsumer();

            // When
            createSequence(aElements).forEach(aConsumer);

            // Then (the sequence should pass the instance from the underlying collection to the
            // consumer)
            for (String aElement : aElements)
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
            String[] aElements = randomElementArray(aElementCount);
            Sequence<String> aSequence = createSequence(aElements);

            // When
            Iterator<String> aIterator = aSequence.iterator();

            // Then
            for (int i=0; i<aSequence.size(); i++)
                assertSame(aSequence.elementAt(i), aIterator.next());
        }
    }


    /**
     * It should be possible to cast a sequence to a sequence of a supertype of its elements.
     */
    @Test
    public void sequenceCanBeUpCast()
    {
        // Given
        Sequence<String> aSequence = createSequence(randomCollectionLength());

        // When
        Sequence<CharSequence> aUpCastSequence = Sequences.upCast(aSequence);

        // Then
        Iterator<CharSequence> aIterator = aUpCastSequence.iterator();
        for (int i=0; i<aSequence.size(); i++)
            assertSame(aSequence.elementAt(i), aIterator.next());
    }
}
