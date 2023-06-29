/*
 * Copyright 2021, 2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the {@link org.myire.concurrent.PollableFuture} implementation returned by
 * {@link org.myire.concurrent.PollableFuture#of(Future)} and
 * {@link org.myire.concurrent.PollableFuture#of(CompletableFuture)}.
 */
public class DelegatingPollableFutureTest
{
    /**
     * Calling {@code of(Future)} with a null argument should throw a {@code NullPointerException}.
     */
    @Test
    public void ofNullFutureThrows()
    {
        // Given
        Future<Object> aDelegate = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> PollableFuture.of(aDelegate)
        );
    }


    /**
     * Calls to {@code cancel} should be delegated.
     */
    @Test
    public void cancelIsDelegated()
    {
        // Given
        Future<String> aDelegate = newMockFuture();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        aFuture.cancel(false);

        // Then
        verify(aDelegate).cancel(false);
        verifyNoMoreInteractions(aDelegate);
    }


    /**
     * Calls to {@code isCancelled} should be delegated.
     */
    @Test
    public void isCancelledIsDelegated()
    {
        // Given
        Future<String> aDelegate = newMockFuture();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        aFuture.isCancelled();

        // Then
        verify(aDelegate).isCancelled();
        verifyNoMoreInteractions(aDelegate);
    }


    /**
     * Calls to {@code isDone} should be delegated.
     */
    @Test
    public void isDoneIsDelegated()
    {
        // Given
        Future<String> aDelegate = newMockFuture();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        aFuture.isDone();

        // Then
        verify(aDelegate).isDone();
        verifyNoMoreInteractions(aDelegate);
    }


    /**
     * Calls to {@code get} should be delegated.
     *
     * @throws ExecutionException   never
     * @throws InterruptedException if the test is unexpectedly interrupted.
     */
    @Test
    public void getIsDelegated() throws ExecutionException, InterruptedException
    {
        // Given
        Future<String> aDelegate = newMockFuture();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        aFuture.get();

        // Then
        verify(aDelegate).get();
        verifyNoMoreInteractions(aDelegate);
    }


    /**
     * Calls to {@code get(long, TimeUnit)} should be delegated.
     *
     * @throws ExecutionException   never
     * @throws TimeoutException     never
     * @throws InterruptedException if the test is unexpectedly interrupted.
     */
    @Test
    public void getWithTimeoutIsDelegated() throws ExecutionException, TimeoutException, InterruptedException
    {
        // Given
        Future<String> aDelegate = newMockFuture();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        aFuture.get(10, TimeUnit.SECONDS);

        // Then
        verify(aDelegate).get(10, TimeUnit.SECONDS);
        verifyNoMoreInteractions(aDelegate);
    }


    /**
     * The {@code getNow} method should return the delegate's result when done.
     *
     * @throws ExecutionException   never
     * @throws InterruptedException if the test is unexpectedly interrupted.
     */
    @Test
    public void getNowReturnsDelegatesResultWhenDone() throws ExecutionException, InterruptedException
    {
        // Given
        String aResult = "the result";
        Future<String> aDelegate = newMockFuture();
        when(aDelegate.isDone()).thenReturn(true);
        when(aDelegate.get()).thenReturn(aResult);
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        String aReturnedValue = aFuture.getNow(null);

        // Then
        assertEquals(aResult, aReturnedValue);
    }


    /**
     * The {@code getNow} method should return the specified absence value if the delegate hasn't
     * completed.
     */
    @Test
    public void getNowReturnsAbsenceValueIfDelegateHasNotCompleted()
    {
        // Given
        String aAbsenceValue = "not completed";
        Future<String> aDelegate = newMockFuture();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        String aReturnedValue = aFuture.getNow(aAbsenceValue);

        // Then
        assertEquals(aAbsenceValue, aReturnedValue);
    }


    /**
     * The {@code getNow} method should return the specified absence value if the call to the
     * delegate's {@code get} method throws an {@code InterruptedException}. The calling thread
     * should have its interrupt status set when {@code getNow} returns.
     *
     * @throws ExecutionException   never
     * @throws InterruptedException if the test is unexpectedly interrupted.
     */
    @Test
    public void getNowReturnsAbsenceValueWhenDelegateThrowsInterruptedException()
        throws ExecutionException, InterruptedException
    {
        // Given
        String aAbsenceValue = "not completed";
        Future<String> aDelegate = newMockFuture();
        when(aDelegate.isDone()).thenReturn(true);
        when(aDelegate.get()).thenThrow(new InterruptedException());
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        String aReturnedValue = aFuture.getNow(aAbsenceValue);

        // Then
        assertAll(
            () -> assertEquals(aAbsenceValue, aReturnedValue),
            () -> assertTrue(Thread.interrupted())
        );
    }


    /**
     * The {@code getNow} method should throw a {@code CompletionException} when the delegate has
     * completed with an exception.
     *
     * @throws ExecutionException   never
     * @throws InterruptedException if the test is unexpectedly interrupted.
     */
    @Test
    public void getNowThrowsWhenDelegateHasCompletedExceptionally() throws ExecutionException, InterruptedException
    {
        // Given
        Exception aCause = new NullPointerException("Oh dear");
        Future<String> aDelegate = newMockFuture();
        when(aDelegate.isDone()).thenReturn(true);
        when(aDelegate.get()).thenThrow(new ExecutionException(aCause));
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // Then
        CompletionException aCompletionException =
            assertThrows(
                CompletionException.class,
                () -> aFuture.getNow(""));
        assertSame(aCause, aCompletionException.getCause());
    }


    /**
     * The {@code getNow} method should throw a {@code CancellationException} when the delegate has
     * been canceled.
     *
     * @throws ExecutionException   never
     * @throws InterruptedException if the test is unexpectedly interrupted.
     */
    @Test
    public void getNowThrowsWhenDelegateHasBeenCanceled() throws ExecutionException, InterruptedException
    {
        // Given
        Future<String> aDelegate = newMockFuture();
        when(aDelegate.isCancelled()).thenReturn(true);
        when(aDelegate.isDone()).thenReturn(true);
        when(aDelegate.get()).thenThrow(new CancellationException());
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // Then
        assertThrows(
            CancellationException.class,
            () -> aFuture.getNow(null)
        );
    }


    /**
     * Calling {@code of(CompletableFuture)} with a null argument should throw a
     * {@code NullPointerException}.
     */
    @Test
    public void ofNullCompletableFutureThrows()
    {
        // Given
        CompletableFuture<Object> aDelegate = null;

        // When
        assertThrows(
            NullPointerException.class,
            () -> PollableFuture.of(aDelegate)
        );
    }


    /**
     * The {@code getNow} method should return the result of a completed {@code CompletableFuture}
     * delegate.
     */
    @Test
    public void getNowReturnsResultFromCompletedCompletableFutureDelegate()
    {
        // Given
        String aResult ="muhaawhaaw";
        CompletableFuture<String> aDelegate = new CompletableFuture<>();
        aDelegate.complete(aResult);
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        String aReturnedValue = aFuture.getNow(null);

        // Then
        assertEquals(aResult, aReturnedValue);
    }


    /**
     * The {@code getNow} method should return the specified absence value if a
     * {@code CompletableFuture} delegate hasn't completed.
     */
    @Test
    public void getNowReturnsAbsenceValueForNotCompletedCompletableFutureDelegate()
    {
        // Given
        String aAbsenceValue = "not completed";
        CompletableFuture<String> aDelegate = new CompletableFuture<>();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        String aReturnedValue = aFuture.getNow(aAbsenceValue);

        // Then
        assertEquals(aAbsenceValue, aReturnedValue);
    }


    /**
     * The {@code getNow} method should return the specified absence value if a
     * {@code CompletableFuture} delegate returns the absence value when the calling thread's
     * interrupt status is set. The interrupt status should be preserved.
     */
    @Test
    public void getNowReturnsAbsenceValueWhenCompletableFutureReturnsWithInterruptSet()
    {
        // Given
        String aAbsenceValue = "not completed";
        CompletableFuture<String> aDelegate = new CompletableFuture<>();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        Thread.currentThread().interrupt();
        String aReturnedValue = aFuture.getNow(aAbsenceValue);

        // Then
        assertAll(
            () -> assertEquals(aAbsenceValue, aReturnedValue),
            () -> assertTrue(Thread.interrupted())
        );
    }


    /**
     * The {@code getNow} method should throw a {@code CompletionException} when a
     * {@code CompletableFuture} delegate has completed with an exception.
     */
    @Test
    public void getNowThrowsWhenCompletableFutureHasCompletedExceptionally()
    {
        // Given
        Exception aCause = new NullPointerException("My, oh my");
        CompletableFuture<String> aDelegate = new CompletableFuture<>();
        aDelegate.completeExceptionally(aCause);
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // Then
        CompletionException aCompletionException =
            assertThrows(
                CompletionException.class,
                () -> aFuture.getNow(""));
        assertSame(aCause, aCompletionException.getCause());
    }


    /**
     * The {@code getNow} method should throw a {@code CancellationException} when a
     * {@code CompletableFuture} delegate has been canceled.
     */
    @Test
    public void getNowThrowsWhenCompletableFutureHasBeenCanceled()
    {
        // Given
        CompletableFuture<String> aDelegate = new CompletableFuture<>();
        aDelegate.cancel(true);
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // Then
        assertThrows(
            CancellationException.class,
            () -> aFuture.getNow(null)
        );
    }


    /**
     * Mock a {@code Future<T>}, suppressing the unchecked cast.
     *
     * @param <T>   The type to consume.
     *
     * @return  A mocked {@code Future<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Future<T> newMockFuture()
    {
        return (Future<T>) mock(Future.class);
    }
}
