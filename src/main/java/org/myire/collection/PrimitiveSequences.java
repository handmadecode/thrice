/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static org.myire.util.Numbers.requireRangeWithinBounds;


/**
 * Factory methods for primitive sequence implementations.
 */
public final class PrimitiveSequences
{
    private PrimitiveSequences()
    {
        // Don't allow instantiations of utility classes.
    }


    /**
     * Get the empty {@code int} sequence. This is an immutable sequence of size 0.
     *
     * @return  The empty {@code IntSequence}, never null.
     */
    @Nonnull
    static public IntSequence emptyIntSequence()
    {
        return EmptyIntSequence.INSTANCE;
    }


    /**
     * Get the empty {@code long} sequence. This is an immutable sequence of size 0.
     *
     * @return  The empty {@code LongSequence}, never null.
     */
    @Nonnull
    static public LongSequence emptyLongSequence()
    {
        return EmptyLongSequence.INSTANCE;
    }


    /**
     * Get the empty {@code double} sequence. This is an immutable sequence of size 0.
     *
     * @return  The empty {@code DoubleSequence}, never null.
     */
    @Nonnull
    static public DoubleSequence emptyDoubleSequence()
    {
        return EmptyDoubleSequence.INSTANCE;
    }


    /**
     * Create an {@code IntSequence} that holds a single value.
     *
     * @param pValue    The sequence's singe {@code int} value.
     *
     * @return  A new {@code IntSequence}, never null.
     */
    @Nonnull
    static public IntSequence singleton(int pValue)
    {
        return new SingletonIntSequence(pValue);
    }


    /**
     * Create a {@code LongSequence} that holds a single value.
     *
     * @param pValue    The sequence's singe {@code long} value.
     *
     * @return  A new {@code LongSequence}, never null.
     */
    @Nonnull
    static public LongSequence singleton(long pValue)
    {
        return new SingletonLongSequence(pValue);
    }


    /**
     * Create a {@code DoubleSequence} that holds a single value.
     *
     * @param pValue    The sequence's singe {@code double} value.
     *
     * @return  A new {@code DoubleSequence}, never null.
     */
    @Nonnull
    static public DoubleSequence singleton(double pValue)
    {
        return new SingletonDoubleSequence(pValue);
    }


    /**
     * Create an {@code IntSequence} that is a wrapper around the values in an {@code int} array.
     * The returned {@code IntSequence} will reflect changes made to the values in the array.
     * Replacing a value in the array will also replace the value in the sequence.
     *
     * @param pValues   The array to wrap. The sequence will retain a reference to this array.
     *
     * @return  A new {@code IntSequence}, never null.
     *
     * @throws NullPointerException if {@code pValues} is null.
     */
    @Nonnull
    static public IntSequence wrap(@Nonnull int[] pValues)
    {
        return wrap(pValues, 0, pValues.length);
    }


    /**
     * Create an {@code IntSequence} that is a wrapper around a range of the values in an
     * {@code int} array. The returned {@code IntSequence} will reflect changes made to the values
     * in the array. Replacing a value in the array will also replace the value in the sequence.
     *
     * @param pValues   The array to wrap. The sequence will retain a reference to this array.
     * @param pOffset   The offset of the first value in the array that belongs to the sequence.
     * @param pLength   The number of values in the sequence, starting at the specified offset.
     *
     * @return  A new {@code IntSequence}, never null.
     *
     * @throws NullPointerException if {@code pValues} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength} is
     *                                   negative, or if {@code pOffset} is greater than
     *                                   {@code pValues.length - pLength}.
     */
    @Nonnull
    static public IntSequence wrap(
        @Nonnull int[] pValues,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        if (pLength == 0)
            return emptyIntSequence();
        else if (pLength == 1)
            return singleton(pValues[pOffset]);
        else
            return new IntArraySequence(pValues, pOffset, pLength);
    }


    /**
     * Create a {@code LongSequence} that is a wrapper around the values in a {@code long} array.
     * The returned {@code LongSequence} will reflect changes made to the values in the array.
     * Replacing a value in the array will also replace the value in the sequence.
     *
     * @param pValues   The array to wrap. The sequence will retain a reference to this array.
     *
     * @return  A new {@code LongSequence}, never null.
     *
     * @throws NullPointerException if {@code pValues} is null.
     */
    @Nonnull
    static public LongSequence wrap(@Nonnull long[] pValues)
    {
        return wrap(pValues, 0, pValues.length);
    }


    /**
     * Create a {@code LongSequence} that is a wrapper around a range of the values in a
     * {@code long} array. The returned {@code LongSequence} will reflect changes made to the values
     * in the array. Replacing a value in the array will also replace the value in the sequence.
     *
     * @param pValues   The array to wrap. The sequence will retain a reference to this array.
     * @param pOffset   The offset of the first value in the array that belongs to the sequence.
     * @param pLength   The number of values in the sequence, starting at the specified offset.
     *
     * @return  A new {@code LongSequence}, never null.
     *
     * @throws NullPointerException if {@code pValues} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength} is
     *                                   negative, or if {@code pOffset} is greater than
     *                                   {@code pValues.length - pLength}.
     */
    @Nonnull
    static public LongSequence wrap(
        @Nonnull long[] pValues,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        if (pLength == 0)
            return emptyLongSequence();
        else if (pLength == 1)
            return singleton(pValues[pOffset]);
        else
            return new LongArraySequence(pValues, pOffset, pLength);
    }


    /**
     * Create a {@code DoubleSequence} that is a wrapper around the values in a {@code double}
     * array. The returned {@code DoubleSequence} will reflect changes made to the values in the
     * array. Replacing a value in the array will also replace the value in the sequence.
     *
     * @param pValues   The array to wrap. The sequence will retain a reference to this array.
     *
     * @return  A new {@code DoubleSequence}, never null.
     *
     * @throws NullPointerException if {@code pValues} is null.
     */
    @Nonnull
    static public DoubleSequence wrap(@Nonnull double[] pValues)
    {
        return wrap(pValues, 0, pValues.length);
    }


    /**
     * Create a {@code DoubleSequence} that is a wrapper around a range of the values in a
     * {@code double} array. The returned {@code DoubleSequence} will reflect changes made to the
     * values in the array. Replacing a value in the array will also replace the value in the
     * sequence.
     *
     * @param pValues   The array to wrap. The sequence will retain a reference to this array.
     * @param pOffset   The offset of the first value in the array that belongs to the sequence.
     * @param pLength   The number of values in the sequence, starting at the specified offset.
     *
     * @return  A new {@code DoubleSequence}, never null.
     *
     * @throws NullPointerException if {@code pValues} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength} is
     *                                   negative, or if {@code pOffset} is greater than
     *                                   {@code pValues.length - pLength}.
     */
    @Nonnull
    static public DoubleSequence wrap(
        @Nonnull double[] pValues,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        if (pLength == 0)
            return emptyDoubleSequence();
        else if (pLength == 1)
            return singleton(pValues[pOffset]);
        else
            return new DoubleArraySequence(pValues, pOffset, pLength);
    }


    /**
     * Immutable implementation of an empty {@code IntSequence}.
     */
    static private class EmptyIntSequence implements IntSequence
    {
        static final IntSequence INSTANCE = new EmptyIntSequence();

        @Nonnegative
        @Override
        public int size()
        {
            return 0;
        }

        @Override
        public int valueAt(@Nonnegative int pIndex)
        {
            throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull IntConsumer pAction)
        {
            // Nothing do to in an empty sequence.
            requireNonNull(pAction);
        }

        @Override
        @Nonnull
        public PrimitiveIterator.OfInt iterator()
        {
            return PrimitiveIterators.emptyIntIterator();
        }
    }


    /**
     * Immutable implementation of an empty {@code LongSequence}.
     */
    static private class EmptyLongSequence implements LongSequence
    {
        static final LongSequence INSTANCE = new EmptyLongSequence();

        @Nonnegative
        @Override
        public int size()
        {
            return 0;
        }

        @Override
        public long valueAt(@Nonnegative int pIndex)
        {
            throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull LongConsumer pAction)
        {
            // Nothing do to in an empty sequence.
            requireNonNull(pAction);
        }

        @Override
        @Nonnull
        public PrimitiveIterator.OfLong iterator()
        {
            return PrimitiveIterators.emptyLongIterator();
        }
    }


    /**
     * Immutable implementation of an empty {@code DoubleSequence}.
     */
    static private class EmptyDoubleSequence implements DoubleSequence
    {
        static final DoubleSequence INSTANCE = new EmptyDoubleSequence();

        @Nonnegative
        @Override
        public int size()
        {
            return 0;
        }

        @Override
        public double valueAt(@Nonnegative int pIndex)
        {
            throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull DoubleConsumer pAction)
        {
            // Nothing do to in an empty sequence.
            requireNonNull(pAction);
        }

        @Override
        @Nonnull
        public PrimitiveIterator.OfDouble iterator()
        {
            return PrimitiveIterators.emptyDoubleIterator();
        }
    }


    /**
     * An {@code IntSequence} containing only one value.
     */
    static private class SingletonIntSequence implements IntSequence
    {
        private final int fValue;

        SingletonIntSequence(int pValue)
        {
            fValue = pValue;
        }

        @Nonnegative
        @Override
        public int size()
        {
            return 1;
        }

        @Override
        public int valueAt(@Nonnegative int pIndex)
        {
            if (pIndex == 0)
                return fValue;
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull IntConsumer pAction)
        {
            pAction.accept(fValue);
        }

        @Nonnull
        @Override
        public PrimitiveIterator.OfInt iterator()
        {
            return PrimitiveIterators.singletonIterator(fValue);
        }
    }


    /**
     * A {@code LongSequence} containing only one value.
     */
    static private class SingletonLongSequence implements LongSequence
    {
        private final long fValue;

        SingletonLongSequence(long pValue)
        {
            fValue = pValue;
        }

        @Nonnegative
        @Override
        public int size()
        {
            return 1;
        }

        @Override
        public long valueAt(@Nonnegative int pIndex)
        {
            if (pIndex == 0)
                return fValue;
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull LongConsumer pAction)
        {
            pAction.accept(fValue);
        }

        @Nonnull
        @Override
        public PrimitiveIterator.OfLong iterator()
        {
            return PrimitiveIterators.singletonIterator(fValue);
        }
    }


    /**
     * A {@code DoubleSequence} containing only one value.
     */
    static private class SingletonDoubleSequence implements DoubleSequence
    {
        private final double fValue;

        SingletonDoubleSequence(double pValue)
        {
            fValue = pValue;
        }

        @Nonnegative
        @Override
        public int size()
        {
            return 1;
        }

        @Override
        public double valueAt(@Nonnegative int pIndex)
        {
            if (pIndex == 0)
                return fValue;
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull DoubleConsumer pAction)
        {
            pAction.accept(fValue);
        }

        @Nonnull
        @Override
        public PrimitiveIterator.OfDouble iterator()
        {
            return PrimitiveIterators.singletonIterator(fValue);
        }
    }


    /**
     * Implementation of {@code IntSequence} backed by (a sub-range of) an {@code int} array.
     */
    static private class IntArraySequence implements IntSequence
    {
        private final int[] fValues;
        private final int fOffset;
        private final int fLength;

        /**
         * Create a new {@code IntArraySequence}.
         *
         * @param pValues   The underlying array. This reference will be used, no copy will be made.
         * @param pOffset   The offset of the first value in the array that belongs to the sequence.
         * @param pLength   The number of values in the sequence, starting at the specified offset.
         *
         * @throws NullPointerException if {@code pValues} is null.
         * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength}
         *                                   is negative, or if {@code pOffset} is greater than
         *                                   {@code pValues.length - pLength}.
         */
        IntArraySequence(@Nonnull int[] pValues, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fValues = requireNonNull(pValues);
            fOffset = requireRangeWithinBounds(pOffset, pLength, pValues.length);
            fLength = pLength;
        }

        @Override
        @Nonnegative
        public int size()
        {
            return fLength;
        }

        @Override
        public int valueAt(int pIndex)
        {
            if (pIndex >= 0 && pIndex < fLength)
                return fValues[fOffset + pIndex];
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull IntConsumer pAction)
        {
            for (int i=0; i<fLength; i++)
                pAction.accept(fValues[fOffset + i]);
        }

        @Nonnull
        @Override
        public PrimitiveIterator.OfInt iterator()
        {
            return PrimitiveIterators.arrayIterator(fValues, fOffset, fLength);
        }
    }


    /**
     * Implementation of {@code LongSequence} backed by (a sub-range of) a {@code long} array.
     */
    static private class LongArraySequence implements LongSequence
    {
        private final long[] fValues;
        private final int fOffset;
        private final int fLength;

        /**
         * Create a new {@code LongArraySequence}.
         *
         * @param pValues   The underlying array. This reference will be used, no copy will be made.
         * @param pOffset   The offset of the first value in the array that belongs to the sequence.
         * @param pLength   The number of values in the sequence, starting at the specified offset.
         *
         * @throws NullPointerException if {@code pValues} is null.
         * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength}
         *                                   is negative, or if {@code pOffset} is greater than
         *                                   {@code pValues.length - pLength}.
         */
        LongArraySequence(@Nonnull long[] pValues, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fValues = requireNonNull(pValues);
            fOffset = requireRangeWithinBounds(pOffset, pLength, pValues.length);
            fLength = pLength;
        }

        @Override
        @Nonnegative
        public int size()
        {
            return fLength;
        }

        @Override
        public long valueAt(int pIndex)
        {
            if (pIndex >= 0 && pIndex < fLength)
                return fValues[fOffset + pIndex];
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull LongConsumer pAction)
        {
            for (int i=0; i<fLength; i++)
                pAction.accept(fValues[fOffset + i]);
        }

        @Nonnull
        @Override
        public PrimitiveIterator.OfLong iterator()
        {
            return PrimitiveIterators.arrayIterator(fValues, fOffset, fLength);
        }
    }


    /**
     * Implementation of {@code DoubleSequence} backed by (a sub-range of) a {@code double} array.
     */
    static private class DoubleArraySequence implements DoubleSequence
    {
        private final double[] fValues;
        private final int fOffset;
        private final int fLength;

        /**
         * Create a new {@code DoubleArraySequence}.
         *
         * @param pValues   The underlying array. This reference will be used, no copy will be made.
         * @param pOffset   The offset of the first value in the array that belongs to the sequence.
         * @param pLength   The number of values in the sequence, starting at the specified offset.
         *
         * @throws NullPointerException if {@code pValues} is null.
         * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength}
         *                                   is negative, or if {@code pOffset} is greater than
         *                                   {@code pValues.length - pLength}.
         */
        DoubleArraySequence(@Nonnull double[] pValues, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fValues = requireNonNull(pValues);
            fOffset = requireRangeWithinBounds(pOffset, pLength, pValues.length);
            fLength = pLength;
        }

        @Override
        @Nonnegative
        public int size()
        {
            return fLength;
        }

        @Override
        public double valueAt(int pIndex)
        {
            if (pIndex >= 0 && pIndex < fLength)
                return fValues[fOffset + pIndex];
            else
                throw new IndexOutOfBoundsException(String.valueOf(pIndex));
        }

        @Override
        public void forEach(@Nonnull DoubleConsumer pAction)
        {
            for (int i=0; i<fLength; i++)
                pAction.accept(fValues[fOffset + i]);
        }

        @Nonnull
        @Override
        public PrimitiveIterator.OfDouble iterator()
        {
            return PrimitiveIterators.arrayIterator(fValues, fOffset, fLength);
        }
    }
}
