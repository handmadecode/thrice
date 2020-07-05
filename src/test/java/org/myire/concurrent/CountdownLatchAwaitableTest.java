/*
 * Copyright 2017, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;


/**
 * Unit tests for the {@code Awaitable} implementation returned by
 * {@link Awaitables#wrap(CountDownLatch)}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class CountdownLatchAwaitableTest extends AwaitableTest
{
    @Override
    protected void createAwaitableAndConditionSetter(BiConsumer<Awaitable, Runnable> pDestination)
    {
        CountDownLatch aLatch = new CountDownLatch(1);
        pDestination.accept(Awaitables.wrap(aLatch), aLatch::countDown);
    }
}
