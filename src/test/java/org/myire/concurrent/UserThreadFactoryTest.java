/*
 * Copyright 2010, 2016-2017, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for {@link org.myire.concurrent.UserThreadFactory}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class UserThreadFactoryTest extends AbstractDefaultThreadFactoryTest
{
    @Override
    protected DefaultThreadFactory createTestInstance(String pBaseName, boolean pAppendThreadNumber)
    {
        return new UserThreadFactory(pBaseName, pAppendThreadNumber);
    }


    /**
     * The constructor should throw a {@code NullPointerException} when passed a null base name.
     */
    @SuppressWarnings("unused")
    @Test
    public void ctorThrowsForNullBaseName()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                new UserThreadFactory(null, false)
        );
    }


    /**
     * The daemon status of the created threads should be false when created from a user thread.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    @Test
    public void daemonStatusIsFalseWhenCreatedFromUserThread() throws InterruptedException
    {
        // Given
        ThreadCreatingThread aCreatorThread = newThreadCreatingThread();
        aCreatorThread.setDaemon(false);

        // When
        aCreatorThread.startAndJoin();

        // Then
        assertFalse(aCreatorThread.getCreatedThread().isDaemon());
    }


    /**
     * The daemon status of the created threads should be false when created from a daemon thread.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    @Test
    public void daemonStatusIsFalseWhenCreatedFromDaemonThread() throws InterruptedException
    {
        // Given
        ThreadCreatingThread aCreatorThread = newThreadCreatingThread();
        aCreatorThread.setDaemon(true);

        // When
        aCreatorThread.startAndJoin();

        // Then
        assertFalse(aCreatorThread.getCreatedThread().isDaemon());
    }
}
