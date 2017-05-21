/*
 * Copyright 2007-2009, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.Objects;

import javax.annotation.Nullable;


/**
 * An association between two objects.
 *
 * @param <X> The type of the first object.
 * @param <Y> The type of the second object.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class Pair<X, Y>
{
    // The pair members.
    private final X fFirst;
    private final Y fSecond;


    /**
     * Create a new {@code Pair}.
     *
     * @param pFirst    The first member of the pair.
     * @param pSecond   The second member of the pair.
     */
    public Pair(@Nullable X pFirst, @Nullable Y pSecond)
    {
        fFirst = pFirst;
        fSecond = pSecond;
    }


    /**
     * Get the first member of the pair.
     *
     * @return  The first member of the pair.
     */
    @Nullable
    public X getFirst()
    {
        return fFirst;
    }


    /**
     * Get the second member of the pair.
     *
     * @return  The second member of the pair.
     */
    @Nullable
    public Y getSecond()
    {
        return fSecond;
    }


    /**
     * Check if this {@code Pair} is equal to another object. A {@code Pair} is equal to another
     * object if the other object is a {@code Pair}, its first member is equal to this instance's
     * first member, and its second member is equal to this instance's second member.
     *
     * @param pObject   The object to compare this {@code Pair} to.
     *
     * @return  True if {@code pObject} is equal to this object, false if not.
     */
    @Override
    public boolean equals(Object pObject)
    {
        // Standard check for object identity and correct class, note that instanceof will return
        // false if pObject is null.
        if (pObject == this)
            return true;
        if (!(pObject instanceof Pair<?, ?>))
            return false;

        // Cast pObject to a Pair, which is safe at this point, and compare the pair members.
        Pair<?, ?> aOther = (Pair<?, ?>) pObject;
        return Objects.equals(fFirst, aOther.fFirst) && Objects.equals(fSecond, aOther.fSecond);
    }


    /**
     * Get the hash code for this {@code Pair}.
     *
     * @return The hash code of this {@code Pair}.
     */
    @Override
    public int hashCode()
    {
        HashComposer aComposer = MultiplyAddHashComposer.FACTOR31;
        int aHashCode = aComposer.initialValue();
        aHashCode = MultiplyAddHashComposer.FACTOR31.update(aHashCode, fFirst);
        return MultiplyAddHashComposer.FACTOR31.update(aHashCode, fSecond);
    }


    /**
     * Get the string representation of this {@code Pair}.
     *
     * @return  A string on the format &quot;&lt;first&gt;-&lt;second&gt;&quot;, where &lt;first&gt;
     *          is the result of calling {@code toString()} on the first member, and &lt;second&gt;
     *          is the result of calling {@code toString()} on the second member.
     */
    @Override
    public String toString()
    {
        return String.valueOf(fFirst) + '-' + fSecond;
    }
}
