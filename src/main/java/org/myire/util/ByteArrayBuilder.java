/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.Arrays;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;


/**
 * A sequence of bytes that can be appended to.
 * *<p>
 * Instances of this class are <b>not</b> safe for use by multiple threads without external
 * synchronization.
 */
@NotThreadSafe
public class ByteArrayBuilder
{
    // The maximum length of an array. Some JVMs store state in the last few bytes.
    static private final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    private byte[] fBytes;
    private int fLength;


    /**
     * Create a new {@code ByteArrayBuilder}.
     *
     * @param pInitialCapacity  The initial capacity of the internal byte array.
     *
     * @throws NegativeArraySizeException   if {@code pInitialCapacity} is negative.
     */
    public ByteArrayBuilder(@Nonnegative int pInitialCapacity)
    {
        fBytes = new byte[pInitialCapacity];
    }


    /**
     * Get the number of bytes appended to this instance.
     *
     * @return  The number of bytes appended to this instance.
     */
    @Nonnegative
    public int getLength()
    {
        return fLength;
    }


    /**
     * Discard all bytes appended to this instance. The next call to one of the {@code append}
     * methods will append bytes at the beginning of the internal buffer.
     *
     * @return  This instance.
     */
    @Nonnull
    public ByteArrayBuilder reset()
    {
        fLength = 0;
        return this;
    }


    /**
     * Discard all bytes appended to this instance. The next call to one of the {@code append}
     * methods will append bytes at the beginning of the internal buffer.
     *<p>
     * This method allows a maximum capacity to be specified. If the internal buffer is larger than
     * this value, that buffer will be replaced with a buffer of the specified capacity, which can
     * avoid having long-lived instances hang on to large buffers that no longer are needed.
     *
     * @param pMaxCapacity  The maximum capacity the internal buffer should have after the reset.
     *
     * @return  This instance.
     *
     * @throws NegativeArraySizeException   if {@code pMaxCapacity} is negative.
     */
    @Nonnull
    public ByteArrayBuilder reset(@Nonnegative int pMaxCapacity)
    {
        fLength = 0;
        if (fBytes.length > pMaxCapacity)
            fBytes = new byte[pMaxCapacity];

        return this;
    }


    /**
     * Append a byte to this instance.
     *
     * @param pByte The byte to append.
     *
     * @return  This instance.
     *
     * @throws OutOfMemoryError if the internal buffer is full and memory cannot be allocated.
     */
    @Nonnull
    public ByteArrayBuilder append(byte pByte)
    {
        if (fLength == fBytes.length)
            expandBuffer(fBytes.length + 1);

        fBytes[fLength++] = pByte;
        return this;
    }


    /**
     * Append the bytes in an array to this instance.
     *
     * @param pBytes    The bytes to append.
     *
     * @return  This instance.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     * @throws OutOfMemoryError if the internal buffer is full and memory cannot be allocated for
     *                          the number of bytes in the array.
     */
    @Nonnull
    public ByteArrayBuilder append(@Nonnull byte[] pBytes)
    {
        appendBytes(pBytes, 0, pBytes.length);
        return this;
    }


    /**
     * Append the bytes in a subrange of an array to this instance.
     *
     * @param pBytes    The byte array to append from.
     * @param pOffset   The offset in the array of the first byte to append.
     * @param pLength   The number of bytes to append.
     *
     * @return  This instance.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is less than 0 or if {@code pLength} is
     *                                   less than 0 or if {@code pOffset+pLength} is greater than
     *                                   {@code pBytes.length}.
     * @throws OutOfMemoryError if the internal buffer is full and memory cannot be allocated for
     *                          the specified number of bytes.
     */
    @Nonnull
    public ByteArrayBuilder append(@Nonnull byte[] pBytes, int pOffset, int pLength)
    {
        appendBytes(pBytes, pOffset, pLength);
        return this;
    }


    /**
     * Get the bytes appended to this instance.
     *
     * @return  A new byte array with the appended bytes copied, possibly empty, never null.
     */
    @Nonnull
    public byte[] getBytes()
    {
        byte[] aBytes = new byte[fLength];
        System.arraycopy(fBytes, 0, aBytes, 0, fLength);
        return aBytes;
    }


    /**
     * Pass the bytes appended to this instance to a destination.
     *
     * @param pDestination  The destination for the bytes.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     */
    public void withBytes(@Nonnull ByteArrayConsumer pDestination)
    {
        pDestination.accept(fBytes, 0, fLength);
    }


    /**
     * Apply a function to the bytes appended to this instance.
     *
     * @param pFunction The function to apply to the bytes.
     *
     * @param <R> The function's result type.
     *
     * @return  The result of applying the function to the bytes.
     *
     * @throws NullPointerException if {@code pFunction} is null.
     */
    public <R> R applyToBytes(@Nonnull ByteArrayFunction<R> pFunction)
    {
        return pFunction.apply(fBytes, 0, fLength);
    }


    /**
     * Append bytes to the internal buffer.
     *
     * @param pBytes    The byte array to copy from.
     * @param pOffset   The offset to copy from.
     * @param pLength   The number of bytes to copy.
     *
     * @throws NullPointerException if {@code pBytes} is null.
     * @throws ArrayIndexOutOfBoundsException   if {@code pOffset} and {@code pLength} specify an
     *                                          invalid range in {@code pBytes}.
     * @throws OutOfMemoryError if the internal buffer is full and memory cannot be allocated for
     *                          the specified number of bytes.
     */
    private void appendBytes(@Nonnull byte[] pBytes, @Nonnegative int pOffset, @Nonnegative int pLength)
    {
        int aNewLength = fLength + pLength;
        if (aNewLength >= fBytes.length)
            expandBuffer(aNewLength);

        System.arraycopy(pBytes, pOffset, fBytes, fLength, pLength);
        fLength = aNewLength;
    }


    /**
     * Expand the internal buffer.
     *
     * @param pRequiredCapacity The minimum new capacity.
     *
     * @throws OutOfMemoryError if {@code pRequiredCapacity} is less than 0 or greater than the
     *                          maximum array size, or if allocating a new array failed.
     */
    private void expandBuffer(int pRequiredCapacity)
    {
        if (pRequiredCapacity < 0 || pRequiredCapacity > MAX_ARRAY_SIZE)
            throw new OutOfMemoryError("Array length " + pRequiredCapacity);

        // Increase the new capacity with 50% until the required capacity is satisfied.
        long aNewCapacity = fBytes.length;
        while (aNewCapacity < pRequiredCapacity)
            aNewCapacity = aNewCapacity + (aNewCapacity >> 1);

        if (aNewCapacity > MAX_ARRAY_SIZE)
            aNewCapacity = MAX_ARRAY_SIZE;

        fBytes = Arrays.copyOf(fBytes, (int) aNewCapacity);
    }
}
