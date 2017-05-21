/*
 * Copyright 2006, 2008, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nullable;


/**
 * Checked exception thrown when some kind of malformed data is encountered. It can be thought of as
 * a checked variant of {@code java.lang.NumberFormatException} (or, more generally,
 * {@code java.lang.IllegalArgumentException}) that can be thrown when parsing malformed (numeric)
 * data isn't necessarily a runtime failure.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class MalformedDataException extends Exception
{
    /** Serial version UID in case anyone should want to serialize instances. */
    public static final long serialVersionUID = 1L;


    /**
     * Create a new {@code MalformedDataException}.
     *
     * @param pMessage  A message describing the malformed data.
     */
    public MalformedDataException(@Nullable String pMessage)
    {
        super(pMessage);
    }


    /**
     * Create a new {@code MalformedDataException} with an underlying cause. This constructor can be
     * used when the malformed data is detected by catching another exception, e.g. a
     * {@code NumberFormatException}.
     *
     * @param pCause    The underlying cause of the malformed data.
     */
    public MalformedDataException(@Nullable Throwable pCause)
    {
        super(pCause);
    }


    /**
     * Create a new {@code MalformedDataException} with a message and an underlying cause.
     *
     * @param pMessage  A message describing the malformed data.
     * @param pCause    The underlying cause of the malformed data.
     */
    public MalformedDataException(@Nullable String pMessage, @Nullable Throwable pCause)
    {
        super(pMessage, pCause);
    }
}
