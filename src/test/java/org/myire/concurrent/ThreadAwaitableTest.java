/*
 * Copyright 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CountDownLatch;


/**
 * Unit tests for the {@code Awaitable} implementation returned by {@link Awaitables#wrap(Thread)}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class ThreadAwaitableTest extends AwaitableTest
{
    private final CountDownLatch fLatch = new CountDownLatch(1);
    private final Thread fThread = new Thread(this::run);


    @Override
    public void setup()
    {
        fThread.setDaemon(true);
        fThread.start();
        setup(Awaitables.wrap(fThread), this::terminate);
    }


    /**
     * Run method for the thread being tested. This method waits for the CountDownLatch that is
     * counted down in {@link #terminate()}.
     */
    private void run()
    {
        try
        {
            fLatch.await();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Count down the CountDownLatch that {@link #run()} waits for. This will allow {@link #run()}
     * to return and the thread being tested to terminate.
     */
    private void terminate()
    {
        fLatch.countDown();
        try
        {
            fThread.join();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
