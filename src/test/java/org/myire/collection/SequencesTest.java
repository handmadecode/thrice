/*
 * Copyright 2013, 2015, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * JUnit tests for the {@link Sequences} class.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class SequencesTest
{
    /**
     * The sequence returned by {@code Sequences.emptySequence()} should have size 0.
     */
    @Test
    public void emptySequenceHasSize0()
    {
        assertEquals(0, Sequences.emptySequence().size());
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt(0)} is called.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void emptySequenceThrowsForElementAtIndex0()
    {
        Sequences.emptySequence().elementAt(0);
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a positive index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void emptySequenceThrowsForElementAtPositiveIndex()
    {
        Sequences.emptySequence().elementAt(1);
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a negative index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void emptySequenceThrowsForElementAtNegativeIndex()
    {
        Sequences.emptySequence().elementAt(-1);
    }


    /**
     * The sequence returned by {@code Sequences.emptySequence()} should not invoke the action when
     * {@code forEach()} is called.
     */
    @Test
    public void emptySequenceDoesNotInvokeForEachAction()
    {
        // Given
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        Sequences.emptySequence().forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should have size 1.
     */
    @Test
    public void singletonSequenceHasSize1()
    {
        assertEquals(1, Sequences.singleton(null).size());
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should contain the element specified in
     * the factory method.
     */
    @Test
    public void singletonSequenceContainsExpectedElement()
    {
        // Given
        Object aElement = new Object();

        // Then
        assertSame(aElement, Sequences.singleton(aElement).elementAt(0));
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should allow its element to be
     * {@code null}.
     */
    @Test
    public void singletonSequenceAllowsNullElement()
    {
        assertNull(Sequences.singleton(null).elementAt(0));
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should invoke the action on its element
     * when {@code forEach()} is called.
     */
    @Test
    public void singletonSequenceInvokesForEachActionOnElement()
    {
        // Given
        Object aElement = new Object();
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        Sequences.singleton(aElement).forEach(aConsumer);

        // Then
        verify(aConsumer).accept(aElement);
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a positive index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void singletonSequenceThrowsForElementAtPositiveIndex()
    {
        Sequences.singleton("").elementAt(1);
    }


    /**
     * A sequence returned by {@code Sequences.singleton()} should throw an
     * {@code IndexOutOfBoundsException} when {@code elementAt()} is called with a negative index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void singletonSequenceThrowsForElementAtNegativeIndex()
    {
        Sequences.singleton("").elementAt(-1);
    }


    /**
     * Calling {@code Sequences.wrap(List)} should throw a {@code NullPointerException} when passed
     * {@code null} as argument.
     */
    @Test(expected=NullPointerException.class)
    public void wrapNullListThrows()
    {
        // Given
        List<?> aList = null;

        // When
        Sequences.wrap(aList);
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
            assertSame(pList.get(i), aSeq. elementAt(i));
    }


    /**
     * A sequence wrapping a list should invoke the action on each element in the list when
     * {@code forEach()} is called.
     */
    @Test
    public void wrappedListSequenceInvokesForEachActionOnAllElements()
    {
        // Given
        List<Object> aList = Arrays.asList(new Object(), new Object(), new Object());
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        Sequences.wrap(aList).forEach(aConsumer);

        // Then
        for (Object aElement : aList)
            verify(aConsumer).accept(aElement);

        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence wrapping an empty list should not invoke the action when {@code forEach()} is
     * called.
     */
    @Test
    public void wrappedEmptyListSequenceDoesNotInvokeForEachAction()
    {
        // Given
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        Sequences.wrap(new ArrayList<>()).forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence wrapping a list should throw an {@code IndexOutOfBoundsException} when
     * {@code elementAt()} is called with a negative index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void wrappedListSequenceThrowsForElementAtNegativeIndex()
    {
        // Given
        List<Object> aList = Arrays.asList(new Object(), new Object(), new Object());

        // When
        Sequences.wrap(aList).elementAt(-2);
    }


    /**
     * A sequence wrapping a list should throw an {@code IndexOutOfBoundsException} when
     * {@code elementAt()} is called with a too large index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void wrappedListSequenceThrowsForElementAtTooLargeIndex()
    {
        // Given
        List<Object> aList = Arrays.asList(new Object(), new Object());

        // When
        Sequences.wrap(aList).elementAt(aList.size());
    }


    /**
     * Calling {@code Sequences.wrap(T[])} should throw a {@code NullPointerException} when passed
     * {@code null} as argument.
     */
    @Test(expected=NullPointerException.class)
    public void wrapNullArrayThrows()
    {
        // Given
        Object[] aArray = null;

        // When
        Sequences.wrap(aArray);
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
            assertSame(pArray[i], aSeq. elementAt(i));
    }


    /**
     * A sequence wrapping an array should invoke the action on each element in the array when
     * {@code forEach()} is called.
     */
    @Test
    public void wrappedArraySequenceInvokesForEachActionOnAllElements()
    {
        // Given
        Object[] aArray = {new Object(), new Object(), new Object(), new Object()};
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        Sequences.wrap(aArray).forEach(aConsumer);

        // Then
        for (Object aElement : aArray)
            verify(aConsumer).accept(aElement);

        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence wrapping an empty array should not invoke the action when {@code forEach()} is
     * called.
     */
    @Test
    public void wrappedEmptyArraySequenceDoesNotInvokeForEachAction()
    {
        // Given
        Consumer<Object> aConsumer = newMockConsumer();

        // When
        Sequences.wrap(new Object[0]).forEach(aConsumer);

        // Then
        verifyNoMoreInteractions(aConsumer);
    }


    /**
     * A sequence wrapping an array should throw an {@code IndexOutOfBoundsException} when
     * {@code elementAt()} is called with a negative index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void wrappedArraySequenceThrowsForElementAtNegativeIndex()
    {
        // Given
        Object[] aArray = {new Object(),new Object()};

        // When
        Sequences.wrap(aArray).elementAt(-5);
    }


    /**
     * A sequence wrapping an array should throw an {@code IndexOutOfBoundsException} when
     * {@code elementAt()} is called with a too large index.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void wrappedArraySequenceThrowsForTooLargeIndex()
    {
        // Given
        Object[] aArray = {new Object(), new Object(), new Object()};

        // When
        Sequences.wrap(aArray).elementAt(aArray.length);
    }


    /**
     * Mock a {@code Consumer<T>}, suppressing the unchecked cast.
     *
     * @param <T>   The type to consume.
     *
     * @return  A mocked {@code Consumer<T>}.
     */
    @SuppressWarnings("unchecked")
    static private <T> Consumer<T> newMockConsumer()
    {
        return (Consumer<T>) mock(Consumer.class);
    }
}
