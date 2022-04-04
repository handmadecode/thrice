/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.concurrent.Immutable;


/**
 * The {@code Nothing} type represents an entity that has no content or meaning, but isn't
 * {@code null}. It is similar to Kotlin's and Scala's {@code Unit} type, or to Python's
 * {@code NoneType}. It can also be thought of as an equivalent to {@code java.lang.Void} with a
 * single instance.
 *<p>
 * The main use case is to return something that isn't {@code null} but still represents
 * nothingness.
 *<p>
 * This class cannot be instantiated; the singleton instance {@link Nothing#INSTANCE} should be
 * used. By definition, the {@link Nothing#INSTANCE} is immutable.
 */
@Immutable
public final class Nothing
{
    /** Singleton instance. */
    static public final Nothing INSTANCE = new Nothing();

    private Nothing()
    {
        // Do not allow other instances than the singleton.
    }
}
