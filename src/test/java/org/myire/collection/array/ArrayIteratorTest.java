/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import java.util.Iterator;

import static org.myire.collection.Iterators.arrayIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.myire.collection.ReferenceIteratorBaseTest;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link org.myire.collection.Iterators#arrayIterator(Object[])}.
 */
public class ArrayIteratorTest extends ReferenceIteratorBaseTest
{
    @Override
    protected Iterator<Object> createIterator(Object[] pElements)
    {
        return arrayIterator(pElements);
    }


    /**
     * The factory method {@code Iterators.arrayIterator} should throw a
     * {@code NullPointerException} when passed a null array argument.
     */
    @Test
    public void factoryMethodThrowsForNullArrayArgument()
    {
        assertThrows(
            NullPointerException.class,
            () -> arrayIterator(null)
        );
    }
}
