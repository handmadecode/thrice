/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleConsumer;

import org.myire.collection.DoubleSequence;
import org.myire.collection.PrimitiveSequences;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.randomDoubleInstance;


/**
 * Unit tests for the {@code Sequence} implementation returned by
 * {@link PrimitiveSequences#singleton(double)}.
 */
public class SingletonDoubleSequenceTest extends SingletonPrimitiveSequenceBaseTest<Double>
{
    @Override
    protected Double randomElement()
    {
        return randomDoubleInstance();
    }


    @Override
    protected DoubleSequence createSingletonSequence(Double pElement)
    {
        return PrimitiveSequences.singleton((pElement.doubleValue()));
    }


    /**
     * A {@code SingletonDoubleSequence} should contain the value specified in the factory method.
     */
    @Test
    public void singletonDoubleSequenceContainsExpectedElement()
    {
        // Given
        double aValue = ThreadLocalRandom.current().nextDouble();

        // Then
        assertEquals(aValue, PrimitiveSequences.singleton(aValue).valueAt(0));
    }


    /**
     * A {@code SingletonDoubleSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a negative index.
     */
    @Test
    public void singletonDoubleSequenceThrowsForValueAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.singleton(1.0d).valueAt(-1)
        );
    }


    /**
     * A {@code SingletonDoubleSequence} should throw an {@code IndexOutOfBoundsException} when
     * {@code valueAt()} is called with a positive index.
     */
    @Test
    public void singletonDoubleSequenceThrowsForValueAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> PrimitiveSequences.singleton(1.0d).valueAt(1)
        );
    }


    /**
     * A {@code SingletonDoubleSequence} should invoke the action on its element when
     * {@code forEach(DoubleConsumer)} is called.
     */
    @Test
    public void singletonDoubleSequenceInvokesForEachActionOnElement()
    {
        // Given
        double aValue = ThreadLocalRandom.current().nextDouble();
        DoubleConsumer aConsumer = mock(DoubleConsumer.class);

        // When
        PrimitiveSequences.singleton(aValue).forEach(aConsumer);

        // Then
        verify(aConsumer).accept(eq(aValue));
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A {@code SingletonDoubleSequence} should return a {@code PrimitiveIterator.OfDouble} with the
     * sequence's single value.
     */
    @Test
    public void singletonDoubleSequencePrimitiveIteratorContainsTheExpectedValue()
    {
        // Given
        double aValue = ThreadLocalRandom.current().nextDouble();

        // When
        PrimitiveIterator.OfDouble aIterator = PrimitiveSequences.singleton(aValue).iterator();

        // Then
        assertEquals(aValue, aIterator.nextDouble());
        assertFalse(aIterator.hasNext());
    }
}
