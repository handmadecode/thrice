/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

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
import static org.myire.collection.CollectionTests.randomIntegerInstance;


/**
 * Unit tests for the {@code PrimitiveIterator.OfInt} implementation returned by
 * {@link org.myire.collection.PrimitiveIterators#singletonIterator(int)}.
 */
public class SingletonIntIteratorTest extends SingletonIteratorBaseTest<Integer>
{
    @Override
    protected Integer randomElement()
    {
        return randomIntegerInstance();
    }


    @Override
    protected PrimitiveIterator.OfInt createIterator(Integer pElement)
    {
        return singletonIterator(pElement.intValue());
    }


    /**
     * The {@code hasNext} method should return false after the single element has been returned by
     * {@code nextInt}.
     */
    @Test
    public void hasNextReturnsFalseAfterCallToNextInt()
    {
        // Given
        PrimitiveIterator.OfInt aIterator = singletonIterator(randomElement().intValue());

        // When
        aIterator.nextInt();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code next} method should return an {@code Integer} boxing the value passed to the
     * constructor.
     */
    @Test
    public void nextReturnsTheExpectedElement()
    {
        // Given
        int aValue = randomElement().intValue();
        PrimitiveIterator.OfInt aIterator = singletonIterator(aValue);

        // Then
        assertEquals(aValue, aIterator.next().intValue());
    }


    /**
     * The {@code nextInt} method should return the value passed to the constructor.
     */
    @Test
    public void nextIntReturnsTheExpectedValue()
    {
        // Given
        int aValue = randomElement().intValue();
        PrimitiveIterator.OfInt aIterator = singletonIterator(aValue);

        // Then
        assertEquals(aValue, aIterator.nextInt());
    }


    /**
     * The {@code nextInt} method should throw a {@code NoSuchElementException} on all calls except
     * the first.
     */
    @Test
    public void nextIntThrowsAfterReturningTheElement()
    {
        // Given
        PrimitiveIterator.OfInt aIterator = singletonIterator(randomElement().intValue());

        // When
        aIterator.nextInt();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::nextInt
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
        PrimitiveIterator.OfInt aIterator = singletonIterator(randomElement().intValue());

        // When
        aIterator.nextInt();

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
        Consumer<Integer> aAction = createMockConsumer();

        // Given
        int aValue = randomElement().intValue();
        PrimitiveIterator.OfInt aIterator = singletonIterator(aValue);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verify(aAction).accept(eq(Integer.valueOf(aValue)));
        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should pass the value to the specified action if
     * {@code nextInt} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesTheValue()
    {
        // Given
        IntConsumer aAction = mock(IntConsumer.class);

        // Given
        int aValue = randomElement().intValue();
        PrimitiveIterator.OfInt aIterator = singletonIterator(aValue);

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
        IntConsumer aAction = mock(IntConsumer.class);

        // Given
        int aValue = randomElement().intValue();
        PrimitiveIterator.OfInt aIterator = singletonIterator(aValue);

        // When
        aIterator.nextInt();
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoInteractions(aAction);
    }
}
