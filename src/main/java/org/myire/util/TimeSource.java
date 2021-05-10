/*
 * Copyright 2019, 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;


/**
 * A source for the current time, expressed in a specific {@code TimeUnit}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public interface TimeSource
{
    /**
     * Get the current time as 64-bit integer value.
     *
     * @return  The current time, expressed in the {@code TimeUnit} of this source.
     */
    long getCurrentTime();

    /**
     * Get the time unit of the value returned by {@link #getCurrentTime()}.
     *
     * @return  The time unit, never null.
     */
    @Nonnull TimeUnit getTimeUnit();

    /**
     * Get the current time as 64-bit integer value in the specified time unit rather than the time
     * source's native unit.
     *
     * @param pTimeUnit The time unit to get the current time in.
     *
     * @return  The current time expressed in the specified time unit.
     *
     * @throws NullPointerException if {@code pTimeUnit} is null.
     */
    default long getCurrentTimeInUnit(@Nonnull TimeUnit pTimeUnit)
    {
        return pTimeUnit.convert(getCurrentTime(), getTimeUnit());
    }


    /**
     * {@code TimeSource} implementation that returns {@code System.currentTimeMillis()}.
     */
    class CurrentMillisTimeSource implements TimeSource
    {
        static public final TimeSource INSTANCE = new CurrentMillisTimeSource();

        @Override
        public long getCurrentTime()
        {
            return System.currentTimeMillis();
        }

        @Override
        @Nonnull
        public TimeUnit getTimeUnit()
        {
            return TimeUnit.MILLISECONDS;
        }
    }
}
