/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

/**
 * Unit tests for {@code org.myire.util.TimeSource.CurrentMillisTimeSource}.
 */
public class CurrentMillisTimeSourceTest extends TimeSourceBaseTest
{
    @Override
    protected TimeSource createInstance()
    {
        return TimeSource.CurrentMillisTimeSource.INSTANCE;
    }
}
