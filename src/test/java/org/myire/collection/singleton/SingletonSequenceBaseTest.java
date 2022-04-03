/*
 * Copyright 2013, 2015, 2017, 2020-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.singleton;

import org.myire.collection.Sequence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Base unit tests for {@code Sequence} implementations that contain a single element.
 *
 * @param <T>   The element type used in the tests.
 */
abstract public class SingletonSequenceBaseTest<T>
{
    /**
     * Create a singleton sequence.
     *
     * @return  A new {@code Sequence} with a single element.
     */
    abstract protected Sequence<T> createSingletonSequence();


    /**
     * A singleton sequence should have size 1.
     */
    @Test
    public void singletonSequenceHasSize1()
    {
        assertEquals(1, createSingletonSequence().size());
    }


    /**
     * A singleton sequence should throw an {@code IndexOutOfBoundsException} when
     * {@code elementAt()} is called with a negative index.
     */
    @Test
    public void singletonSequenceThrowsForElementAtNegativeIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> createSingletonSequence().elementAt(-1)
        );
    }


    /**
     * A singleton sequence should throw an {@code IndexOutOfBoundsException} when
     * {@code elementAt()} is called with a positive index.
     */
    @Test
    public void singletonSequenceThrowsForElementAtPositiveIndex()
    {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> createSingletonSequence().elementAt(1)
        );
    }
}
