/*
 * Copyright 2021 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * Unit tests for the {@link org.myire.concurrent.PollableFuture} implementation returned by
 * {@link org.myire.concurrent.PollableFuture#of(java.util.concurrent.CompletableFuture)}.
 */
public class PollableCompletableFutureTest
{
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
     * Calls to {@code getNow} should be delegated.
     */
    @Test
    public void getNowIsDelegated()
    {
        // Given
        CompletableFuture<String> aDelegate = newMockCompletableFuture();
        PollableFuture<String> aFuture = PollableFuture.of(aDelegate);

        // When
        aFuture.getNow("");

        // Then
        verify(aDelegate).getNow("");
        verifyNoMoreInteractions(aDelegate);
    }


    /**
     * Mock a {@code CompletableFuture<T>}, suppressing the unchecked cast.
     *
     * @param <T>   The type to consume.
     *
     * @return  A mocked {@code CompletableFuture<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> CompletableFuture<T> newMockCompletableFuture()
    {
        return (CompletableFuture<T>) mock(CompletableFuture.class);
    }
}
