/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import static org.myire.collection.PrimitiveIterators.singletonIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;
import static org.myire.collection.CollectionTests.randomLongInstance;


/**
 * Unit tests for the {@code PrimitiveIterator.OfLong} implementation returned by
 * {@link org.myire.collection.PrimitiveIterators#singletonIterator(long)}.
 */
public class SingletonLongIteratorTest extends SingletonIteratorBaseTest<Long>
{
    @Override
    protected Long randomElement()
    {
        return randomLongInstance();
    }


    @Override
    protected PrimitiveIterator.OfLong createIterator(Long pElement)
    {
        return singletonIterator(pElement.longValue());
    }


    /**
     * The {@code hasNext} method should return false after the single element has been returned by
     * {@code nextLong}.
     */
    @Test
    public void hasNextReturnsFalseAfterCallToNextLong()
    {
        // Given
        PrimitiveIterator.OfLong aIterator = singletonIterator(randomElement().longValue());

        // When
        aIterator.nextLong();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code next} method should return a {@code Long} boxing the value passed to the
     * constructor.
     */
    @Test
    public void nextReturnsTheExpectedElement()
    {
        // Given
        long aValue = randomElement().longValue();
        PrimitiveIterator.OfLong aIterator = singletonIterator(aValue);

        // Then
        assertEquals(aValue, aIterator.next().longValue());
    }


    /**
     * The {@code nextLong} method should return the value passed to the constructor.
     */
    @Test
    public void nextLongReturnsTheExpectedValue()
    {
        // Given
        long aValue = randomElement().longValue();
        PrimitiveIterator.OfLong aIterator = singletonIterator(aValue);

        // Then
        assertEquals(aValue, aIterator.nextLong());
    }


    /**
     * The {@code nextLong} method should throw a {@code NoSuchElementException} on all calls except
     * the first.
     */
    @Test
    public void nextLongThrowsAfterReturningTheElement()
    {
        // Given
        PrimitiveIterator.OfLong aIterator = singletonIterator(randomElement().longValue());

        // When
        aIterator.nextLong();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::nextLong
        );
    }


    /**
     * The {@code remove} method should throw an {@code UnsupportedOperationException} at the end of
     * the iteration.
     */
    @Test
    public void removeThrowsAtEndOfIteration()
    {
        // Given
        PrimitiveIterator.OfLong aIterator = singletonIterator(randomElement().longValue());

        // When
        aIterator.nextLong();

        // Then
        assertThrows(
            UnsupportedOperationException.class,
            aIterator::remove
        );
    }


    /**
     * The {@code forEachRemaining} method should pass the element to the specified action if
     * {@code next} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesTheElement()
    {
        // Given
        Consumer<Long> aAction = createMockConsumer();

        // Given
        long aValue = randomElement().longValue();
        PrimitiveIterator.OfLong aIterator = singletonIterator(aValue);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verify(aAction).accept(eq(Long.valueOf(aValue)));
        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should pass the value to the specified action if
     * {@code nextLong} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesTheValue()
    {
        // Given
        LongConsumer aAction = mock(LongConsumer.class);

        // Given
        long aValue = randomElement().longValue();
        PrimitiveIterator.OfLong aIterator = singletonIterator(aValue);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verify(aAction).accept(eq(aValue));
        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action if the value has
     * been returned by the iteration.
     */
    @Test
    public void forEachRemainingDoesNothingWhenValueHasBeenReturned()
    {
        // Given
        LongConsumer aAction = mock(LongConsumer.class);

        // Given
        long aValue = randomElement().longValue();
        PrimitiveIterator.OfLong aIterator = singletonIterator(aValue);

        // When
        aIterator.nextLong();
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoInteractions(aAction);
    }
}
