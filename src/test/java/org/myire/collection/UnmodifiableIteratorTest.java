/*
 * Copyright 2009, 2016-2017, 2020-2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.myire.collection.Iterators.unmodifiableIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link Iterators#unmodifiableIterator(Iterator)}.
 */
public class UnmodifiableIteratorTest extends ReferenceIteratorBaseTest
{
    @Override
    protected Iterator<Object> createIterator(Object[] pElements)
    {
        return unmodifiableIterator(Arrays.asList(pElements).iterator());
    }


    /**
     * The factory method {@code Iterators.unmodifiableIterator} should throw a
     * {@code NullPointerException} when passed a null argument.
     */
    @SuppressWarnings("unused")
    @Test
    public void factoryMethodThrowsForNullArgument()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                unmodifiableIterator(null)
        );
    }


    /**
     * {@link Iterators#unmodifiableIterator(Iterator)} should be able to wrap an iterator whose
     * elements are of a subtype of the unmodifiable iterator's elements.
     */
    @Test
    public void iteratorCanWrapIteratorOfSubType()
    {
        // Given
        List<String> aList = Arrays.asList("A", "B", "C", "D");

        // When
        Iterator<CharSequence> aIterator = unmodifiableIterator(aList.iterator());

        // Then
        for (String aElement : aList)
            assertSame(aElement, aIterator.next());

        assertFalse(aIterator.hasNext());
    }
}
