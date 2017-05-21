/*
 * Copyright 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

/**
 * Base class for blocking test actions that are run in separate threads and need to be synchronized
 * with actions in the main test thread.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
class BlockingTestAction extends ConcurrentTestAction
{
    private final BlockingCall fCall;
    private volatile boolean fWasInterrupted;


    /**
     * Create a new {@code BlockingTestAction} with no {@code BlockingCall}. The
     * {@code performBlockingCall()} method will effectively be a no-op.
     */
    BlockingTestAction()
    {
        this(null);
    }


    /**
     * Create a new {@code BlockingTestAction}.
     *
     * @param pCall The blocking call to invoke in {@link #performBlockingCall()}, possibly null.
     */
    BlockingTestAction(BlockingCall pCall)
    {
        fCall = pCall;
    }


    /**
     * Perform the blocking call and catch any interruption. If an {@code InterruptedException} is
     * caught, {@link #wasInterrupted()} will return true.
     */
    @Override
    protected void perform()
    {
        try
        {
            performBlockingCall();
        }
        catch (InterruptedException e)
        {
            fWasInterrupted = true;
        }
    }


    /**
     * Perform the action's blocking call.
     *
     * @throws InterruptedException if the blocking call is interrupted before it returns.
     */
    protected void performBlockingCall() throws InterruptedException
    {
        if (fCall != null)
            fCall.call();
    }


    /**
     * Was the blocking call interrupted?
     *
     * @return  True if the blocking call performed in the {@code performBlockingCall()} method was
     *          interrupted, false if it wasn't or hasn't returned yet.
     */
    boolean wasInterrupted()
    {
        return fWasInterrupted;
    }


    /**
     * A blocking call that may be interrupted.
     */
    @FunctionalInterface
    interface BlockingCall
    {
        void call() throws InterruptedException;
    }
}
