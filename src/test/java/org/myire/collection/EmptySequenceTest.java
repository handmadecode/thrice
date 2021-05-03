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
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Unit tests for the {@code Sequence} implementation returned by {@link Sequences#emptySequence()}.
 */
public class EmptySequenceTest
{
    /**
     * The sequence returned by {@code Sequences.emptySequence()} should have size 0.
     */
    @Test
    public void emptySequenceHasSize0()
    {
        assertEquals(0, Sequences.emptySequence().size());
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt(0)} is called.
     */
    @Test
    public void emptySequenceThrowsForElementAtIndex0()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Sequences.emptySequence().elementAt(0)
        );
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a positive index.
     */
    @Test
    public void emptySequenceThrowsForElementAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Sequences.emptySequence().elementAt(1)
        );
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a negative index.
     */
    @Test
    public void emptySequenceThrowsForElementAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () ->
                Sequences.emptySequence().elementAt(-1)
        );
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should not invoke the action when
     * {@code forEach()} is called.
     */
    @Test
    public void emptySequenceDoesNotInvokeForEachAction()
    {
        // Given
        Consumer<Object> aConsumer = SequenceBaseTest.newMockConsumer();

        // When
        Sequences.emptySequence().forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should return an empty
     * {@code Iterator} from its {@code iterator()} method.
     */
    @Test
    public void emptySequenceIteratorHasNoElements()
    {
        // Given
        Iterator<Object> aIterator = Sequences.emptySequence().iterator();

        // Then
        assertFalse(aIterator.hasNext());
    }
}
