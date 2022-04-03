/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.function.LongConsumer;

import org.myire.collection.LongSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link PrimitiveSequences#emptyLongSequence()}.
 */
public class EmptyLongSequenceTest extends EmptySequenceBaseTest<Long>
{
    @Override
    LongSequence createEmptySequence()
    {
        return PrimitiveSequences.emptyLongSequence();
    }


    /**
     * An empty {@code LongSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt(0)} is called.
     */
    @Test
    public void emptyLongSequenceThrowsForValueAtIndex0()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyLongSequence().valueAt(0)
        );
    }


    /**
     * An empty {@code LongSequence} should throw an  {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a positive index.
     */
    @Test
    public void emptyLongSequenceThrowsForValueAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyLongSequence().valueAt(1)
        );
    }


    /**
     * An empty {@code LongSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void emptyLongSequenceThrowsForValueAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyLongSequence().valueAt(-1)
        );
    }


    /**
     * An empty {@code LongSequence} should not invoke the action when {@code forEach(LongConsumer)}
     * is called.
     */
    @Test
    public void emptyLongSequenceDoesNotInvokePrimitiveForEachAction()
    {
        // Given
        LongConsumer aConsumer = mock(LongConsumer.class);

        // When
        PrimitiveSequences.emptyLongSequence().forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }
}
