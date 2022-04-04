/*
 * Copyright 2021-2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.collection;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.myire.annotation.Unreachable;
import static org.myire.util.Numbers.requireRangeWithinBounds;


/**
 * Factory methods for primitive iterator implementations.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class PrimitiveIterators
{
    @Unreachable
    private PrimitiveIterators()
    {
        // Don't allow instantiations of utility classes.
    }


    /**
     * Return a {@code PrimitiveIterator.OfInt} that contains no values.
     *
     * @return  A {@code PrimitiveIterator.OfInt} with no values.
     */
    @Nonnull
    static public PrimitiveIterator.OfInt emptyIntIterator()
    {
        return EmptyIntIterator.INSTANCE;
    }


    /**
     * Return a {@code PrimitiveIterator.OfLong} that contains no values.
     *
     * @return  A {@code PrimitiveIterator.OfLong} with no values.
     */
    @Nonnull
    static public PrimitiveIterator.OfLong emptyLongIterator()
    {
        return EmptyLongIterator.INSTANCE;
    }


    /**
     * Return a {@code PrimitiveIterator.OfDouble} that contains no values.
     *
     * @return  A {@code PrimitiveIterator.OfDouble} with no values.
     */
    @Nonnull
    static public PrimitiveIterator.OfDouble emptyDoubleIterator()
    {
        return EmptyDoubleIterator.INSTANCE;
    }


    /**
     * Create a {@code PrimitiveIterator.OfInt} that contains a single {@code int} value. The
     * returned instance doesn't support the {@code remove} operation.
     *
     * @param pValue    The iterator's single value.
     *
     * @return  A new {@code PrimitiveIterator.OfInt} for the specified value.
     */
    @Nonnull
    static public PrimitiveIterator.OfInt singletonIterator(int pValue)
    {
        return new SingletonIntIterator(pValue);
    }


    /**
     * Create a {@code PrimitiveIterator.OfLong} that contains a single {@code long} value. The
     * returned instance doesn't support the {@code remove} operation.
     *
     * @param pValue    The iterator's single value.
     *
     * @return  A new {@code PrimitiveIterator.OfLong} for the specified value.
     */
    @Nonnull
    static public PrimitiveIterator.OfLong singletonIterator(long pValue)
    {
        return new SingletonLongIterator(pValue);
    }


    /**
     * Create a {@code PrimitiveIterator.OfDouble} that contains a single {@code double} value. The
     * returned instance doesn't support the {@code remove} operation.
     *
     * @param pValue    The iterator's single value.
     *
     * @return  A new {@code PrimitiveIterator.OfDouble} for the specified value.
     */
    @Nonnull
    static public PrimitiveIterator.OfDouble singletonIterator(double pValue)
    {
        return new SingletonDoubleIterator(pValue);
    }


    /**
     * Create a {@code PrimitiveIterator.OfInt} that returns the elements of an {@code int} array.
     * The returned instance doesn't support the {@code remove} operation.
     *
     * @param pValues   The array to iterate over. The iterator will reflect changes made to the
     *                  elements in the array. Replacing an element in the array will also cause the
     *                  iterator to return the new element (unless the replaced element already has
     *                  been returned by the iteration).
     * @param pOffset   The offset of the first value to return from the iteration.
     * @param pLength   The number of values to return from the iteration.
     *
     * @return  A new {@code PrimitiveIterator.OfInt} for the specified array.
     *
     * @throws NullPointerException if {@code pValues} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength} is
     *                                   negative, or if {@code pOffset} is greater than
     *                                   {@code pValues.length - pLength}.
     */
    @Nonnull
    static public PrimitiveIterator.OfInt arrayIterator(
        @Nonnull int[] pValues,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        return new IntArrayIterator(pValues, pOffset, pLength);
    }


    /**
     * Create a {@code PrimitiveIterator.OfLong} that returns the elements of a {@code long} array.
     * The returned instance doesn't support the {@code remove} operation.
     *
     * @param pValues   The array to iterate over. The iterator will reflect changes made to the
     *                  elements in the array. Replacing an element in the array will also cause the
     *                  iterator to return the new element (unless the replaced element already has
     *                  been returned by the iteration).
     * @param pOffset   The offset of the first value to return from the iteration.
     * @param pLength   The number of values to return from the iteration.
     *
     * @return  A new {@code PrimitiveIterator.OfLong} for the specified array.
     *
     * @throws NullPointerException if {@code pValues} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength} is
     *                                   negative, or if {@code pOffset} is greater than
     *                                   {@code pValues.length - pLength}.
     */
    @Nonnull
    static public PrimitiveIterator.OfLong arrayIterator(
        @Nonnull long[] pValues,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        return new LongArrayIterator(pValues, pOffset, pLength);
    }


    /**
     * Create a {@code PrimitiveIterator.OfDouble} that returns the elements of a {@code double}
     * array. The returned instance doesn't support the {@code remove} operation.
     *
     * @param pValues   The array to iterate over. The iterator will reflect changes made to the
     *                  elements in the array. Replacing an element in the array will also cause the
     *                  iterator to return the new element (unless the replaced element already has
     *                  been returned by the iteration).
     * @param pOffset   The offset of the first value to return from the iteration.
     * @param pLength   The number of values to return from the iteration.
     *
     * @return  A new {@code PrimitiveIterator.OfDouble} for the specified array.
     *
     * @throws NullPointerException if {@code pValues} is null.
     * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength} is
     *                                   negative, or if {@code pOffset} is greater than
     *                                   {@code pValues.length - pLength}.
     */
    @Nonnull
    static public PrimitiveIterator.OfDouble arrayIterator(
        @Nonnull double[] pValues,
        @Nonnegative int pOffset,
        @Nonnegative int pLength)
    {
        return new DoubleArrayIterator(pValues, pOffset, pLength);
    }


    /**
     * An iterator for empty collections of {@code int} values.
     */
    static private class EmptyIntIterator implements PrimitiveIterator.OfInt
    {
        static final EmptyIntIterator INSTANCE = new EmptyIntIterator();

        @Override
        public boolean hasNext()
        {
            return false;
        }

        @Override
        public int nextInt()
        {
            throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull IntConsumer pAction)
        {
            requireNonNull(pAction);
        }
    }


    /**
     * An iterator for empty collections of {@code long} values.
     */
    static private class EmptyLongIterator implements PrimitiveIterator.OfLong
    {
        static final EmptyLongIterator INSTANCE = new EmptyLongIterator();

        @Override
        public boolean hasNext()
        {
            return false;
        }

        @Override
        public long nextLong()
        {
            throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull LongConsumer pAction)
        {
            requireNonNull(pAction);
        }
    }


    /**
     * An iterator for empty collections of {@code double} values.
     */
    static private class EmptyDoubleIterator implements PrimitiveIterator.OfDouble
    {
        static final EmptyDoubleIterator INSTANCE = new EmptyDoubleIterator();

        @Override
        public boolean hasNext()
        {
            return false;
        }

        @Override
        public double nextDouble()
        {
            throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull DoubleConsumer pAction)
        {
            requireNonNull(pAction);
        }
    }


    /**
     * Implementation of {@code PrimitiveIterator.OfInt} for a single {@code int} value. This
     * implementation does <b>not</b> support the {@code remove} operation; that method always
     * throws an {@code UnsupportedOperationException} (by falling back to the default
     * implementation in {@code java.util.Iterator}).
     */
    static private class SingletonIntIterator implements PrimitiveIterator.OfInt
    {
        private final int fValue;
        private boolean fHasNext = true;

        SingletonIntIterator(int pValue)
        {
            fValue = pValue;
        }

        @Override
        public boolean hasNext()
        {
            return fHasNext;
        }

        @Override
        public int nextInt()
        {
            if (!fHasNext)
                throw new NoSuchElementException();

            fHasNext = false;
            return fValue;
        }

        @Override
        public void forEachRemaining(@Nonnull IntConsumer pAction)
        {
            if (fHasNext)
            {
                fHasNext = false;
                pAction.accept(fValue);
            }
        }
    }


    /**
     * Implementation of {@code PrimitiveIterator.OfLong} for a single {@code long} value. This
     * implementation does <b>not</b> support the {@code remove} operation; that method always
     * throws an {@code UnsupportedOperationException} (by falling back to the default
     * implementation in {@code java.util.Iterator}).
     */
    static private class SingletonLongIterator implements PrimitiveIterator.OfLong
    {
        private final long fValue;
        private boolean fHasNext = true;

        SingletonLongIterator(long pValue)
        {
            fValue = pValue;
        }

        @Override
        public boolean hasNext()
        {
            return fHasNext;
        }

        @Override
        public long nextLong()
        {
            if (!fHasNext)
                throw new NoSuchElementException();

            fHasNext = false;
            return fValue;
        }

        @Override
        public void forEachRemaining(@Nonnull LongConsumer pAction)
        {
            if (fHasNext)
            {
                fHasNext = false;
                pAction.accept(fValue);
            }
        }
    }


    /**
     * Implementation of {@code PrimitiveIterator.OfDouble} for a single {@code double} value. This
     * implementation does <b>not</b> support the {@code remove} operation; that method always
     * throws an {@code UnsupportedOperationException} (by falling back to the default
     * implementation in {@code java.util.Iterator}).
     */
    static private class SingletonDoubleIterator implements PrimitiveIterator.OfDouble
    {
        private final double fValue;
        private boolean fHasNext = true;

        SingletonDoubleIterator(double pValue)
        {
            fValue = pValue;
        }

        @Override
        public boolean hasNext()
        {
            return fHasNext;
        }

        @Override
        public double nextDouble()
        {
            if (!fHasNext)
                throw new NoSuchElementException();

            fHasNext = false;
            return fValue;
        }

        @Override
        public void forEachRemaining(@Nonnull DoubleConsumer pAction)
        {
            if (fHasNext)
            {
                fHasNext = false;
                pAction.accept(fValue);
            }
        }
    }


    /**
     * Implementation of {@code PrimitiveIterator.OfInt} backed by an array. This implementation
     * does <b>not</b> support the {@code remove} operation; that method always throws an
     * {@code UnsupportedOperationException} (by falling back to the default implementation in
     * {@code java.util.Iterator}).
     */
    static private class IntArrayIterator implements PrimitiveIterator.OfInt
    {
        private final int[] fValues;
        private final int fLastPos;
        private int fNextPos;

        /**
         * Create a new {@code IntArrayIterator}.
         *
         * @param pValues   The array to iterate over. The iterator will reflect changes made to the
         *                  elements in the array. Replacing an element in the array will also cause
         *                  the iterator to return the new element (unless the replaced element
         *                  already has been returned by the iteration).
         * @param pOffset   The offset of the first value to return from the iteration.
         * @param pLength   The number of value to return from the iteration.
         *
         * @throws NullPointerException if {@code pValues} is null.
         * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength}
         *                                   is negative, or if {@code pOffset} is greater than
         *                                   {@code pValues.length - pLength}.
         */
        IntArrayIterator(@Nonnull int[] pValues, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fValues = requireNonNull(pValues);
            fNextPos = requireRangeWithinBounds(pOffset, pLength, pValues.length);
            fLastPos = pOffset + pLength;
        }

        @Override
        public boolean hasNext()
        {
            return fNextPos < fLastPos;
        }

        @Override
        public int nextInt()
        {
            if (fNextPos < fLastPos)
                return fValues[fNextPos++];
            else
                throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull IntConsumer pAction)
        {
            while (fNextPos < fLastPos)
                pAction.accept(fValues[fNextPos++]);
        }
    }


    /**
     * Implementation of {@code PrimitiveIterator.OfLong} backed by an array. This implementation
     * does <b>not</b> support the {@code remove} operation; that method always throws an
     * {@code UnsupportedOperationException} (by falling back to the default implementation in
     * {@code java.util.Iterator}).
     */
    static private class LongArrayIterator implements PrimitiveIterator.OfLong
    {
        private final long[] fValues;
        private final int fLastPos;
        private int fNextPos;

        /**
         * Create a new {@code LongArrayIterator}.
         *
         * @param pValues   The array to iterate over. The iterator will reflect changes made to the
         *                  elements in the array. Replacing an element in the array will also cause
         *                  the iterator to return the new element (unless the replaced element
         *                  already has been returned by the iteration).
         * @param pOffset   The offset of the first value to return from the iteration.
         * @param pLength   The number of value to return from the iteration.
         *
         * @throws NullPointerException if {@code pValues} is null.
         * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength}
         *                                   is negative, or if {@code pOffset} is greater than
         *                                   {@code pValues.length - pLength}.
         */
        LongArrayIterator(@Nonnull long[] pValues, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fValues = requireNonNull(pValues);
            fNextPos = requireRangeWithinBounds(pOffset, pLength, pValues.length);
            fLastPos = pOffset + pLength;
        }

        @Override
        public boolean hasNext()
        {
            return fNextPos < fLastPos;
        }

        @Override
        public long nextLong()
        {
            if (fNextPos < fLastPos)
                return fValues[fNextPos++];
            else
                throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull LongConsumer pAction)
        {
            while (fNextPos < fLastPos)
                pAction.accept(fValues[fNextPos++]);
        }
    }


    /**
     * Implementation of {@code PrimitiveIterator.OfDouble} backed by an array. This implementation
     * does <b>not</b> support the {@code remove} operation; that method always throws an
     * {@code UnsupportedOperationException} (by falling back to the default implementation in
     * {@code java.util.Iterator}).
     */
    static private class DoubleArrayIterator implements PrimitiveIterator.OfDouble
    {
        private final double[] fValues;
        private final int fLastPos;
        private int fNextPos;

        /**
         * Create a new {@code DoubleArrayIterator}.
         *
         * @param pValues   The array to iterate over. The iterator will reflect changes made to the
         *                  elements in the array. Replacing an element in the array will also cause
         *                  the iterator to return the new element (unless the replaced element
         *                  already has been returned by the iteration).
         * @param pOffset   The offset of the first value to return from the iteration.
         * @param pLength   The number of values to return from the iteration.
         *
         * @throws NullPointerException if {@code pValues} is null.
         * @throws IndexOutOfBoundsException if {@code pOffset} is negative, or if {@code pLength}
         *                                   is negative, or if {@code pOffset} is greater than
         *                                   {@code pValues.length - pLength}.
         */
        DoubleArrayIterator(@Nonnull double[] pValues, @Nonnegative int pOffset, @Nonnegative int pLength)
        {
            fValues = requireNonNull(pValues);
            fNextPos = requireRangeWithinBounds(pOffset, pLength, pValues.length);
            fLastPos = pOffset + pLength;
        }

        @Override
        public boolean hasNext()
        {
            return fNextPos < fLastPos;
        }

        @Override
        public double nextDouble()
        {
            if (fNextPos < fLastPos)
                return fValues[fNextPos++];
            else
                throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(@Nonnull DoubleConsumer pAction)
        {
            while (fNextPos < fLastPos)
                pAction.accept(fValues[fNextPos++]);
        }
    }
}
