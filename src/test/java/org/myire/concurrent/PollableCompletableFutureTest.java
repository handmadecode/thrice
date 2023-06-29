/*
 * Copyright 2021, 2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for {@link org.myire.concurrent.PollableCompletableFuture}.
 */
public class PollableCompletableFutureTest
{
    /**
     * Calling {@code complete} should succeed when the {@code PollableCompletableFuture} isn't
     * completed.
     */
    @Test
    public void completeSucceedsWhenNotCompleted()
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();

        // Then
        assertTrue(aFuture.complete("someResult"));
    }


    /**
     * Calling {@code complete} should fail when the {@code PollableCompletableFuture} already is
     * completed.
     */
    @Test
    public void completeFailsWhenCompleted()
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.complete("someResult");

        // Then
        assertFalse(aFuture.complete("anotherResult"));
    }


    /**
     * Calling {@code complete} should fail when the {@code PollableCompletableFuture} already is
     * completed exceptionally.
     */
    @Test
    public void completeFailsWhenCompletedExceptionally()
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.completeExceptionally(new Exception());

        // Then
        assertFalse(aFuture.complete("someResult"));
    }


    /**
     * Calling {@code complete} should fail when the {@code PollableCompletableFuture} already is
     * cancelled.
     */
    @Test
    public void completeFailsWhenCancelled()
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.cancel(false);

        // Then
        assertFalse(aFuture.complete("someResult"));
    }


    /**
     * Calling {@code completeExceptionally} should succeed when the
     * {@code PollableCompletableFuture} isn't completed.
     */
    @Test
    public void completeExceptionallySucceedsWhenNotCompleted()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // Then
        assertTrue(aFuture.completeExceptionally(new Exception()));
    }


    /**
     * Calling {@code completeExceptionally} should fail when the {@code PollableCompletableFuture}
     * is completed.
     */
    @Test
    public void completeExceptionallyFailsWhenCompleted()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.complete("blah");

        // Then
        assertFalse(aFuture.completeExceptionally(new Exception()));
    }


    /**
     * Calling {@code completeExceptionally} should fail when the {@code PollableCompletableFuture}
     * already has completed exceptionally.
     */
    @Test
    public void completeExceptionallyFailsWhenCompletedExceptionally()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.completeExceptionally(new Exception());

        // Then
        assertFalse(aFuture.completeExceptionally(new Exception()));
    }


    /**
     * Calling {@code completeExceptionally} should fail when the {@code PollableCompletableFuture}
     * has been cancelled.
     */
    @Test
    public void completeExceptionallyFailsWhenCancelled()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.cancel(false);

        // Then
        assertFalse(aFuture.completeExceptionally(new Exception()));
    }


    /**
     * Calling {@code cancel} should succeed when the {@code PollableCompletableFuture} isn't
     * completed.
     */
    @Test
    public void cancelSucceedsWhenNotCompleted()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // Then
        assertTrue(aFuture.cancel(false));
    }


    /**
     * Calling {@code cancel} should fail when the {@code PollableCompletableFuture} is completed.
     */
    @Test
    public void cancelFailsWhenCompleted()
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.complete("done");

        // Then
        assertFalse(aFuture.cancel(false));
    }


    /**
     * Calling {@code cancel} should fail when the {@code PollableCompletableFuture} already has
     * completed exceptionally.
     */
    @Test
    public void cancelFailsWhenCompletedExceptionally()
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.completeExceptionally(new Exception());

        // Then
        assertFalse(aFuture.cancel(true));
    }


    /**
     * Calling {@code cancel} should succeed when the {@code PollableCompletableFuture} already has
     * been cancelled.
     */
    @Test
    public void cancelSucceedsWhenAlreadyCancelled()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.cancel(true);

        // Then
        assertTrue(aFuture.cancel(true));
    }


    /**
     * Calling {@code cancel(false)} should cancel a {@code PollableCompletableFuture} if it isn't
     * completed.
     */
    @Test
    public void cancelWithMayInterruptFalseCancels()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        boolean aWasCancelled = aFuture.cancel(false);

        // Then
        assertAll(
            () -> assertTrue(aWasCancelled),
            () -> assertTrue(aFuture.isCancelled())
        );
    }


    /**
     * Calling {@code cancel(false)} should cancel a {@code PollableCompletableFuture} if it isn't
     * completed.
     */
    @Test
    public void cancelWithMayInterruptTrueCancels()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        boolean aWasCancelled = aFuture.cancel(true);

        // Then
        assertAll(
            () -> assertTrue(aWasCancelled),
            () -> assertTrue(aFuture.isCancelled())
        );
    }


    /**
     * The {@code get} method should return the value specified in the call to {@code complete}.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getReturnsValueFromComplete() throws InterruptedException
    {
        // Given (a thread calls get() and is blocked because the Future isn't completed)
        String aValue = "racoon";
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aFuture);
        aGetAction.startAndAwaitRun();

        // When
        aFuture.complete(aValue);

        // Then (the call to get() should have returned the result)
        assertAll(
            () -> assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS)),
            () -> assertEquals(aValue, aGetAction.getResult())
        );
    }


    /**
     * The {@code get} method should block when the {@code PollableCompletableFuture} isn't
     * completed.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getBlocksWhenNotCompleted() throws InterruptedException
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aFuture);

        try
        {
            // When (the get action should be blocked and its thread shouldn't terminate)
            aGetAction.startAndAwaitRun();

            // Then
            assertFalse(aGetAction.timedJoin(100, TimeUnit.MILLISECONDS));
        }
        finally
        {
            // Release the thread calling get().
            aFuture.complete(null);
        }
    }


    /**
     * The {@code get} method should throw an {@code ExecutionException} when the
     * {@code PollableCompletableFuture} completed exceptionally.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getThrowsWhenCompletedExceptionally() throws InterruptedException
    {
        // Given
        Exception aException = new NullPointerException("Whoops");
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aFuture);
        aGetAction.startAndAwaitRun();

        // When
        aFuture.completeExceptionally(aException);

        // Then (the call to get() should have thrown an ExecutionException containing the
        // Exception passed to completeExceptionally())
        assertAll(
            () -> assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS)),
            () -> assertSame(aException, aGetAction.getExecutionException().getCause())
        );
    }


    /**
     * The {@code get} method should throw a {@code CancellationException} when the
     * {@code PollableCompletableFuture} has been cancelled.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getThrowsWhenCancelled() throws InterruptedException
    {
        // Given
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();
        FutureGetAction<String> aGetAction = new FutureGetAction<>(aFuture);
        aGetAction.startAndAwaitRun();

        // When
        aFuture.cancel(false);

        // Then (the call to get() should have thrown a CancellationException)
        assertAll(
            () -> assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS)),
            () -> assertNotNull(aGetAction.getCancellationException())
        );
    }


    /**
     * The {@code get(long, TimeUnit)} method should return the value specified in the call to
     * {@code complete}.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetReturnsValueFromComplete() throws InterruptedException
    {
        // Given
        String aValue = "moronic";
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();
        TimedFutureGetAction<String> aGetAction = new TimedFutureGetAction<>(aFuture, 1, TimeUnit.DAYS);
        aGetAction.startAndAwaitRun();

        // When
        aFuture.complete(aValue);

        // Then (the call to get() should have returned the result)
        assertAll(
            () -> assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS)),
            () -> assertEquals(aValue, aGetAction.getResult())
        );
    }


    /**
     * The {@code get(long, TimeUnit} method should throw a {@code TimeoutException} if the
     * {@code PollableCompletableFuture} isn't completed when the waiting time elapses.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetThrowsWhenNotCompleted() throws InterruptedException
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();
        TimedFutureGetAction<Object> aGetAction =
            new TimedFutureGetAction<>(aFuture, 10, TimeUnit.MILLISECONDS);

        // When (the Future is not completed before the waiting time elapses)
        aGetAction.startAndAwaitRun();

        // Then
        assertAll(
            () -> assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS)),
            () -> assertNotNull(aGetAction.getTimeoutException())
        );
    }


    /**
     * The {@code get(long, TimeUnit} method should throw an {@code ExecutionException} when the
     * {@code PollableCompletableFuture} completed exceptionally.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetThrowsWhenCompletedExceptionally() throws InterruptedException
    {
        // Given
        Exception aException = new IllegalArgumentException("Can't have that here");
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();
        TimedFutureGetAction<Object> aGetAction = new TimedFutureGetAction<>(aFuture, 1, TimeUnit.DAYS);
        aGetAction.startAndAwaitRun();

        // When
        aFuture.completeExceptionally(aException);

        // Then (the call to get() should have thrown an ExecutionException containing the
        // Exception passed to completeExceptionally())
        assertAll(
            () -> assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS)),
            () -> assertSame(aException, aGetAction.getExecutionException().getCause())
        );
    }


    /**
     * The {@code get(long, TimeUnit} method should throw a {@code CancellationException} when the
     * {@code PollableCompletableFuture} has been cancelled.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void timedGetThrowsWhenCancelled() throws InterruptedException
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();
        TimedFutureGetAction<Object> aGetAction = new TimedFutureGetAction<>(aFuture, 1, TimeUnit.DAYS);
        aGetAction.startAndAwaitRun();

        // When
        aFuture.cancel(false);

        // Then (the call to get() should have thrown a CancellationException)
        assertAll(
            () -> assertTrue(aGetAction.timedJoin(10, TimeUnit.SECONDS)),
            () -> assertNotNull(aGetAction.getCancellationException())
        );
    }


    /**
     * The {@code getNow} method should return the value specified in the call to {@code complete}.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getNowReturnsValueFromComplete() throws InterruptedException
    {
        // Given (a PollableCompletableFuture is completed from another thread)
        String aValue = "completed";
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();
        Thread aThread = new Thread(() -> aFuture.complete(aValue));
        aThread.start();
        aThread.join();

        // When
        String aReturnedValue = aFuture.getNow(null);

        // Then
        assertEquals(aValue, aReturnedValue);
    }


    /**
     * The {@code getNow} method should return the specified absence value if the
     * {@code PollableCompletableFuture} isn't completed.
     */
    @Test
    public void getNowReturnsAbsenceValueIfNotCompleted()
    {
        // Given (a PollableCompletableFuture that isn't completed)
        String aAbsenceValue = "not completed";
        PollableCompletableFuture<String> aFuture = new PollableCompletableFuture<>();

        // When
        String aReturnedValue = aFuture.getNow(aAbsenceValue);

        // Then
        assertEquals(aAbsenceValue, aReturnedValue);
    }


    /**
     * The {@code getNow} method should throw a {@code CompletionException} when the
     * {@code PollableCompletableFuture} completed exceptionally.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getNowThrowsWhenCompletedExceptionally() throws InterruptedException
    {
        // Given (a PollableCompletableFuture is completed exceptionally from another thread)
        Exception aException = new NullPointerException("No null here");
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();
        Thread aThread = new Thread(() -> aFuture.completeExceptionally(aException));
        aThread.start();
        aThread.join();

        // Then
        CompletionException aCompletionException =
            assertThrows(
                CompletionException.class,
                () -> aFuture.getNow(null));
        assertSame(aException, aCompletionException.getCause());
    }


    /**
     * The {@code getNow} method should throw a {@code CancellationException} when the
     * {@code PollableCompletableFuture} has been cancelled.
     *
     * @throws InterruptedException if the test is interrupted.
     */
    @Test
    public void getNowThrowsWhenCancelled() throws InterruptedException
    {
        // Given (a PollableCompletableFuture is cancelled from another thread)
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();
        Thread aThread = new Thread(() -> aFuture.cancel(true));
        aThread.start();
        aThread.join();

        // Then
        assertThrows(
            CancellationException.class,
            () -> aFuture.getNow(null)
        );
    }


    /**
     * A new {@code PollableCompletableFuture} should not be completed nor cancelled.
     */
    @Test
    public void newInstanceIsNotCompleted()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // Then
        assertAll(
            () -> assertFalse(aFuture.isDone()),
            () -> assertFalse(aFuture.isCompletedExceptionally()),
            () -> assertFalse(aFuture.isCancelled())
        );
    }


    /**
     * A {@code PollableCompletableFuture} that completed normally should be done but not cancelled.
     */
    @Test
    public void completedInstanceHasCorrectState()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.complete(new Object());

        // Then
        assertAll(
            () -> assertTrue(aFuture.isDone()),
            () -> assertFalse(aFuture.isCompletedExceptionally()),
            () -> assertFalse(aFuture.isCancelled())
        );
    }


    /**
     * A {@code PollableCompletableFuture} that completed exceptionally should be done but not
     * cancelled.
     */
    @Test
    public void exceptionallyCompletedInstanceHasCorrectState()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.completeExceptionally(new Exception());

        // Then
        assertAll(
            () -> assertTrue(aFuture.isDone()),
            () -> assertTrue(aFuture.isCompletedExceptionally()),
            () -> assertFalse(aFuture.isCancelled())
        );
    }


    /**
     * A {@code PollableCompletableFuture} that was cancelled should be done and cancelled.
     */
    @Test
    public void cancelledInstanceHasCorrectState()
    {
        // Given
        PollableCompletableFuture<Object> aFuture = new PollableCompletableFuture<>();

        // When
        aFuture.cancel(true);

        // Then
        assertAll(
            () -> assertTrue(aFuture.isDone()),
            () -> assertTrue(aFuture.isCompletedExceptionally()),
            () -> assertTrue(aFuture.isCancelled())
        );
    }
}
