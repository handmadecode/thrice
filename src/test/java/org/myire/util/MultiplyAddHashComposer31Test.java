/*
 * Copyright 2016 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

/**
 * Unit tests for {@link org.myire.util.MultiplyAddHashComposer#FACTOR31}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class MultiplyAddHashComposer31Test extends HashComposerTest
{
    @Override
    protected HashComposer getInstance()
    {
        return MultiplyAddHashComposer.FACTOR31;
    }
}
