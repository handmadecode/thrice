/*
 * Copyright 2007-2010 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;


/**
 * Basic thread factory that creates all threads in the same {@code ThreadGroup}. If
 * {@code System.getSecurityManager()} returns a non-null value, the threads are created in the
 * thread group of that {@code SecurityManager}, otherwise they are created in the group of the
 * thread that creates the instance of this factory.
 *<p>
 * The threads will have the priority and daemon status of the thread that calls
 * {@link #newThread(Runnable)}, just as is the case when calling {@code new Thread()}.
 *<p>
 * The created threads are given a fixed name with an optional thread number suffix.
 *<p>
 * This class is based on {@code java.util.concurrent.Executors$DefaultThreadFactory}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@Immutable
public class DefaultThreadFactory implements ThreadFactory
{
    private final ThreadGroup fThreadGroup;
    private final String fBaseName;
    private final AtomicInteger fNextThreadNumber = new AtomicInteger(1);
    private final boolean fAppendThreadNumber;


    /**
     * Create a new {@code DefaultThreadFactory}.
     *
     * @param pBaseName             The base name for all created threads.
     * @param pAppendThreadNumber   If true, the thread number will be appended to the base thread
     *                              name prefix. If false, the thread names will consist only of the
     *                              base name. The thread number is a sequential number assigned to
     *                              each created thread, starting with 1.
     *
     * @throws NullPointerException if {@code pBaseName} is null.
     */
    public DefaultThreadFactory(@Nonnull String pBaseName, boolean pAppendThreadNumber)
    {
        fBaseName = requireNonNull(pBaseName);
        fAppendThreadNumber = pAppendThreadNumber;
        SecurityManager aSecurityManager = System.getSecurityManager();
        if (aSecurityManager != null)
            fThreadGroup = aSecurityManager.getThreadGroup();
        else
            fThreadGroup = Thread.currentThread().getThreadGroup();
    }


    /**
     * Create a new thread.
     *
     * @param pTarget   The runnable be executed by the thread.
     *
     * @return  A new thread, never null.
     */
    @Override
    @Nonnull
    public Thread newThread(@Nullable Runnable pTarget)
    {
        String aThreadName = fBaseName;
        int aThreadNumber = fNextThreadNumber.getAndIncrement();
        if (fAppendThreadNumber)
            aThreadName = fBaseName + aThreadNumber;

        return new Thread(fThreadGroup, pTarget, aThreadName);
    }


    /**
     * Get the number of threads created by this thread factory.
     *
     * @return  The number of created threads.
     */
    public int getNumCreatedThreads()
    {
        return fNextThreadNumber.get() - 1;
    }
}
