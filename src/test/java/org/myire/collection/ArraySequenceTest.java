/*
 * Copyright 2013, 2015, 2017, 2020, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for the {@code Sequence} implementation returned by {@link Sequences#wrap(Object[])}.
 */
public class ArraySequenceTest extends SequenceBaseTest
{
    @Override
    @SuppressWarnings("unchecked")
    protected <T> Sequence<T> createEmptySequence()
    {
        return Sequences.wrap((T[]) new Object[0]);
    }


    @Override
    protected <T> Sequence<T> createSequence(T[] pElements)
    {
        return Sequences.wrap(pElements);
    }


    /**
     * Calling {@code Sequences.wrap(T[])} should throw a {@code NullPointerException} when passed
     * {@code null} as argument.
     */
    @Test
    public void wrapNullArrayThrows()
    {
        // Given
        Object[] aArray = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> Sequences.wrap(aArray)
        );
    }


    /**
     * A sequence wrapping an array should contain the same elements in the same order as stored in
     * the array.
     */
    @Test
    public void sequenceContainsElementsFromWrappedArray()
    {
        testWrappedArrayElements(new Object[0]);
        testWrappedArrayElements(new Object[]{new Object()});
        testWrappedArrayElements(new Object[]{new Object(), new Object(), new Object(), new Object()});
    }


    /**
     * A sequence wrapping an array should contain the same elements in the same order as stored in
     * the array.
     *
     * @param pArray    The array to test.
     */
    static private <T> void testWrappedArrayElements(T[] pArray)
    {
        // When
        Sequence<T> aSeq = Sequences.wrap(pArray);

        // Then
        assertEquals(pArray.length, aSeq.size());
        for (int i=0; i<pArray.length; i++)
            assertSame(pArray[i], aSeq.elementAt(i));
    }
}
