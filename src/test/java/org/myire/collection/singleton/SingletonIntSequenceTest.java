/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;

import org.myire.collection.IntSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.randomIntegerInstance;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link PrimitiveSequences#singleton(int)}.
 */
public class SingletonIntSequenceTest extends SingletonPrimitiveSequenceBaseTest<Integer>
{
    @Override
    protected Integer randomElement()
    {
        return randomIntegerInstance();
    }


    @Override
    protected IntSequence createSingletonSequence(Integer pElement)
    {
        return PrimitiveSequences.singleton((pElement.intValue()));
    }


    /**
     * A {@code SingletonIntSequence} should contain the value specified in the factory method.
     */
    @Test
    public void singletonIntSequenceContainsExpectedElement()
    {
        // Given
        int aValue = ThreadLocalRandom.current().nextInt();

        // Then
        assertEquals(aValue, PrimitiveSequences.singleton(aValue).valueAt(0));
    }


    /**
     * A {@code SingletonIntSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void singletonIntSequenceThrowsForValueAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.singleton(1).valueAt(-1)
        );
    }


    /**
     * A {@code SingletonIntSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a positive index.
     */
    @Test
    public void singletonIntSequenceThrowsForValueAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.singleton(1).valueAt(1)
        );
    }


    /**
     * A {@code SingletonIntSequence} should invoke the action on its element when
     * {@code forEach(IntConsumer)} is called.
     */
    @Test
    public void singletonIntSequenceInvokesForEachActionOnElement()
    {
        // Given
        int aValue = ThreadLocalRandom.current().nextInt();
        IntConsumer aConsumer = mock(IntConsumer.class);

        // When
        PrimitiveSequences.singleton(aValue).forEach(aConsumer);

        // Then
        verify(aConsumer).accept(eq(aValue));
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A {@code SingletonIntSequence} should return a {@code PrimitiveIterator.OfInt} with the
     * sequence's single value.
     */
    @Test
    public void singletonIntSequencePrimitiveIteratorContainsTheExpectedValue()
    {
        // Given
        int aValue = ThreadLocalRandom.current().nextInt();

        // When
        PrimitiveIterator.OfInt aIterator = PrimitiveSequences.singleton(aValue).iterator();

        // Then
        assertEquals(aValue, aIterator.nextInt());
        assertFalse(aIterator.hasNext());
    }
}
