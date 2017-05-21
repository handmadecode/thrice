/*
 * Copyright 2016 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation used to document a method as unreachable under normal execution. The canonical example
 * is the private constructor in utility classes (i.e. final classes with only static methods). This
 * constructor is declared to prevent instantiations of such a utility class, and is not meant to be
 * executed.
 *<p>
 * Unreachable code may still be reachable by e.g. changing the access modifier of the method
 * through reflection.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Unreachable
{
    // No body
}
