/*
 * Copyright 2013, 2015, 2017, 2020-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for the {@code Sequence} implementation returned by {@link Sequences#wrap(List)}.
 */
public class ListSequenceTest extends ReferenceSequenceBaseTest
{
    @Override
    protected Sequence<Object> createSequence(Object[] pElements)
    {
        return Sequences.wrap(Arrays.asList(pElements));
    }


    /**
     * Calling {@code Sequences.wrap(List)} should throw a {@code NullPointerException} when passed
     * {@code null} as argument.
     */
    @Test
    public void wrapNullListThrows()
    {
        // Given
        List<?> aList = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> Sequences.wrap(aList)
        );
    }


    /**
     * A sequence wrapping a list should contain the same elements in the same order as stored in
     * the list.
     */
    @Test
    public void sequenceContainsElementsFromWrappedList()
    {
        testWrappedListElements(Collections.emptyList());
        testWrappedListElements(new ArrayList<>());
        testWrappedListElements(Collections.singletonList(new Object()));
        testWrappedListElements(Arrays.asList(new Object(), new Object(), new Object(), new Object()));
        testWrappedListElements(Arrays.asList(randomElementArray(randomCollectionLength())));
    }


    /**
     * A sequence wrapping a list should contain the same elements in the same order as stored in
     * the list.
     *
     * @param pList The list to test.
     */
    static private <T> void testWrappedListElements(List<T> pList)
    {
        // When
        Sequence<T> aSeq = Sequences.wrap(pList);

        // Then
        assertEquals(pList.size(), aSeq.size());
        for (int i=0; i<pList.size(); i++)
            assertSame(pList.get(i), aSeq.elementAt(i));
    }
}
