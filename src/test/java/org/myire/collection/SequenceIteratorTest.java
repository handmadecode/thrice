/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.myire.collection.Iterators.sequenceIterator;


/**
 * Unit tests for the {@code Iterator} implementation returned by
 * {@link Iterators#sequenceIterator(Sequence)}.
 */
public class SequenceIteratorTest extends IteratorBaseTest
{
    @Override
    protected <T> Iterator<T> createIterator(T[] pElements)
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
