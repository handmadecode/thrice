/*
 * Copyright 2023 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.util.concurrent.CompletableFuture;


/**
 * {@code CompletableFuture} subclass that implements {@code PollableFuture}.
 *
 * @param <T> The result type returned by this instance.
 */
public class PollableCompletableFuture<T> extends CompletableFuture<T> implements PollableFuture<T>
{
    // No additional methods.
}
