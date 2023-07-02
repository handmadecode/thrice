/*
 * Copyright 2011, 2013, 2016-2017, 2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Blocking test action that calls {@code Future.get()}.
 */
class FutureGetAction<T> extends BlockingTestAction
{
    private final Future<T> fFuture;
    private volatile T fResult;
    private volatile ExecutionException fExecutionException;
    private volatile CancellationException fCancellationException;


    FutureGetAction(Future<T> pFuture)
    {
        fFuture = pFuture;
    }


    @Override
    protected void performBlockingCall() throws InterruptedException
    {
        try
        {
            fResult = performGet(fFuture);
        }
        catch (ExecutionException e)
        {
            fExecutionException = e;
        }
        catch (CancellationException e)
        {
            fCancellationException = e;
        }
    }


    protected T performGet(Future<T> pFuture) throws ExecutionException, InterruptedException
    {
        return pFuture.get();
    }


    T getResult()
    {
        return fResult;
    }


    ExecutionException getExecutionException()
    {
        return fExecutionException;
    }


    CancellationException getCancellationException()
    {
        return fCancellationException;
    }
}
