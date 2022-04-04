/*
 * Copyright 2022 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Unit tests for {@code org.myire.util.Nothing}.
 */
public class NothingTest
{
    @Test
    public void singletonInstanceIsNotNull()
    {
        assertNotNull(Nothing.INSTANCE);
    }
}
