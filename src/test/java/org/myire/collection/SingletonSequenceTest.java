/*
 * Copyright 2013, 2015, 2017, 2020, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link Sequences#singleton(Object)}.
 */
public class SingletonSequenceTest
{
    /**
     * A sequence returned by {@code Sequences.singleton()} should have size 1.
     */
    @Test
    public void singletonSequenceHasSize1()
    {
        assertEquals(1, Sequences.singleton(null).size());
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
     * A sequence returned by {@code Sequences.singleton()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a negative index.
     */
    @Test
    public void singletonSequenceThrowsForElementAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Sequences.singleton("").elementAt(-1)
        );
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a positive index.
     */
    @Test
    public void singletonSequenceThrowsForElementAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Sequences.singleton("").elementAt(1)
        );
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
        Consumer<Object> aConsumer = SequenceBaseTest.newMockConsumer();

        // When
        Sequences.singleton(aElement).forEach(aConsumer);

        // Then
        verify(aConsumer).accept(aElement);
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
