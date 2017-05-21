/*
 * Copyright 2007-2010 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;


/**
 * Extension of {@code DefaultThreadFactory} that creates threads according to the same semantics as
 * the superclass with the exception that all created threads are daemon threads.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@Immutable
public class DaemonThreadFactory extends DefaultThreadFactory
{
    /**
     * Create a new {@code DaemonThreadFactory}.
     *
     * @param pBaseName             The base name for all created threads.
     * @param pAppendThreadNumber   If true, the thread number will be appended to the base thread
     *                              name prefix. If false, the thread names will consist only of the
     *                              base name. The thread number is a sequential number assigned to
     *                              each created thread, starting with 1.
     *
     * @throws NullPointerException if {@code pBaseName} is null.
     */
    public DaemonThreadFactory(@Nonnull String pBaseName, boolean pAppendThreadNumber)
    {
        super(pBaseName, pAppendThreadNumber);
    }


    /**
     * Create a new daemon thread.
     *
     * @param pTarget   The runnable be executed by the thread.
     *
     * @return  A new daemon thread, never null.
     */
    @Override
    @Nonnull
    public Thread newThread(@Nullable Runnable pTarget)
    {
        Thread aThread = super.newThread(pTarget);
        if (!aThread.isDaemon())
            aThread.setDaemon(true);

        return aThread;
    }
}
