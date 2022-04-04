/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import java.util.Iterator;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.same;
import static org.myire.collection.Iterators.singletonIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.myire.collection.CollectionTests.createMockConsumer;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link org.myire.collection.Iterators#singletonIterator(Object)}.
 */
public class SingletonIteratorTest extends SingletonIteratorBaseTest<Object>
{
    @Override
    protected Object randomElement()
    {
        return new Object();
    }


    @Override
    protected Iterator<Object> createIterator(Object pElement)
    {
        return singletonIterator(pElement);
    }


    /**
     * The {@code next} method should return the element passed to the constructor.
     */
    @Test
    public void nextReturnsTheExpectedElement()
    {
        // Given
        String aString = "sole element";
        Iterator<String> aIterator = singletonIterator(aString);

        // Then
        assertSame(aString, aIterator.next());
    }


    /**
     * The {@code forEachRemaining} method should pass the element to the specified action if
     * {@code next} hasn't been called.
     */
    @Test
    public void forEachRemainingProcessesTheElement()
    {
        // Given
        Consumer<String> aAction = createMockConsumer();

        // Given
        String aString = "xyz";
        Iterator<String> aIterator = singletonIterator(aString);

        // When
        aIterator.forEachRemaining(aAction);

        // Then
        verify(aAction).accept(same(aString));
        verifyNoMoreInteractions(aAction);
    }
}
