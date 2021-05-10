/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.Iterator;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;


/**
 * Unit tests for the default methods in the {@code Sequence} interface.
 */
public class SequenceTest
{
    @Test
    public void defaultIteratorReturnsSequenceElements()
    {
        // Given
        String aElement0 = "hi";
        String aElement1 = "ho";
        TestSequence<String> aSequence = new TestSequence<>(aElement0, aElement1);

        // When
        Iterator<String> aIterator = aSequence.iterator();

        // Then
        assertSame(aElement0, aIterator.next());
        assertSame(aElement1, aIterator.next());
        assertFalse(aIterator.hasNext());
    }


    /**
     * Implementation of {@code Sequence} for unit tests. The sequence contains two elements.
     *
     * @param <E>   The type of the elements.
     */
    static private class TestSequence<E> implements Sequence<E>
    {
        private final E fElement0;
        private final E fElement1;

        TestSequence(E pElement0, E pElement1)
        {
            fElement0 = pElement0;
            fElement1 = pElement1;
        }

        @Override
        public int size()
        {
            return 2;
        }

        @Override
        public E elementAt(int pIndex)
        {
            if (pIndex == 0)
                return fElement0;
            else if (pIndex == 1)
                return fElement1;
            else
                throw new IndexOutOfBoundsException();
        }

        @Override
        public void forEach(Consumer<? super E> pAction)
        {
            pAction.accept(fElement0);
            pAction.accept(fElement1);
        }
    }
}
