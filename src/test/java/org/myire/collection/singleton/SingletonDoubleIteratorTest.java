/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

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
import static org.myire.collection.CollectionTests.randomDoubleInstance;


/**
 * Unit tests for the {@code PrimitiveIterator.OfDouble} implementation returned by
 * {@link org.myire.collection.PrimitiveIterators#singletonIterator(double)}.
 */
public class SingletonDoubleIteratorTest extends SingletonIteratorBaseTest<Double>
{
    @Override
    protected Double randomElement()
    {
        return randomDoubleInstance();
    }


    @Override
    protected PrimitiveIterator.OfDouble createIterator(Double pElement)
    {
        return singletonIterator(pElement.doubleValue());
    }


    /**
     * The {@code hasNext} method should return false after the single element has been returned by
     * {@code nextDouble}.
     */
    @Test
    public void hasNextReturnsFalseAfterCallToNextDouble()
    {
        // Given
        PrimitiveIterator.OfDouble aIterator = singletonIterator(randomElement().doubleValue());

        // When
        aIterator.nextDouble();

        // Then
        assertFalse(aIterator.hasNext());
    }


    /**
     * The {@code next} method should return a {@code Double} boxing the value passed to the
     * constructor.
     */
    @Test
    public void nextReturnsTheExpectedElement()
    {
        // Given
        double aValue = randomElement().doubleValue();
        PrimitiveIterator.OfDouble aIterator = singletonIterator(aValue);

        // Then
        assertEquals(aValue, aIterator.next().doubleValue());
    }


    /**
     * The {@code nextDouble} method should return the value passed to the constructor.
     */
    @Test
    public void nextDoubleReturnsTheExpectedValue()
    {
        // Given
        double aValue = randomElement().doubleValue();
        PrimitiveIterator.OfDouble aIterator = singletonIterator(aValue);

        // Then
        assertEquals(aValue, aIterator.nextDouble());
    }


    /**
     * The {@code nextDouble} method should throw a {@code NoSuchElementException} on all calls except
     * the first.
     */
    @Test
    public void nextDoubleThrowsAfterReturningTheElement()
    {
        // Given
        PrimitiveIterator.OfDouble aIterator = singletonIterator(randomElement().doubleValue());

        // When
        aIterator.nextDouble();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::nextDouble
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
        PrimitiveIterator.OfDouble aIterator = singletonIterator(randomElement().doubleValue());

        // When
        aIterator.nextDouble();

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
        Consumer<Double> aAction = createMockConsumer();

        // Given
        double aValue = randomElement().doubleValue();
        PrimitiveIterator.OfDouble aIterator = singletonIterator(aValue);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verify(aAction).accept(eq(Double.valueOf(aValue)));
        verifyNoMoreInteractions(aAction);
    }


    /**
     * The {@code forEachRemaining} method should pass the value to the specified action if
     * {@code nextDouble} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesTheValue()
    {
        // Given
        DoubleConsumer aAction = mock(DoubleConsumer.class);

        // Given
        double aValue = randomElement().doubleValue();
        PrimitiveIterator.OfDouble aIterator = singletonIterator(aValue);

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
        DoubleConsumer aAction = mock(DoubleConsumer.class);

        // Given
        double aValue = randomElement().doubleValue();
        PrimitiveIterator.OfDouble aIterator = singletonIterator(aValue);

        // When
        aIterator.nextDouble();
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoInteractions(aAction);
    }
}
