/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.function.DoubleConsumer;

import org.myire.collection.DoubleSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link PrimitiveSequences#emptyDoubleSequence()}.
 */
public class EmptyDoubleSequenceTest extends EmptySequenceBaseTest<Double>
{
    @Override
    DoubleSequence createEmptySequence()
    {
        return PrimitiveSequences.emptyDoubleSequence();
    }


    /**
     * An empty {@code DoubleSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt(0)} is called.
     */
    @Test
    public void emptyDoubleSequenceThrowsForValueAtIndex0()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyDoubleSequence().valueAt(0)
        );
    }


    /**
     * An empty {@code DoubleSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a positive index.
     */
    @Test
    public void emptyDoubleSequenceThrowsForValueAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyDoubleSequence().valueAt(1)
        );
    }


    /**
     * An empty {@code DoubleSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void emptyDoubleSequenceThrowsForValueAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.emptyDoubleSequence().valueAt(-1)
        );
    }


    /**
     * An empty {@code DoubleSequence} should not invoke the action when
     * {@code forEach(DoubleConsumer)} is called.
     */
    @Test
    public void emptyDoubleSequenceDoesNotInvokePrimitiveForEachAction()
    {
        // Given
        DoubleConsumer aConsumer = mock(DoubleConsumer.class);

        // When
        PrimitiveSequences.emptyDoubleSequence().forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }
}
