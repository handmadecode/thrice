/*
 * Copyright 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;


/**
 * A synchronization aid that allows one or more threads to wait until a condition of some sort
 * holds true. The exact nature of the condition or how it becomes true is not part if this
 * interface.
 *<p>
 * Implementations must be safe for use by multiple threads.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@ThreadSafe
public interface Awaitable
{
    /**
     * Wait for the underlying condition to become true.
     *<p>
     * When calling this method, the current thread becomes disabled for thread scheduling purposes
     * and lies dormant until one of two things happen:
     *<ul>
     * <li>The condition becomes true; or</li>
     * <li>Some other thread interrupts the current thread.</li>
     *</ul>
     * If the condition already is true when this method is called it returns immediately.
     *<p>
     * If the current thread has its interrupted status set on entry to this method then an
     * {@link InterruptedException} is thrown immediately and the current thread's interrupted
     * status is cleared.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting for the
     *                              underlying condition to become true.
     */
    void await() throws InterruptedException;

    /**
     * Wait for the underlying condition to become true unless the specified waiting time elapses.
     *<p>
     * When calling this method, the current thread becomes disabled for thread scheduling purposes
     * and lies dormant until one of three things happen:
     *<ul>
     * <li>The condition becomes true; or</li>
     * <li>Some other thread interrupts the current thread; or</li>
     * <li>The specified waiting time elapses.</li>
     *</ul>
     * If the condition already is true when this method is called then {@code true} is returned
     * immediately.
     *<p>
     * If the specified waiting time elapses before the condition becomes true then {@code false} is
     * returned.
     *<p>
     * If the current thread has its interrupted status set on entry to this method then an
     * {@link InterruptedException} is thrown immediately and the current thread's interrupted
     * status is cleared.
     *
     * @param pTimeout  The maximum time to wait. If the time is less than or equal to zero, the
     *                  method will not wait at all.
     * @param pUnit     The time unit of the {@code pTimeout} parameter.
     *
     * @return  {@code true} if the condition has become true, {@code false} if the waiting time
     *          elapsed before the condition was true.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting for the
     *                              underlying condition to become true.
     * @throws NullPointerException if {@code pUnit} is null.
     */
    boolean await(long pTimeout, @Nonnull TimeUnit pUnit) throws InterruptedException;
}
