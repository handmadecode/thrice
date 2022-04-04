/*
 * Copyright 2013, 2015, 2017, 2020-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection.empty;

import org.myire.collection.Sequence;
import org.myire.collection.Sequences;


/**
 * Unit tests for the {@code Sequence} implementation returned by {@link Sequences#emptySequence()}.
 */
public class EmptySequenceTest extends EmptySequenceBaseTest<Object>
{
    @Override
    Sequence<Object> createEmptySequence()
    {
        return Sequences.emptySequence();
    }
}
