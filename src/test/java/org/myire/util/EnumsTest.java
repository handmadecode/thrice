/*
 * Copyright 2010-2011 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.myire.util.Enums.toEnum;


/**
 * Unit tests for {@link org.myire.util.Enums}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class EnumsTest
{
    /**
     * The {@code toEnum()} method should return null when passed a null name.
     */
    @Test
    public void toEnumReturnsNullForNullName()
    {
        assertNull(toEnum(null, TestEnum.class));
    }


    /**
     * The {@code toEnum()} method should throw a {@code NullPointerException } when passed a null
     * enum class.
     */
    @Test(expected=NullPointerException.class)
    public void toEnumThrowsforNullEnumClass()
    {
        toEnum("", null);
    }


    /**
     * The {@code toEnum()} method should return the expected enum instance when passed the
     * corresponding name.
     */
    @Test
    public void toEnumReturnsCorrectInstanceForItsName()
    {
        for (TestEnum aEnum : TestEnum.values())
            assertEquals(aEnum, toEnum(aEnum.name(), TestEnum.class));
    }


    /**
     * The {@code toEnum()} method should return null when passed a name that is not the name of an
     * instance of the enum class.
     */
    @Test
    public void toEnumReturnsNullForInvalidName()
    {
        assertNull(toEnum("IDUYFDEWHDWE", TestEnum.class));
    }


    /**
     * Enumeration for testing {@code Enums.toEnum()}.
     */
    private enum TestEnum {ALPHA, BETA, GAMMA, OMEGA}
}
