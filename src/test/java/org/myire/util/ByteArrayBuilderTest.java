/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for {@code ByteArrayBuilder}.
 */
public class ByteArrayBuilderTest
{
    @Test
    public void constructorThrowsForNegativeInitialCapacity()
    {
        assertThrows(
            NegativeArraySizeException.class,
            () -> new ByteArrayBuilder(-1)
        );
    }


    @Test
    public void getLengthReturnsZeroForNewInstance()
    {
        assertEquals(0, new ByteArrayBuilder(8).getLength());
    }


    @Test
    public void getLengthReturnsTheNumberOfAppendedBytes()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        aBuilder.append((byte) 17);

        // Then
        assertEquals(1, aBuilder.getLength());

        // When
        aBuilder.append((byte) 18);

        // Then
        assertEquals(2, aBuilder.getLength());
    }


    @Test
    public void resetReturnsThis()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        ByteArrayBuilder aReturned = aBuilder.reset();

        // Then
        assertSame(aBuilder, aReturned);
    }


    @Test
    public void resetSetsLengthToZero()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        aBuilder.append((byte) 2);

        // Then
        assertEquals(1, aBuilder.getLength());

        // When
        aBuilder.reset();

        // Then
        assertEquals(0, aBuilder.getLength());
    }


    @Test
    public void resetDiscardsAppendedBytes()
    {
        // Given
        byte[] aBytes = {(byte) 1, (byte) 99};
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(16);

        // When
        aBuilder.append(aBytes[0]);
        aBuilder.append(aBytes[1]);

        // Then
        assertArrayEquals(aBytes, aBuilder.getBytes());

        // When
        aBuilder.reset();

        // Then
        assertEquals(0, aBuilder.getBytes().length);
    }


    @Test
    public void resetToCapacityReturnsThis()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        ByteArrayBuilder aReturned = aBuilder.reset(17);

        // Then
        assertSame(aBuilder, aReturned);
    }


    @Test
    public void resetToCapacityThrowsForNegativeArgument()
    {
        assertThrows(
            NegativeArraySizeException.class,
            () -> new ByteArrayBuilder(8).reset(-2)
        );
    }


    @Test
    public void resetToCapacitySetsLengthToZero()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        aBuilder.append((byte) 56);

        // Then
        assertEquals(1, aBuilder.getLength());

        // When
        aBuilder.reset(4);

        // Then
        assertEquals(0, aBuilder.getLength());
    }


    @Test
    public void resetToCapacityDiscardsAppendedBytes()
    {
        // Given
        byte[] aBytes = {(byte) 47, (byte) -22, (byte) 88};
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(16);

        // When
        aBuilder.append(aBytes[0]);
        aBuilder.append(aBytes[1]);
        aBuilder.append(aBytes[2]);

        // Then
        assertArrayEquals(aBytes, aBuilder.getBytes());

        // When
        aBuilder.reset(22);

        // Then
        assertEquals(0, aBuilder.getBytes().length);
    }


    @Test
    public void appendByteReturnsThis()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        ByteArrayBuilder aReturned = aBuilder.append((byte) 98);

        // Then
        assertSame(aBuilder, aReturned);
    }


    @Test
    public void appendByteAppendsTheExpectedBytes()
    {
        // Given
        byte[] aBytes = new byte[4711];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);

        // When
        for (byte aByte : aBytes)
            aBuilder.append(aByte);

        // Then
        assertEquals(aBytes.length, aBuilder.getLength());
        assertArrayEquals(aBytes, aBuilder.getBytes());
    }


    @Test
    public void appendByteAppendsTheExpectedBytesWhenInitialCapacityIsTooSmall()
    {
        // Given
        byte[] aBytes = new byte[197];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length / 2);

        // When
        for (byte aByte : aBytes)
            aBuilder.append(aByte);

        // Then
        assertEquals(aBytes.length, aBuilder.getLength());
        assertArrayEquals(aBytes, aBuilder.getBytes());
    }


    @Test
    public void appendByteArrayReturnsThis()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        ByteArrayBuilder aReturned = aBuilder.append(new byte[8189]);

        // Then
        assertSame(aBuilder, aReturned);
    }


    @Test
    public void appendByteArrayAppendsTheExpectedBytes()
    {
        // Given
        byte[] aBytes = new byte[16385];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);

        // When
        aBuilder.append(aBytes);

        // Then
        assertEquals(aBytes.length, aBuilder.getLength());
        assertArrayEquals(aBytes, aBuilder.getBytes());
    }


    @Test
    public void appendByteArrayAppendsTheExpectedBytesWhenInitialCapacityIsTooSmall()
    {
        // Given
        byte[] aBytes = new byte[16386];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length / 2);

        // When
        aBuilder.append(aBytes);

        // Then
        assertEquals(aBytes.length, aBuilder.getLength());
        assertArrayEquals(aBytes, aBuilder.getBytes());
    }


    @Test
    public void appendByteArrayCopiesBytes()
    {
        // Given
        byte[] aBytes = new byte[381];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length + 2);

        // Given
        int aPos1 = 1;
        int aPos2 = aBytes.length - 2;
        byte aOriginalValue1 = aBytes[aPos1];
        byte aOriginalValue2 = aBytes[aPos2];

        // When (modify some bytes after they have been appended)
        aBuilder.append(aBytes);
        aBytes[aPos1] += 2;
        aBytes[aPos2] -= 3;

        // Then
        byte[] aAppendedBytes = aBuilder.getBytes();
        assertEquals(aOriginalValue1, aAppendedBytes[aPos1]);
        assertEquals(aOriginalValue2, aAppendedBytes[aPos2]);
    }


    @Test
    public void appendByteArrayRangeReturnsThis()
    {
        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(8);

        // When
        ByteArrayBuilder aReturned = aBuilder.append(new byte[46], 18, 12);

        // Then
        assertSame(aBuilder, aReturned);
    }


    @Test
    public void appendByteArrayRangeAppendsTheExpectedBytes()
    {
        // Given
        byte[] aBytes = new byte[4090];
        ThreadLocalRandom.current().nextBytes(aBytes);

        // Given
        int aOffset = 17, aLength = 97;
        byte[] aExpectedBytes = new byte[aLength];
        System.arraycopy(aBytes, aOffset, aExpectedBytes, 0, aLength);

        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length);

        // When
        aBuilder.append(aBytes, aOffset, aLength);

        // Then
        assertEquals(aLength, aBuilder.getLength());
        assertArrayEquals(aExpectedBytes, aBuilder.getBytes());
    }


    @Test
    public void appendByteArrayRangeAppendsTheExpectedBytesWhenInitialCapacityIsTooSmall()
    {
        // Given
        byte[] aBytes = new byte[665];
        ThreadLocalRandom.current().nextBytes(aBytes);

        // Given
        int aOffset = 429, aLength = aBytes.length - aOffset - 2;
        byte[] aExpectedBytes = new byte[aLength];
        System.arraycopy(aBytes, aOffset, aExpectedBytes, 0, aLength);

        // Given
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aLength/2);

        // When
        aBuilder.append(aBytes, aOffset, aLength);

        // Then
        assertEquals(aLength, aBuilder.getLength());
        assertArrayEquals(aExpectedBytes, aBuilder.getBytes());
    }


    @Test
    public void appendByteArrayRangeCopiesBytes()
    {
        // Given
        byte[] aBytes = new byte[66];
        ThreadLocalRandom.current().nextBytes(aBytes);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(aBytes.length + 2);

        // Given
        int aOffset = 28;
        int aPos1 = 3;
        int aPos2 = aBytes.length - aOffset - 1;
        byte aOriginalValue1 = aBytes[aOffset + aPos1];
        byte aOriginalValue2 = aBytes[aOffset + aPos2];

        // When (modify some bytes after they have been appended)
        aBuilder.append(aBytes, aOffset, aBytes.length - aOffset);
        aBytes[aOffset + aPos1] += 5;
        aBytes[aOffset + aPos2] -= 2;

        // Then
        byte[] aAppendedBytes = aBuilder.getBytes();
        assertEquals(aOriginalValue1, aAppendedBytes[aPos1]);
        assertEquals(aOriginalValue2, aAppendedBytes[aPos2]);
    }


    @Test
    public void withBytesPassesBytesToConsumer()
    {
        // Given
        String aString = "All the smiles and all the frowns and all the ups and all the downs";
        byte[] aBytes = aString.getBytes(StandardCharsets.US_ASCII);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(64);
        aBuilder.append(aBytes);

        // When
        StringBuilder aStringBuilder = new StringBuilder();
        aBuilder.withBytes(
            (b, o, l) -> aStringBuilder.append(new String(b, o, l, StandardCharsets.US_ASCII))
        );

        // Then
        assertEquals(aString, aStringBuilder.toString());
    }


    @Test
    public void applyToBytesPassesBytesToFunction()
    {
        // Given
        String aString = "Please don't fuck up my world, so much now needs addressing";
        byte[] aBytes = aString.getBytes(StandardCharsets.US_ASCII);
        ByteArrayBuilder aBuilder = new ByteArrayBuilder(32);
        aBuilder.append(aBytes);

        // When
        String aCopy = aBuilder.applyToBytes(String::new);

        // Then
        assertEquals(aString, aCopy);
    }
}
