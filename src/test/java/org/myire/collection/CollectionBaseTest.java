/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Base test class for collection related classes.
 *
 * @param <T>   The element type used in the tests.
 */
abstract public class CollectionBaseTest<T>
{
    /**
     * Create a random {@code T} instance.
     *
     * @return  A new {@code T} instance.
     */
    abstract protected T randomElement();

    /**
     * Create a new {@code T} array.
     *
     * @param pLength   The array's length.
     *
     * @return  A new {@code T} array.
     */
    abstract protected T[] newArray(int pLength);


    /**
     * Create an array of random elements.
     *
     * @param pNumElements  The number of elements to create.
     *
     * @return  An array with the specified number of random elements.
     */
    protected T[] randomElementArray(int pNumElements)
    {
        T[] aElements = newArray(pNumElements);
        for (int i=0; i<pNumElements; i++)
            aElements[i] = randomElement();

        return aElements;
    }


    /**
     * Create a random collection length that isn't one of the special cases 0 or 1.
     *
     * @return  A random number between 2 and 256.
     */
    static protected int randomCollectionLength()
    {
        return ThreadLocalRandom.current().nextInt(2, 256);
    }
}
