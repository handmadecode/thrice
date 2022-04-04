/*
 * Copyright 2013, 2015, 2017, 2020-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.Iterator;
import java.util.function.Consumer;

import org.myire.collection.Sequence;
import org.myire.collection.Sequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link Sequences#singleton(Object)}.
 */
public class SingletonSequenceTest extends SingletonSequenceBaseTest<Object>
{
    @Override
    protected Sequence<Object> createSingletonSequence()
    {
        return Sequences.singleton(new Object());
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should contain the element specified in
     * the factory method.
     */
    @Test
    public void singletonSequenceContainsExpectedElement()
    {
        // Given
        Object aElement = new Object();

        // Then
        assertSame(aElement, Sequences.singleton(aElement).elementAt(0));
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should allow its element to be
     * {@code null}.
     */
    @Test
    public void singletonSequenceAllowsNullElement()
    {
        assertNull(Sequences.singleton(null).elementAt(0));
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should invoke the action on its element
     * when {@code forEach()} is called.
     */
    @Test
    public void singletonSequenceInvokesForEachActionOnElement()
    {
        // Given
        Object aElement = new Object();
        Consumer<Object> aConsumer = createMockConsumer();

        // When
        Sequences.singleton(aElement).forEach(aConsumer);

        // Then
        verify(aConsumer).accept(same(aElement));
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * The sequence returned by {@code Sequences.singleton()} should return an {@code Iterator}
     * containing the sequence's single element.
     */
    @Test
    public void singletonSequenceIteratorContainsTheExpectedElement()
    {
        // Given
        Object aElement = new Object();

        // When
        Iterator<Object> aIterator = Sequences.singleton(aElement).iterator();

        // Then
        assertSame(aElement, aIterator.next());
        assertFalse(aIterator.hasNext());
    }
}
