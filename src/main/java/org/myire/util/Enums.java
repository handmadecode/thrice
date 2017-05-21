/*
 * Copyright 2009-2011 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.myire.annotation.Unreachable;


/**
 * Utility methods for enums.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Enums
{
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Enums()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Get the instance of an Enum class that has a specific name. This method is similar to
     * {@code Enum.valueOf()}, but returns null instead of throwing an
     * {@code IllegalArgumentException} if the specified name isn't a name of any instance of the
     * enum type.
     *
     * @param pName         The name of the Enum instance, possibly null.
     * @param pEnumClass    The class object of the enum type from which to return an instance.
     *
     * @param <E>           The enum type.
     *
     * @return  The instance of {@code E} that has the specified name, or null if no instance has
     *          that name. Null is also returned if {@code pName} is null.
     *
     * @throws NullPointerException if {@code pEnumClass} is null.
     */
    @CheckForNull
    static public <E extends Enum<E>> E toEnum(@CheckForNull String pName, @Nonnull Class<E> pEnumClass)
    {
        if (pName == null)
            return null;

        try
        {
            return Enum.valueOf(pEnumClass, pName);
        }
        catch (IllegalArgumentException ignore)
        {
            return null;
        }
    }
}
