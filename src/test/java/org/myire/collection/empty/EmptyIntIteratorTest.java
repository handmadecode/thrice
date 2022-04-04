/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.myire.collection.PrimitiveIterators.emptyIntIterator;


/**
 * Unit tests for the {@code PrimitiveIterator.OfInt} implementation returned by
 * {@link org.myire.collection.PrimitiveIterators#emptyIntIterator()}.
 */
public class EmptyIntIteratorTest extends EmptyIteratorBaseTest<Integer>
{
    @Override
    protected PrimitiveIterator.OfInt createIterator()
    {
        return emptyIntIterator();
    }


    /**
     * The {@code nextInt} method should throw a {@code NoSuchElementException}.
     */
    @Test
    public void nextIntThrows()
    {
        // Given
        PrimitiveIterator.OfInt aIterator = createIterator();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::nextInt
        );
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action.
     */
    @Test
    public void primitiveForEachRemainingDoesNothing()
    {
        // Given
        IntConsumer aAction = mock(IntConsumer.class);
        PrimitiveIterator.OfInt aIterator = createIterator();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoInteractions(aAction);
    }
}
