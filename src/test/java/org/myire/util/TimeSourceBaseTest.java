/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Base unit test class for {@code org.myire.util.TimeSource} implementations.
 */
abstract public class TimeSourceBaseTest
{
    /**
     * Create an instance of the {@code TimeSource} to test.
     *
     * @return  A new {@code TimeSource} instance.
     */
    abstract protected TimeSource createInstance();


    /**
     * The value returned by {@code getCurrentTime} should be greater than or equal to previously
     * returned values.
     */
    @Test
    public void getCurrentTimeIsNotDecreasing()
    {
        // Given
        TimeSource aTimeSource = createInstance();

        // When
        long aTime1 = aTimeSource.getCurrentTime();
        long aTime2 = aTimeSource.getCurrentTime();

        // Then
        assertTrue(aTime2 >= aTime1);
    }


    /**
     * The {@code getTimeUnit} method should return a non-null instance.
     */
    @Test
    public void getTimeUnitReturnsNonNullInstance()
    {
        assertNotNull(createInstance().getTimeUnit());
    }


    /**
     * The {@code getCurrentTimeInUnit} method should throw a {@code NullPointerException} when
     * passed a null argument.
     */
    @Test
    public void getCurrentTimeInUnitThrowsForNullArgument()
    {
        assertThrows(
            NullPointerException.class,
            () -> createInstance().getCurrentTimeInUnit(null)
        );
    }


    /**
     * The value returned by {@code getCurrentTimeInUnit} should be greater than or equal to
     * previously returned values.
     */
    @Test
    public void getCurrentTimeInUnitIsNotDecreasing()
    {
        // Given
        TimeUnit aTimeUnit = TimeUnit.SECONDS;
        TimeSource aTimeSource = createInstance();

        // When
        long aTime1 = aTimeSource.getCurrentTimeInUnit(aTimeUnit);
        long aTime2 = aTimeSource.getCurrentTimeInUnit(aTimeUnit);

        // Then
        assertTrue(aTime2 >= aTime1);
    }
}
