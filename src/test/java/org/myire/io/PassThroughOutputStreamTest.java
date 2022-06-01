/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.myire.util.ByteArrayBuilder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;


/**
 * Unit tests for {@code PassThroughOutputStream}.
 */
public class PassThroughOutputStreamTest
{
    @Test
    public void constructorThrowsForNullDestination()
    {
        assertThrows(
            NullPointerException.class,
            () -> new PassThroughOutputStream(null)
        );
    }


    @Test
    public void writeBytePassesByteToDestination()
    {
        // Given
        byte[] aBytes = new byte[1024];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);
        PassThroughOutputStream aStream = new PassThroughOutputStream(aBuilder::append);

        // When
        for (byte b : aBytes)
            aStream.write(b);

        // Then
        assertArrayEquals(aBytes, aBuilder.getBytes());
    }


    @Test
    public void writeByteDoesNotThrowAfterClose() throws IOException
    {
        // Given
        byte[] aBytes = new byte[512];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);
        PassThroughOutputStream aStream = new PassThroughOutputStream(aBuilder::append);

        // Given (part of the array is written)
        int aHalf = aBytes.length / 2;
        for (int i=0; i<aHalf; i++)
            aStream.write(aBytes[i]);

        // When (close the stream and write the remaining part of the array)
        aStream.close();
        for (int i=aHalf; i<aBytes.length; i++)
            aStream.write(aBytes[i]);

        // Then
        assertArrayEquals(aBytes, aBuilder.getBytes());
    }


    @Test
    public void writeByteArrayPassesArrayToDestination() throws IOException
    {
        // Given
        byte[] aBytes = new byte[1024];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);
        PassThroughOutputStream aStream = new PassThroughOutputStream(aBuilder::append);

        // When
        aStream.write(aBytes);

        // Then
        assertArrayEquals(aBytes, aBuilder.getBytes());
    }


    @Test
    public void writeByteArrayPassesArrayRangeToDestination()
    {
        // Given
        byte[] aBytes = new byte[8192];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);
        PassThroughOutputStream aStream = new PassThroughOutputStream(aBuilder::append);

        // Given
        int aRangeStart = 10, aRangeEnd = aBytes.length - 12;

        // When
        aStream.write(aBytes, aRangeStart, aRangeEnd - aRangeStart);

        // Then
        assertArrayEquals(Arrays.copyOfRange(aBytes, aRangeStart, aRangeEnd), aBuilder.getBytes());
    }


    @Test
    public void writeByteArrayRangeDoesNotThrowAfterClose() throws IOException
    {
        // Given
        byte[] aBytes = new byte[666];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);
        PassThroughOutputStream aStream = new PassThroughOutputStream(aBuilder::append);

        // Given
        int aRangeStart = 17, aRangeEnd = aBytes.length - aBytes.length / 4;

        // Given (part of the array is written)
        int aHalf = (aRangeEnd - aRangeStart) / 2;
        aStream.write(aBytes, aRangeStart, aHalf - aRangeStart);

        // When (close the stream and write the remaining part of the array)
        aStream.close();
        aStream.write(aBytes, aHalf, aRangeEnd - aHalf);

        // Then
        assertArrayEquals(Arrays.copyOfRange(aBytes, aRangeStart, aRangeEnd), aBuilder.getBytes());
    }
}
