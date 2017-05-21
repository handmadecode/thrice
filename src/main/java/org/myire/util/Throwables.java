/*
 * Copyright 2015, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.myire.annotation.Unreachable;


/**
 * Utility methods for throwables.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public final class Throwables
{
    /**
     * Private constructor to disallow instantiations of utility method class.
     */
    @Unreachable
    private Throwables()
    {
        // Empty default ctor, defined to override access scope.
    }


    /**
     * Set the cause of a {@code Throwable}.
     *<p>
     * This method is mainly used to throw wrapper exceptions for which the cause cannot be set in
     * the constructor, e.g.
     *<pre>
     *  catch (ArrayIndexOutOfBoundsException e)
     *  {
     *    throw Throwables.withCause(new NoSuchElementException(), e);
     *  }
     *</pre>
     *
     * @param pThrowable    The throwable to set the cause for.
     * @param pCause        The cause.
     * @param <T>           The type of {@code pThrowable}.
     *
     * @return  {@code pThrowable}.
     *
     * @throws NullPointerException if {@code pThrowable} is null.
     */
    @Nonnull
    static public <T extends Throwable> T withCause(@Nonnull T pThrowable, @Nullable Throwable pCause)
    {
        pThrowable.initCause(pCause);
        return pThrowable;
    }


    /**
     * Check if a {@code Throwable} is thrown from a specific method. The throwing method is the
     * one referred to by the first stack trace element of the {@code Throwable}.
     *
     * @param pThrowable    The instance to examine the throwing method of.
     * @param pClassName    The class name of the throwing method to check for, or null to only
     *                      check the method name.
     * @param pMethodName   The method name of the throwing method to check for, or null to only
     *                      check the class name.
     *
     * @return  True if the first stack trace element of the specified {@code Throwable} has the
     *          specified class and method names, false if not. False is also returned if the
     *          {@code Throwable} has an empty stack trace.
     */
    static public boolean isThrownFrom(
        @Nonnull Throwable pThrowable,
        @Nullable String pClassName,
        @Nullable String pMethodName)
    {
        StackTraceElement[] aStackTrace = pThrowable.getStackTrace();
        return aStackTrace.length != 0 && hasClassAndMethod(aStackTrace[0], pClassName, pMethodName);
    }


    /**
     * Check if a {@code Throwable} has a specific method as its call root. The call root method is
     * is the one referred to by the last stack trace element of the {@code Throwable}.
     *
     * @param pThrowable    The instance to examine the call root method of.
     * @param pClassName    The class name of the call root method to check for, or null to only
     *                      check the method name.
     * @param pMethodName   The method name of the call root method to check for, or null to only
     *                      check the class name.
     *
     * @return  True if the last stack trace element of the specified {@code Throwable} has the
     *          specified class and method names, false if not. False is also returned if the
     *          {@code Throwable} has an empty stack trace.
     */
    static public boolean hasCallRoot(
        @Nonnull Throwable pThrowable,
        @Nullable String pClassName,
        @Nullable String pMethodName)
    {
        StackTraceElement[] aStackTrace = pThrowable.getStackTrace();
        return aStackTrace.length != 0 && hasClassAndMethod(aStackTrace[aStackTrace.length - 1], pClassName, pMethodName);
    }


    /**
     * Check if a {@code StackTraceElement} class and method names match the specified values.
     *
     * @param pElement      The instance to examine the class and method names of.
     * @param pClassName    The class name to check for, or null to only check the method name.
     * @param pMethodName   The method name to check for, or null to only check the class name.
     *
     * @return  True if the {@code StackTraceElement} has the specified class and method names,
     *          false if not.
     *
     * @throws NullPointerException if {@code pElement} is null.
     */
    static private boolean hasClassAndMethod(
        @Nonnull StackTraceElement pElement,
        @Nullable String pClassName,
        @Nullable String pMethodName)
    {
        return (pClassName == null || pClassName.equals(pElement.getClassName()))
                &&
               (pMethodName == null || pMethodName.equals(pElement.getMethodName()));
    }
}
