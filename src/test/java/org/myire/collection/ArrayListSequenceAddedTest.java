/*
 * Copyright 2022-2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for {@code ArrayListSequence} instances created by adding elements.
 */
public class ArrayListSequenceAddedTest extends ReferenceSequenceBaseTest
{
    @Override
    protected Sequence<String> createSequence(String[] pElements)
    {
        ArrayListSequence<String> aSequence = new ArrayListSequence<>(pElements.length);
        for (String aElement : pElements)
            aSequence.add(aElement);

        return aSequence;
    }


    /**
     * Calling {@code new ArrayListSequence(int)} should throw an {@code IllegalArgumentException}
     * when passed a negative argument.
     */
    @Test
    public void constructorThrowsForNegativeInitialCapacity()
    {
        // When
        assertThrows(
            IllegalArgumentException.class,
            () -> new ArrayListSequence<>(-1)
        );
    }
}
