/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.io;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;


/**
 * Unit tests for {@code DevNullOutputStream}.
 */
public class DevNullOutputStreamTest
{
    @Test
    public void writeDoesNotThrowAfterClose() throws IOException
    {
        // Given
        OutputStream aNullStream = new DevNullOutputStream();
        aNullStream.close();

        // When (nothing should happen)
        DevNullOutputStream.INSTANCE.write(1);
    }


    @Test
    public void writeArrayDoesNotThrowAfterClose() throws IOException
    {
        // Given
        OutputStream aNullStream = new DevNullOutputStream();
        aNullStream.close();

        // When (nothing should happen)
        DevNullOutputStream.INSTANCE.write(new byte[10], 0, 10);
    }
}
