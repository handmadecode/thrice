/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.myire.collection.PrimitiveIterators.emptyDoubleIterator;


/**
 * Unit tests for the {@code PrimitiveIterator.OfDouble} implementation returned by
 * {@link org.myire.collection.PrimitiveIterators#emptyDoubleIterator()}.
 */
public class EmptyDoubleIteratorTest extends EmptyIteratorBaseTest<Double>
{
    @Override
    protected PrimitiveIterator.OfDouble createIterator()
    {
        return emptyDoubleIterator();
    }


    /**
     * The {@code nextDouble} method should throw a {@code NoSuchElementException}.
     */
    @Test
    public void nextDoubleThrows()
    {
        // Given
        PrimitiveIterator.OfDouble aIterator = createIterator();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::nextDouble
        );
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action.
     */
    @Test
    public void primitiveForEachRemainingDoesNothing()
    {
        // Given
        DoubleConsumer aAction = mock(DoubleConsumer.class);
        PrimitiveIterator.OfDouble aIterator = createIterator();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoInteractions(aAction);
    }
}
