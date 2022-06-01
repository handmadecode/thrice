/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.io;

import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;


/**
 * An {@code OutputStream} that discards every byte written to it. Unlike the stream returned by
 * {@link OutputStream#nullOutputStream()}, this implementation does not throw an exception if
 * written to after being closed, nor does it do any bounds check of the parameters passed to
 * {@link #write(byte[], int, int)}.
 *<p>
 * Instances of this class are safe for use by multiple threads.
 */
@ThreadSafe
public class DevNullOutputStream extends OutputStream
{
    /** Global instance, should normally be used instead of creating a new instance. */
    static public final DevNullOutputStream INSTANCE = new DevNullOutputStream();


    @Override
    public void write(int pByte)
    {
        // Ignore
    }


    @Override
    public void write(@Nonnull byte[] pBytes, int pOffset, int pLength)
    {
        // Ignore
    }
}
