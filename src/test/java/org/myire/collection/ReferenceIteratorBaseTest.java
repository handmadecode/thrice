/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;


/**
 * Base test for {@code Iterator} implementations that return reference types.
 */
abstract public class ReferenceIteratorBaseTest extends IteratorBaseTest<Object>
{
    @Override
    protected Object randomElement()
    {
        return new Object();
    }


    @Override
    protected Object[] newArray(int pLength)
    {
        return new Object[pLength];
    }
}
