/*
 * Copyright 2016 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

/**
 * Unit tests for {@link org.myire.util.MultiplyAddHashComposer}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class MultiplyAddHashComposerTest extends HashComposerTest
{
    @Override
    protected HashComposer getInstance()
    {
        return new MultiplyAddHashComposer(63);
    }
}
