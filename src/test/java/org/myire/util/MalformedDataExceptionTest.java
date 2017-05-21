/*
 * Copyright 2009 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;


/**
 * Unit tests for {@link org.myire.util.MalformedDataException}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class MalformedDataExceptionTest
{
    /**
     * The constructor that takes a message only as parameter should return that message when
     * {@code getMessage} is called and return null when {@code getCause} is called.
     */
    @Test
    public void messageOnlyCtorShouldHaveCorrectMessageAndNullCause()
    {
        // Given
        String aMessage = "You can't do that";

        // When
        MalformedDataException aException = new MalformedDataException(aMessage);

        // Then
        assertEquals(aMessage, aException.getMessage());
        assertNull(aException.getCause());
    }


    /**
     * The constructor that takes a cause only as parameter should return that cause when
     * {@code getCause} is called.
     */
    @Test
    public void causeOnlyCtorShouldHaveCorrectCause()
    {
        // Given
        Throwable aCause = new InterruptedException("interruptus");

        // When
        MalformedDataException aException = new MalformedDataException(aCause);

        // Then
        assertSame(aCause, aException.getCause());
    }


    /**
     * The constructor that takes both a message and a cause as parameters should return those
     * values when {@code getMessage}  and {@code getCause} are called.
     */
    @Test
    public void testMessageAndCauseCtor()
    {
        // Given
        String aMessage = "You can't do that";
        Throwable aCause = new InterruptedException("interruptus");

        // When
        MalformedDataException aException = new MalformedDataException(aMessage, aCause);

        // Then
        assertEquals(aMessage, aException.getMessage());
        assertSame(aCause, aException.getCause());
    }
}
