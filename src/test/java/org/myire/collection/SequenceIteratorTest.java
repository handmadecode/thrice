/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;

import static org.myire.collection.Iterators.sequenceIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link Iterators#sequenceIterator(Sequence)}.
 */
public class SequenceIteratorTest extends ReferenceIteratorBaseTest
{
    @Override
    protected Iterator<Object> createIterator(Object[] pElements)
    {
        return sequenceIterator(Sequences.wrap(pElements));
    }


    /**
     * The factory method {@code Iterators.sequenceIterator} should throw a
     * {@code NullPointerException} when passed a null array argument.
     */
    @Test
    public void factoryMethodThrowsForNullArrayArgument()
    {
        assertThrows(
            NullPointerException.class,
            () -> sequenceIterator(null)
        );
    }
}
