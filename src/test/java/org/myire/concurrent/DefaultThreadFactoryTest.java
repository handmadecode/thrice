/*
 * Copyright 2007-2010, 2016-2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Unit tests for {@link org.myire.concurrent.DefaultThreadFactory}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class DefaultThreadFactoryTest extends AbstractDefaultThreadFactoryTest
{
    @Override
    protected DefaultThreadFactory createTestInstance(String pBaseName, boolean pAppendThreadNumber)
    {
        return new DefaultThreadFactory(pBaseName, pAppendThreadNumber);
    }


    /**
     * The constructor should throw a {@code NullPointerException} when passed a null base name.
     */
    @SuppressWarnings("unused")
    @Test(expected=NullPointerException.class)
    public void ctorThrowsForNullBaseName()
    {
        new DefaultThreadFactory(null, false);
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
     * The daemon status of the created threads should be true when created from a daemon thread.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    @Test
    public void daemonStatusIsTrueWhenCreatedFromDaemonThread() throws InterruptedException
    {
        // Given
        ThreadCreatingThread aCreatorThread = newThreadCreatingThread();
        aCreatorThread.setDaemon(true);

        // When
        aCreatorThread.startAndJoin();

        // Then
        assertTrue(aCreatorThread.getCreatedThread().isDaemon());
    }
}
