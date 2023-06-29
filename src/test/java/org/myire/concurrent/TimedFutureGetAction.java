/*
 * Copyright 2011, 2013, 2016-2017, 2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Blocking test action that calls {@code Future.get(long, TimeUnit)}.
 */
class TimedFutureGetAction<T> extends FutureGetAction<T>
{
    private final long fTimeout;
    private final TimeUnit fUnit;
    private volatile TimeoutException fTimeoutException;


    TimedFutureGetAction(Future<T> pFuture, long pTimeout, TimeUnit pUnit)
    {
        super(pFuture);
        fTimeout = pTimeout;
        fUnit = pUnit;
    }


    @Override
    protected T performGet(Future<T> pFuture) throws ExecutionException, InterruptedException
    {
        try
        {
            return pFuture.get(fTimeout, fUnit);
        }
        catch (TimeoutException e)
        {
            fTimeoutException = e;
            return null;
        }
    }


    TimeoutException getTimeoutException()
    {
        return fTimeoutException;
    }
}
