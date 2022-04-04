/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.function.IntConsumer;

import org.myire.collection.IntSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link PrimitiveSequences#emptyIntSequence()}.
 */
public class EmptyIntSequenceTest extends EmptySequenceBaseTest<Integer>
{
    @Override
    IntSequence createEmptySequence()
    {
        return PrimitiveSequences.emptyIntSequence();
    }


    /**
     * An empty {@code IntSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt(0)} is called.
     */
    @Test
    public void emptyIntSequenceThrowsForValueAtIndex0()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyIntSequence().valueAt(0)
        );
    }


    /**
     * An empty {@code IntSequence} should throw an  {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a positive index.
     */
    @Test
    public void emptyIntSequenceThrowsForValueAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyIntSequence().valueAt(1)
        );
    }


    /**
     * An empty {@code IntSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void emptyIntSequenceThrowsForValueAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyIntSequence().valueAt(-1)
        );
    }


    /**
     * An empty {@code IntSequence} should not invoke the action when {@code forEach(IntConsumer)}
     * is called.
     */
    @Test
    public void emptyIntSequenceDoesNotInvokePrimitiveForEachAction()
    {
        // Given
        IntConsumer aConsumer = mock(IntConsumer.class);

        // When
        PrimitiveSequences.emptyIntSequence().forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }
}
