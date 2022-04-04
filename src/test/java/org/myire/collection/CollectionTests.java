/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;


/**
 * Utility methods for the unit tests in the {@code org.myire.collection} package.
 */
public final class CollectionTests
{
    private CollectionTests()
    {
        // Don't allow instantiations of utility classes.
    }


    /**
     * Create an array of random {@code int} values.
     *
     * @param pNumValues    The number of random {@code int} values to generate.
     *
     * @return  An array with the specified number of {@code int} values.
     */
    static public int[] randomIntValues(int pNumValues)
    {
        int[] aValues = new int[pNumValues];
        for (int i=0; i<pNumValues; i++)
            aValues[i] = ThreadLocalRandom.current().nextInt();

        return aValues;
    }


    /**
     * Create an array of random {@code long} values.
     *
     * @param pNumValues    The number of random {@code long} values to generate.
     *
     * @return  An array with the specified number of {@code long} values.
     */
    static public long[] randomLongValues(int pNumValues)
    {
        long[] aValues = new long[pNumValues];
        for (int i=0; i<pNumValues; i++)
            aValues[i] = ThreadLocalRandom.current().nextLong();

        return aValues;
    }


    /**
     * Create an array of random {@code double} values.
     *
     * @param pNumValues    The number of random {@code double} values to generate.
     *
     * @return  An array with the specified number of {@code double} values.
     */
    static public double[] randomDoubleValues(int pNumValues)
    {
        double[] aValues = new double[pNumValues];
        for (int i=0; i<pNumValues; i++)
            aValues[i] = randomDoubleValue();

        return aValues;
    }


    /**
     * Generate a random {@code Integer}.
     *
     * @return  A random {@code Integer}.
     */
    static public Integer randomIntegerInstance()
    {
        return Integer.valueOf(ThreadLocalRandom.current().nextInt());
    }


    /**
     * Generate a random {@code Long}.
     *
     * @return  A random {@code Long}.
     */
    static public Long randomLongInstance()
    {
        return Long.valueOf(ThreadLocalRandom.current().nextLong());
    }


    /**
     * Generate a random {@code Double}.
     *
     * @return  A random {@code Double}.
     */
    static public Double randomDoubleInstance()
    {
        return Double.valueOf(randomDoubleValue());
    }


    /**
     * Generate a random {@code double} value.
     *
     * @return  A random {@code double} value.
     */
    static public double randomDoubleValue()
    {
        return ThreadLocalRandom.current().nextDouble(-(Double.MAX_VALUE/2), Double.MAX_VALUE/2);
    }


    /**
     * Create a mock {@code Consumer<T>}, suppressing the unchecked cast.
     *
     * @param <T>   The type to consume.
     *
     * @return  A mock {@code Consumer<T>}.
     */
    @SuppressWarnings("unchecked")
    static public <T> Consumer<T> createMockConsumer()
    {
        return (Consumer<T>) mock(Consumer.class);
    }
}
