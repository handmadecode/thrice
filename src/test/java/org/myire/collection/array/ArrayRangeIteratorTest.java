/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.array;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import static org.myire.collection.Iterators.arrayIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.myire.collection.ReferenceIteratorBaseTest;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link org.myire.collection.Iterators#arrayIterator(Object[], int, int)}.
 */
public class ArrayRangeIteratorTest extends ReferenceIteratorBaseTest
{
    @Override
    protected Iterator<Object> createIterator(Object[] pElements)
    {
        if (pElements.length == 0)
            return arrayIterator(new Object[17], 8, 0);

        // Create an array twice the length of the elements to iterate over and put the elements at
        // a random range in that array.
        Object[] aArray = new Object[pElements.length * 2];
        int aOffset = ThreadLocalRandom.current().nextInt(pElements.length + 1);
        System.arraycopy(pElements, 0, aArray, aOffset, pElements.length);
        return arrayIterator(aArray, aOffset, pElements.length);
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
            () -> arrayIterator(null, 0, 1)
        );
    }


    /**
     * The factory method {@code Iterators.arrayIterator} should throw an
     * {@code IllegalArgumentException} when passed a negative offset argument.
     */
    @Test
    public void factoryMethodThrowsForNegativeOffsetArgument()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> arrayIterator(new Object[1], -1, 1)
        );
    }


    /**
     * The factory method {@code Iterators.arrayIterator} should throw an
     * {@code IllegalArgumentException} when passed a negative length argument.
     */
    @Test
    public void factoryMethodThrowsForNegativeLengthArgument()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> arrayIterator(new Object[1], 0, -1)
        );
    }


    /**
     * The factory method {@code Iterators.arrayIterator} should throw an
     * {@code IllegalArgumentException} when passed an invalid offset and length combination.
     */
    @Test
    public void factoryMethodThrowsForInvalidOffsetAndLength()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> arrayIterator(new Object[2], 1, 2)
        );
    }
}
