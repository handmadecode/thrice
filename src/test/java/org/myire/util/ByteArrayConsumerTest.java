/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit tests for {@code ByteArrayConsumer}.
 */
public class ByteArrayConsumerTest
{
    @Test
    public void defaultAcceptCallsAcceptAtIndex()
    {
        // Given
        String aString = "Den lilla trädgården på Mosebacke hade ännu icke blivit öppnad för allmänheten";
        Charset aCharset = StandardCharsets.UTF_8;
        StringBuilder aBuilder = new StringBuilder();
        ByteArrayConsumer aConsumer = (b, o, l) -> aBuilder.append(new String(b, o, l, aCharset));

        // When
        aConsumer.accept(aString.getBytes(aCharset));

        // Then
        assertEquals(aString, aBuilder.toString());
    }
}
