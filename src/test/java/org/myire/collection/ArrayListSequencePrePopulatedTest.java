/*
 * Copyright 2022-2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for {@code ArrayListSequence} instances created from an initial collection of
 * elements.
 */
public class ArrayListSequencePrePopulatedTest extends ReferenceSequenceBaseTest
{
    @Override
    protected Sequence<String> createSequence(String[] pElements)
    {
        return new ArrayListSequence<>(Arrays.asList(pElements));
    }


    /**
     * Calling {@code new ArrayListSequence(Collection)} should throw a {@code NullPointerException}
     * when passed {@code null} as argument.
     */
    @Test
    public void constructorThrowsForNullCollection()
    {
        // When
        assertThrows(
            NullPointerException.class,
            () -> new ArrayListSequence<>(null)
        );
    }
}
