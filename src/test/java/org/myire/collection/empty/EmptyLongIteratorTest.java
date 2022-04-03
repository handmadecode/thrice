/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.myire.collection.PrimitiveIterators.emptyLongIterator;


/**
 * Unit tests for the {@code PrimitiveIterator.OfLong} implementation returned by
 * {@link org.myire.collection.PrimitiveIterators#emptyLongIterator()}.
 */
public class EmptyLongIteratorTest extends EmptyIteratorBaseTest<Long>
{
    @Override
    protected PrimitiveIterator.OfLong createIterator()
    {
        return emptyLongIterator();
    }


    /**
     * The {@code nextLong} method should throw a {@code NoSuchElementException}.
     */
    @Test
    public void nextLongThrows()
    {
        // Given
        PrimitiveIterator.OfLong aIterator = createIterator();

        // Then
        assertThrows(
            NoSuchElementException.class,
            aIterator::nextLong
        );
    }


    /**
     * The {@code forEachRemaining} method should not invoke the specified action.
     */
    @Test
    public void primitiveForEachRemainingDoesNothing()
    {
        // Given
        LongConsumer aAction = mock(LongConsumer.class);
        PrimitiveIterator.OfLong aIterator = createIterator();

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verifyNoInteractions(aAction);
    }
}
