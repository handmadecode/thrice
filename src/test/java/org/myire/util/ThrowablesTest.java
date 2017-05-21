/*
 * Copyright 2015, 2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.myire.util.Throwables.isThrownFrom;
import static org.myire.util.Throwables.hasCallRoot;


/**
 * JUnit tests for the {@link org.myire.util.Throwables} class.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class ThrowablesTest
{
    /**
     * The {@code withCause()} method should throw a {@code NullPointerException} if its first
     * argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void withCauseThrowsForNullThrowable()
    {
        // When
        Throwables.withCause(null, null);
    }


    /**
     * The {@code withCause()} method should return the {@code Throwable} passed as first argument.
     */
    @Test
    public void withCauseSetsReturnsThrowable()
    {
        // Given
        Throwable aThrowable = new RuntimeException();

        // When
        assertSame(aThrowable, Throwables.withCause(aThrowable, null));
    }


    /**
     * The {@code withCause()} method should set the cause of the {@code Throwable} passed as first
     * argument to the {@code Throwable} passed as second argument.
     */
    @Test
    public void withCauseSetsCause()
    {
        // Given
        Exception aCause = new Exception();

        // When
        assertSame(aCause, Throwables.withCause(new RuntimeException(), aCause).getCause());
    }


    /**
     * The {@code withCause()} method should set the cause of the {@code Throwable} passed as first
     * argument to null if the second argument is null.
     */
    @Test
    public void withCauseSetsNullCause()
    {
        // When
        assertNull(Throwables.withCause(new RuntimeException(), null).getCause());
    }


    /**
     * The {@code isThrownFrom()} method should return false for a {@code Throwable} with an empty
     * stack trace.
     */
    @Test
    public void isThrownFromReturnsFalseForEmptyStackTrace()
    {
        // Given
        Throwable aEmpty = forStackTrace();

        // Then
        assertFalse(isThrownFrom(aEmpty,null, null));
    }


    /**
     * The {@code isThrownFrom()} method should return true when both class and method name is
     * {@code null} and the stack trace isn't empty.
     */
    @Test
    public void isThrownFromReturnsTrueForNullNames()
    {
        // Given
        Throwable aNotEmpty = forStackTrace(stackTraceWithDepth(3));

        // Then
        assertTrue(isThrownFrom(aNotEmpty,null, null));
    }


    /**
     * The {@code isThrownFrom()} method should return true when passed the correct class name and
     * a null method name.
     */
    @Test
    public void isThrownFromReturnsTrueForCorrectClassName()
    {
        // Given
        String aThrowingClass = "ThrowingClass";
        StackTraceElement[] aStackTrace = stackTraceWithTopClass(aThrowingClass, 6);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertTrue(isThrownFrom(aThrowable, aThrowingClass, null));
    }


    /**
     * The {@code isThrownFrom()} method should return true when passed the correct method name and
     * a null class name.
     */
    @Test
    public void isThrownFromReturnsTrueForCorrectMethodName()
    {
        // Given
        String aThrowingMethod = "throwingMethod";
        StackTraceElement[] aStackTrace = stackTraceWithTopMethod(aThrowingMethod, 7);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertTrue(isThrownFrom(aThrowable, null, aThrowingMethod));
    }


    /**
     * The {@code isThrownFrom()} method should return true when passed the correct class and
     * method name.
     */
    @Test
    public void isThrownFromReturnsTrueForCorrectClassAndMethodName()
    {
        // Given
        String aThrowingClass = "ThrowingClass";
        String aThrowingMethod = "throwingMethod";
        StackTraceElement[] aStackTrace = stackTraceWithTopElement(aThrowingClass, aThrowingMethod, 5);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertTrue(isThrownFrom(aThrowable, aThrowingClass, aThrowingMethod));
    }


    /**
     * The {@code isThrownFrom()} method should return false when passed the wrong class name and a
     * null method name.
     */
    @Test
    public void isThrownFromReturnsFalseForWrongClassName()
    {
        // Given
        String aThrowingClass = "ThrowingClass";
        String aNonThrowingClass = aThrowingClass + "X";
        StackTraceElement[] aStackTrace = stackTraceWithTopClass(aThrowingClass, 4);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(isThrownFrom(aThrowable, aNonThrowingClass, null));
    }


    /**
     * The {@code isThrownFrom()} method should return false when passed the wrong class name and
     * the correct method name.
     */
    @Test
    public void isThrownFromReturnsFalseForWrongClassNameAndCorrectMethodName()
    {
        // Given
        String aThrowingClass = "ThrowingClass";
        String aNonThrowingClass = aThrowingClass + "X";
        String aThrowingMethod = "throwingMethod";
        StackTraceElement[] aStackTrace = stackTraceWithTopElement(aThrowingClass, aThrowingMethod, 8);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(isThrownFrom(aThrowable, aNonThrowingClass, aThrowingMethod));
    }


    /**
     * The {@code isThrownFrom()} method should return false when passed the wrong method name and a
     * null class name.
     */
    @Test
    public void isThrownFromReturnsFalseForWrongMethodName()
    {
        // Given
        String aThrowingMethod = "throwingMethod";
        String aNonThrowingMethod = aThrowingMethod + "X";
        StackTraceElement[] aStackTrace = stackTraceWithTopMethod(aThrowingMethod, 4);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(isThrownFrom(aThrowable, null, aNonThrowingMethod));
    }


    /**
     * The {@code isThrownFrom()} method should return false when passed the wrong method name and
     * the correct class name.
     */
    @Test
    public void isThrownFromReturnsFalseForWrongMethodNameAndCorrectClassName()
    {
        // Given
        String aThrowingClass = "ThrowingClass";
        String aThrowingMethod = "throwingMethod";
        String aNonThrowingMethod = aThrowingMethod + "X";
        StackTraceElement[] aStackTrace = stackTraceWithTopElement(aThrowingClass, aThrowingMethod, 8);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(isThrownFrom(aThrowable, aThrowingClass, aNonThrowingMethod));
    }


    /**
     * The {@code isThrownFrom()} method should return false when passed the wrong class name and
     * the wrong method name.
     */
    @Test
    public void isThrownFromReturnsFalseForWrongClassAndMethodName()
    {
        // Given
        String aThrowingClass = "ThrowingClass";
        String aThrowingMethod = "throwingMethod";
        String aNonThrowingClass = aThrowingClass + "X";
        String aNonThrowingMethod = aThrowingMethod + "X";
        StackTraceElement[] aStackTrace = stackTraceWithTopElement(aThrowingClass, aThrowingMethod, 8);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(isThrownFrom(aThrowable, aNonThrowingClass, aNonThrowingMethod));
    }


    /**
     * The {@code hasCallRoot()} method should return false for a {@code Throwable} with an empty
     * stack trace.
     */
    @Test
    public void hasCallRootReturnsFalseForEmptyStackTrace()
    {
        // Given
        Throwable aEmpty = forStackTrace();

        // Then
        assertFalse(hasCallRoot(aEmpty,null, null));
    }


    /**
     * The {@code hasCallRoot()} method should return true when both class and method name is
     * {@code null} and the stack trace isn't empty.
     */
    @Test
    public void hasCallRootReturnsTrueForNullNames()
    {
        // Given
        Throwable aNotEmpty = forStackTrace(stackTraceWithDepth(7));

        // Then
        assertTrue(hasCallRoot(aNotEmpty,null, null));
    }


    /**
     * The {@code hasCallRoot()} method should return true when passed the correct class name and
     * a null method name.
     */
    @Test
    public void hasCallRootReturnsTrueForCorrectClassName()
    {
        // Given
        String aRootClass = "RootClass";
        StackTraceElement[] aStackTrace = stackTraceWithRootClass(aRootClass, 9);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertTrue(hasCallRoot(aThrowable, aRootClass, null));
    }


    /**
     * The {@code hasCallRoot()} method should return true when passed the correct method name and
     * a null class name.
     */
    @Test
    public void hasCallRootReturnsTrueForCorrectMethodName()
    {
        // Given
        String aRootMethod = "rootMethod";
        StackTraceElement[] aStackTrace = stackTraceWithRootMethod(aRootMethod, 18);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertTrue(hasCallRoot(aThrowable, null, aRootMethod));
    }


    /**
     * The {@code hasCallRoot()} method should return true when passed the correct class and
     * method name.
     */
    @Test
    public void hasCallRootReturnsTrueForCorrectClassAndMethodName()
    {
        // Given
        String aRootClass = "RootClass";
        String aRootMethod = "rootMethod";
        StackTraceElement[] aStackTrace = stackTraceWithRootElement(aRootClass, aRootMethod, 11);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertTrue(hasCallRoot(aThrowable, aRootClass, aRootMethod));
    }


    /**
     * The {@code hasCallRoot()} method should return false when passed the wrong class name and a
     * null method name.
     */
    @Test
    public void hasCallRootReturnsFalseForWrongClassName()
    {
        // Given
        String aRootClass = "RootClass";
        String aNonRootClass = aRootClass + "X";
        StackTraceElement[] aStackTrace = stackTraceWithRootClass(aRootClass, 3);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(hasCallRoot(aThrowable, aNonRootClass, null));
    }


    /**
     * The {@code hasCallRoot()} method should return false when passed the wrong class name and the
     * correct method name.
     */
    @Test
    public void hasCallRootReturnsFalseForWrongClassNameAndCorrectMethodName()
    {
        // Given
        String aRootClass = "RootClass";
        String aNonRootClass = aRootClass + "X";
        String aRootMethod = "rootMethod";
        StackTraceElement[] aStackTrace = stackTraceWithRootElement(aRootClass, aRootMethod, 10);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(hasCallRoot(aThrowable, aNonRootClass, aRootMethod));
    }


    /**
     * The {@code hasCallRoot()} method should return false when passed the wrong method name and a
     * null class name.
     */
    @Test
    public void hasCallRootReturnsFalseForWrongMethodName()
    {
        // Given
        String aRootMethod = "rootMethod";
        String aNonRootMethod = aRootMethod + "X";
        StackTraceElement[] aStackTrace = stackTraceWithRootMethod(aRootMethod, 6);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(hasCallRoot(aThrowable, null, aNonRootMethod));
    }


    /**
     * The {@code hasCallRoot()} method should return false when passed the wrong method name and
     * the correct class name.
     */
    @Test
    public void hasCallRootReturnsFalseForWrongMethodNameAndCorrectClassName()
    {
        // Given
        String aRootClass = "RootClass";
        String aRootMethod = "rootMethod";
        String aNonRootMethod = aRootMethod + "X";
        StackTraceElement[] aStackTrace = stackTraceWithRootElement(aRootClass, aRootMethod, 23);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(hasCallRoot(aThrowable, aRootClass, aNonRootMethod));
    }


    /**
     * The {@code hasCallRoot()} method should return false when passed the wrong class name and
     * the wrong method name.
     */
    @Test
    public void hasCallRootReturnsFalseForWrongClassAndMethodName()
    {
        // Given
        String aRootClass = "RootClass";
        String aRootMethod = "rootMethod";
        String aNonRootClass = aRootClass + "X";
        String aNonRootMethod = aRootMethod + "X";
        StackTraceElement[] aStackTrace = stackTraceWithRootElement(aRootClass, aRootMethod, 12);
        Throwable aThrowable = forStackTrace(aStackTrace);

        // Then
        assertFalse(hasCallRoot(aThrowable, aNonRootClass, aNonRootMethod));
    }


    /**
     * Create a {@code Throwable} that will return a specific stack trace when its
     * {@code getStackTrace()} method is called.
     *
     * @param pStackTrace   The stack trace.
     *
     * @return  A new {@code Throwable}.
     */
    static private Throwable forStackTrace(StackTraceElement... pStackTrace)
    {
        Throwable aThrowable = mock(Throwable.class);
        when(aThrowable.getStackTrace()).thenReturn(pStackTrace);
        return aThrowable;
    }


    /**
     * Create a stack trace of a specific depth.
     *
     * @param pDepth    The depth of the stack trace.
     *
     * @return  An array of {@code StackTraceElement} instances with unspecified properties.
     */
    static private StackTraceElement[] stackTraceWithDepth(int pDepth)
    {
        return stackTraceWithTopElement("C0", "m0", pDepth);
    }


    /**
     * Create a stack trace where the top element has a specific class name and method name.
     *
     * @param pClass    The name of the top element's class.
     * @param pMethod   The name of the top element's method.
     * @param pDepth    The depth of the stack trace.
     *
     * @return  An array of {@code StackTraceElement} instances where element 0 has the specified
     *          class name and method name.
     */
    static private StackTraceElement[] stackTraceWithTopElement(String pClass, String pMethod, int pDepth)
    {
        StackTraceElement[] aStackTrace = new StackTraceElement[pDepth];
        aStackTrace[0] = new StackTraceElement(pClass, pMethod, null, 0);
        for (int i=1; i<pDepth; i++)
            aStackTrace[i] = new StackTraceElement("C" + i, "m" + i, null, 0);

        return aStackTrace;
    }


    /**
     * Create a stack trace where the top element has a specific class name.
     *
     * @param pClass    The name of the top element's class.
     * @param pDepth    The depth of the stack trace.
     *
     * @return  An array of {@code StackTraceElement} instances where element 0 has the specified
     *          class name.
     */
    static private StackTraceElement[] stackTraceWithTopClass(String pClass, int pDepth)
    {
        return stackTraceWithTopElement(pClass, "m0", pDepth);
    }


    /**
     * Create a stack trace where the top element has a specific method name.
     *
     * @param pMethod   The name of the top element's method.
     * @param pDepth    The depth of the stack trace.
     *
     * @return  An array of {@code StackTraceElement} instances where element 0 has the specified
     *          method name.
     */
    static private StackTraceElement[] stackTraceWithTopMethod(String pMethod, int pDepth)
    {
        return stackTraceWithTopElement("C0", pMethod, pDepth);
    }


    /**
     * Create a stack trace where the root element has a specific class name and method name.
     *
     * @param pClass    The name of the root element's class.
     * @param pMethod   The name of the root element's method.
     * @param pDepth    The depth of the stack trace.
     *
     * @return  An array of {@code StackTraceElement} instances where the last element has the
     *          specified class name and method name.
     */
    static private StackTraceElement[] stackTraceWithRootElement(String pClass, String pMethod, int pDepth)
    {
        StackTraceElement[] aStackTrace = new StackTraceElement[pDepth];
        aStackTrace[pDepth-1] = new StackTraceElement(pClass, pMethod, null, 0);
        for (int i=0; i<pDepth-1; i++)
            aStackTrace[i] = new StackTraceElement("C" + i, "m" + i, null, 0);

        return aStackTrace;
    }


    /**
     * Create a stack trace where the root element has a specific class name.
     *
     * @param pClass    The name of the root element's class.
     * @param pDepth    The depth of the stack trace.
     *
     * @return  An array of {@code StackTraceElement} instances where the last element has the
     *          specified class name.
     */
    static private StackTraceElement[] stackTraceWithRootClass(String pClass, int pDepth)
    {
        return stackTraceWithRootElement(pClass, "m0", pDepth);
    }


    /**
     * Create a stack trace where the root element has a specific method name.
     *
     * @param pMethod   The name of the root element's method.
     * @param pDepth    The depth of the stack trace.
     *
     * @return  An array of {@code StackTraceElement} instances where the last element has the
     *          specified method name.
     */
    static private StackTraceElement[] stackTraceWithRootMethod(String pMethod, int pDepth)
    {
        return stackTraceWithRootElement("C0", pMethod, pDepth);
    }
}
