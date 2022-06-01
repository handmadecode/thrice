/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit tests for {@code ByteArrayFunction}.
 */
public class ByteArrayFunctionTest
{
    @Test
    public void defaultApplyCallsApplyAtIndex()
    {
        // Given
        String aString = "The final words were written in the sands";
        ByteArrayFunction<String> aFunction = String::new;

        // When
        String aResult = aFunction.apply(aString.getBytes());

        // Then
        assertEquals(aString, aResult);
    }
}
