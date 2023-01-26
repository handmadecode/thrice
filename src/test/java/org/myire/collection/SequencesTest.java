/*
 * Copyright 2021, 2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.myire.collection.Sequences.areEqual;


/**
 * Unit tests for the static methods in {@code Sequences} not covered by the Sequence implementation
 * test classes.
 */
public class SequencesTest
{
    /**
     * areEqual() should return true when passed the same {@code Sequence} instance as both
     * arguments.
     */
    @Test
    public void sequenceIsEqualToItself()
    {
        // Given
        Sequence<String> aSequence = Sequences.singleton("x");

        // Then
        assertTrue(areEqual(aSequence, aSequence, String::equals));
    }


    /**
     * areEqual() should return true when both {@code Sequence} arguments are null.
     */
    @Test
    public void nullSequenceIsEqualToNull()
    {
        // Given
        Sequence<String> aSequence = null;

        // Then
        assertTrue(areEqual(aSequence, null, String::equals));
    }


    /**
     * areEqual() should return false when the first argument is a non-null {@code Sequence} and
     * the second is null.
     */
    @Test
    public void sequenceIsNotEqualToNull()
    {
        // Given
        Sequence<String> aSequence = Sequences.emptySequence();

        // Then
        assertFalse(areEqual(aSequence, null, String::equals));
    }


    /**
     * areEqual() should return false when the first argument is null and  the second is a non-null
     * {@code Sequence}.
     */
    @Test
    public void nullIsNotEqualToSequence()
    {
        // Given
        Sequence<String> aSequence = Sequences.wrap(new String[]{"x", "y"});

        // Then
        assertFalse(areEqual(null, aSequence, String::equals));
    }


    /**
     * areEqual() should return true when passed two empty {@code Sequence} instances.
     */
    @Test
    public void emptySequenceAresEqual()
    {
        // Given
        Sequence<String> aSequence1 = Sequences.wrap(new String[]{});
        Sequence<String> aSequence2 = Sequences.emptySequence();

        // Then
        assertTrue(areEqual(aSequence1, aSequence2, String::equals));
    }


    /**
     * areEqual() should return true when passed two {@code Sequence} instances with the same
     * elements.
     */
    @Test
    public void sequencesWithSameElementsAreEqual()
    {
        // Given
        List<String> aElements = Arrays.asList("x", "y", "z");
        Sequence<String> aSequence1 = Sequences.wrap(aElements);
        Sequence<String> aSequence2 = Sequences.wrap(aElements.toArray(new String[0]));

        // Then
        assertTrue(areEqual(aSequence1, aSequence2, String::equals));
    }


    /**
     * areEqual() should return false when passed two {@code Sequence} instances with the same
     * elements but in different orders.
     */
    @Test
    public void sequencesWithSameElementsInDifferentOrdersAreNotEqual()
    {
        // Given
        List<String> aElements = Arrays.asList("x", "y", "z");
        Sequence<String> aSequence1 = Sequences.wrap(aElements.toArray(new String[0]));
        Collections.swap(aElements, 0, 1);
        Sequence<String> aSequence2 = Sequences.wrap(aElements);

        // Then
        assertFalse(areEqual(aSequence1, aSequence2, String::equals));
    }


    /**
     * areEqual() should return false when passed two {@code Sequence} instances of different
     * lengths.
     */
    @Test
    public void sequencesWithDifferentLengthsAreNotEqual()
    {
        String[] aElements = {"x", "y", "z", "w"};
        Sequence<String> aSequence1 = Sequences.wrap(aElements);
        Sequence<String> aSequence2 = Sequences.wrap(aElements, 0, aElements.length - 1);

        // Then
        assertFalse(areEqual(aSequence1, aSequence2, String::equals));
    }


    /**
     * areEqual() should return false when passed two {@code Sequence} instances with different
     * elements.
     */
    @Test
    public void sequencesWithDifferentElementsAreNotEqual()
    {
        String[] aElements1 = {"x", "y", "z", "w"};
        Sequence<String> aSequence1 = Sequences.wrap(aElements1);
        String[] aElements2 = Arrays.copyOf(aElements1, aElements1.length);
        aElements2[aElements2.length-1] = "q";
        Sequence<String> aSequence2 = Sequences.wrap(aElements2);

        // Then
        assertFalse(areEqual(aSequence1, aSequence2, String::equals));
    }
}
