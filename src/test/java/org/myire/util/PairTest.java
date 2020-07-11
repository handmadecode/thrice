/*
 * Copyright 2007-2008, 2016, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;


/**
 * Unit tests for {@link org.myire.util.Pair}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class PairTest
{
    /**
     * The values passed to the constructor should be returned by the getters.
     */
    @Test
    public void valuesPassedToConstructorAreReturnedByGetters()
    {
        Object aObject1 = new Object();
        Object aObject2 = new Object();
        Pair<Object, Object> aPair = new Pair<>(aObject1, aObject2);
        assertSame(aObject1, aPair.getFirst());
        assertSame(aObject2, aPair.getSecond());

        aPair = new Pair<>(aObject1, null);
        assertSame(aObject1, aPair.getFirst());
        assertNull(aPair.getSecond());

        aPair = new Pair<>(null, aObject2);
        assertNull(aPair.getFirst());
        assertSame(aObject2, aPair.getSecond());

        aPair = new Pair<>(null, null);
        assertNull(aPair.getFirst());
        assertNull(aPair.getSecond());
    }


    /**
     * The {@code equals()} and {@code hashCode()} methods should obey the general contracts of
     * those methods.
     */
    @Test
    public void equalsAndHashCodeObeyContracts()
    {
        EqualsVerifier.forClass(Pair.class).suppress(Warning.STRICT_INHERITANCE).verify();
    }


    /**
     * The {@code toString()} method should return the expected string representation.
     */
    @Test
    public void toStringReturnsTheExpectedValue()
    {
        Pair<String, String> aPair = new Pair<>("a", "b");
        assertEquals("a-b", aPair.toString());

        aPair = new Pair<>("", "");
        assertEquals("-", aPair.toString());

        aPair = new Pair<>("a", null);
        assertEquals("a-null", aPair.toString());

        aPair = new Pair<>(null, "b");
        assertEquals("null-b", aPair.toString());

        aPair = new Pair<>(null, null);
        assertEquals("null-null", aPair.toString());
    }
}
