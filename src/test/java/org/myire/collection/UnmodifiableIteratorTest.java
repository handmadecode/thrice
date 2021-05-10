/*
 * Copyright 2009, 2016-2017, 2020, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.myire.collection.Iterators.unmodifiableIterator;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link Iterators#unmodifiableIterator(Iterator)}.
 */
public class UnmodifiableIteratorTest extends IteratorBaseTest
{
    @Override
    protected <T> Iterator<T> createIterator(T[] pElements)
    {
        if (pElements.length == 0)
            return Collections.emptyIterator();
        else
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
}
