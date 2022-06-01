/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.io;

import java.io.OutputStream;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.myire.util.ByteArrayConsumer;


/**
 * An output stream that passes all written bytes to a {@code ByteArrayConsumer}.
 *<p>
 * Closing or flushing a {@code PassThroughOutputStream} has no effect. Writing to a stream after
 * calling {@code close()} will <i>not</i> throw an exception.
 *
 * Instances of this class are <b>not</b> safe for use by multiple threads.
 */
@NotThreadSafe
public class PassThroughOutputStream extends OutputStream
{
    private final ByteArrayConsumer fDestination;
    private final byte[] fSingleByte = new byte[1];


    /**
     * Create a new {@code PassThroughOutputStream}.
     *
     * @param pDestination  The {@code ByteArrayConsumer} to pass all written bytes to.
     *
     * @throws NullPointerException if {@code pDestination} is null.
     */
    public PassThroughOutputStream(@Nonnull ByteArrayConsumer pDestination)
    {
        fDestination = requireNonNull(pDestination);
    }


    @Override
    public void write(int pByte)
    {
        fSingleByte[0] = (byte) pByte;
        fDestination.accept(fSingleByte);
    }


    @Override
    public void write(@Nonnull byte[] pBytes, int pOffset, int pLength)
    {
        fDestination.accept(pBytes, pOffset, pLength);
    }
}
