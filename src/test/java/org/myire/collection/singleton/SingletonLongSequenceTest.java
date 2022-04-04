/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongConsumer;

import org.myire.collection.LongSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.randomLongInstance;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link PrimitiveSequences#singleton(long)}.
 */
public class SingletonLongSequenceTest extends SingletonPrimitiveSequenceBaseTest<Long>
{
    @Override
    protected Long randomElement()
    {
        return randomLongInstance();
    }


    @Override
    protected LongSequence createSingletonSequence(Long pElement)
    {
        return PrimitiveSequences.singleton((pElement.longValue()));
    }


    /**
     * A {@code SingletonLongSequence} should contain the value specified in the factory method.
     */
    @Test
    public void singletonLongSequenceContainsExpectedElement()
    {
        // Given
        long aValue = ThreadLocalRandom.current().nextLong();

        // Then
        assertEquals(aValue, PrimitiveSequences.singleton(aValue).valueAt(0));
    }


    /**
     * A {@code SingletonLongSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void singletonLongSequenceThrowsForValueAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.singleton(1L).valueAt(-1)
        );
    }


    /**
     * A {@code SingletonLongSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a positive index.
     */
    @Test
    public void singletonLongSequenceThrowsForValueAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.singleton(1L).valueAt(1)
        );
    }


    /**
     * A {@code SingletonLongSequence} should invoke the action on its element when
     * {@code forEach(LongConsumer)} is called.
     */
    @Test
    public void singletonLongSequenceInvokesForEachActionOnElement()
    {
        // Given
        long aValue = ThreadLocalRandom.current().nextLong();
        LongConsumer aConsumer = mock(LongConsumer.class);

        // When
        PrimitiveSequences.singleton(aValue).forEach(aConsumer);

        // Then
        verify(aConsumer).accept(eq(aValue));
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A {@code SingletonLongSequence} should return a {@code PrimitiveIterator.OfLong} with the
     * sequence's single value.
     */
    @Test
    public void singletonLongSequencePrimitiveIteratorContainsTheExpectedValue()
    {
        // Given
        long aValue = ThreadLocalRandom.current().nextLong();

        // When
        PrimitiveIterator.OfLong aIterator = PrimitiveSequences.singleton(aValue).iterator();

        // Then
        assertEquals(aValue, aIterator.nextLong());
        assertFalse(aIterator.hasNext());
    }
}
