/*
 * Copyright 2004, 2008-2009, 2016, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.text;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit tests for {@link org.myire.text.ParamText}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class ParamTextTest
{
    /**
     * Creating a {@code ParamText} with a null template should throw a
     * {@code NullPointerException}.
     */
    @Test
    @SuppressWarnings("unused")
    public void ctorThrowsForNullTemplate()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                new ParamText(null)
        );
    }


    /**
     * The {@code format} method should produce the same string when invoked multiple times with
     * the same arguments.
     */
    @Test
    public void formatProducesTheSameStringForIdenticalArguments()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^2");
        String[] aParameters = new String[]{"0","1", "2"};

        // When
        String aResult1 = aParamText.format(aParameters);
        String aResult2 = aParamText.format(aParameters);

        // Then
        assertEquals(aResult1, aResult2);
    }


    /**
     * Test a template with a prefix, i.,e. text before the first parameter, and a suffix, i.e. text
     * after the last parameter.
     */
    @Test
    public void testTemplateWithPrefixAndSuffix()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^2!");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=1, 2=2!", aParamText.format("0","1","2")),
            () -> assertEquals("0=0, 1=1, 2=!", aParamText.format("0","1")),
            () -> assertEquals("0=, 1=, 2=!", aParamText.format("")),
            () -> assertEquals("0=, 1=, 2=!", aParamText.format(new String[0]))
        );
    }


    /**
     * Test a template with a prefix but no suffix.
     */
    @Test
    public void testTemplateWithPrefixButNoSuffix()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^2");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=1, 2=2", aParamText.format("0","1","2")),
            () -> assertEquals("0=0, 1=1, 2=", aParamText.format("0","1")),
            () -> assertEquals("0=, 1=, 2=", aParamText.format("")),
            () -> assertEquals("0=, 1=, 2=", aParamText.format(new String[0]))
        );
    }


    /**
     * Test a template with a suffix but no prefix.
     */
    @Test
    public void testTemplateWithSuffixButNoPrefix()
    {
        // Given
        ParamText aParamText = new ParamText("^0, 1=^1, 2=^2, 3=3");

        // Then
        assertAll(
            () -> assertEquals("0, 1=1, 2=2, 3=3", aParamText.format("0","1","2")),
            () -> assertEquals("0, 1=1, 2=, 3=3", aParamText.format("0","1")),
            () -> assertEquals(", 1=, 2=, 3=3", aParamText.format("")),
            () -> assertEquals(", 1=, 2=, 3=3", aParamText.format(new String[0]))
        );
    }


    /**
     * Test a template without both prefix and suffix.
     */
    @Test
    public void testTemplateWithoutPrefixAndSuffix()
    {
        // Given
        ParamText aParamText = new ParamText("^0, 1=^1, 2=^2");

        // Then
        assertAll(
            () -> assertEquals("0, 1=1, 2=2", aParamText.format("0","1","2")),
            () -> assertEquals("0, 1=1, 2=", aParamText.format("0","1")),
            () -> assertEquals(", 1=, 2=", aParamText.format("")),
            () -> assertEquals(", 1=, 2=", aParamText.format(new String[0]))
        );
     }


    /**
     * Test a template with that ends with a caret.
     */
    @Test
    public void testCaretAtEndOfTemplate()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=1, 2=^", aParamText.format("0","1","2")),
            () -> assertEquals("0=0, 1=, 2=^", aParamText.format("0")),
            () -> assertEquals("0=, 1=, 2=^", aParamText.format("")),
            () -> assertEquals("0=, 1=, 2=^", aParamText.format(new String[0])))
        ;
    }


    /**
     * Test a template without a any parameters.
     */
    @Test
    public void testTemplateWithoutParameters()
    {
        // Given
        String aTemplate = "This is plaintext";
        ParamText aParamText = new ParamText(aTemplate);

        // Then
        assertAll(
            () -> assertEquals(aTemplate, aParamText.format("0","1","2")),
            () -> assertEquals(aTemplate, aParamText.format("")),
            () -> assertEquals(aTemplate, aParamText.format(new String[0]))
        );
    }


    /**
     * Test an empty template.
     */
    @Test
    public void testEmptyTemplate()
    {
        // Given
        ParamText aParamText = new ParamText("");

        // Then
        assertAll(
            () -> assertTrue(aParamText.format("0","1","2").isEmpty()),
            () -> assertTrue(aParamText.format("").isEmpty()),
            () -> assertTrue(aParamText.format(new String[0]).isEmpty())
        );
    }


    /**
     * Test a template containing only one character.
     */
    @Test
    public void testSingleCharTemplate()
    {
        // Given
        String aTemplate = "^";
        ParamText aParamText = new ParamText(aTemplate);

        // Then
        assertAll(
            () -> assertEquals(aTemplate, aParamText.format("0","1","2")),
            () -> assertEquals(aTemplate, aParamText.format("")),
            () -> assertEquals(aTemplate, aParamText.format(new String[0]))
        );
    }


    /**
     * Test a template with adjacent parameters.
     */
    @Test
    public void testAdjacentParameters()
    {
        // Given
        ParamText aParamText1 = new ParamText("^0^1, 2=^2");

        // Then
        assertAll(
            () -> assertEquals("01, 2=2", aParamText1.format("0","1","2")),
            () -> assertEquals("01, 2=", aParamText1.format("0","1")),
            () -> assertEquals("0, 2=", aParamText1.format("0")),
            () -> assertEquals(", 2=", aParamText1.format("")),
            () -> assertEquals(", 2=", aParamText1.format(new String[0]))
        );

        // Given
        ParamText aParamText2 = new ParamText("Params:^0^1,^2");

        // Then
        assertAll(
            () -> assertEquals("Params:01,2", aParamText2.format("0","1","2")),
            () -> assertEquals("Params:01,", aParamText2.format("0","1")),
            () -> assertEquals("Params:0,", aParamText2.format("0")),
            () -> assertEquals("Params:,", aParamText2.format("")),
            () -> assertEquals("Params:,", aParamText2.format(new String[0]))
        );
    }


    /**
     * Test a template with nothing but parameters.
     */
    @Test
    public void testParametersOnly()
    {
        // Given
        ParamText aParamText1 = new ParamText("^0^1^2");

        // Then
        assertAll(
            () -> assertEquals("012", aParamText1.format("0","1","2")),
            () -> assertEquals("01", aParamText1.format("0","1")),
            () -> assertEquals("", aParamText1.format("")),
            () -> assertEquals("", aParamText1.format(new String[0]))
        );

        // Given
        ParamText aParamText2 = new ParamText("^2");

        // Then
        assertAll(
            () -> assertEquals("2", aParamText2.format("0","1","2")),
            () -> assertEquals("", aParamText2.format("0","1")),
            () -> assertEquals("", aParamText2.format("")),
            () -> assertEquals("", aParamText2.format(new String[0]))
        );
    }


    /**
     * Test formatting with fewer arguments than there are parameters in the template.
     */
    @Test
    public void testTooFewArguments()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^2");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=1, 2=", aParamText.format("0","1")),
            () -> assertEquals("0=0, 1=, 2=", aParamText.format("0")),
            () -> assertEquals("0=, 1=, 2=", aParamText.format(new String[]{""})),
            () -> assertEquals("0=, 1=, 2=", aParamText.format(new String[0]))
        );
    }


    /**
     * Test formatting with more arguments than there are parameters in the template.
     */
    @Test
    public void testTooManyArguments()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^2");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=1, 2=2", aParamText.format("0","1","2","3")),
            () -> assertEquals("0=0, 1=1, 2=2", aParamText.format("0","1","2","3","4","5"))
        );
    }


    /**
     * Test formatting with null arguments to the {@code format()} method.
     */
    @Test
    public void testNullArguments()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^2");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=null, 2=2", aParamText.format("0", null, "2")),
            () -> assertEquals("0=null, 1=null, 2=", aParamText.format(new String[]{null, null})),
            () -> assertEquals("0=null, 1=, 2=", aParamText.format((String) null)),
            () -> assertEquals("0=, 1=, 2=", aParamText.format((String[]) null))
        );
     }


    /**
     * Test a template with duplicate occurrences of some parameter numbers.
     */
    @Test
    public void testDuplicateParameterNumbers()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 0=^0");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=1, 0=0", aParamText.format("0","1","2")),
            () -> assertEquals("0=0, 1=1, 0=0", aParamText.format("0","1")),
            () -> assertEquals("0=0, 1=, 0=0", aParamText.format(new String[]{"0"})),
            () -> assertEquals("0=, 1=, 0=", aParamText.format(""))
        );
    }


    /**
     * Test a template with parameter numbers that don't appear in ascending numeric order.
     */
    @Test
    public void testUnorderedParameterNumbers()
    {
        // Given
        ParamText aParamText = new ParamText("2=^2, 0=^0, 1=^1");

        // Then
        assertAll(
            () -> assertEquals("2=2, 0=0, 1=1", aParamText.format("0","1","2")),
            () -> assertEquals("2=, 0=0, 1=1", aParamText.format("0","1")),
            () -> assertEquals("2=, 0=, 1=", aParamText.format("")),
            () -> assertEquals("2=, 0=, 1=", aParamText.format(new String[0]))
        );
    }


    /**
     * Test a template with gaps in the parameter numbers.
     */
    @Test
    public void testPlaceHolderNumberGaps()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 2=^2, 3=^3");

        // Then
        assertAll(
            () -> assertEquals("0=0, 2=2, 3=3", aParamText.format("0","1","2", "3")),
            () -> assertEquals("0=0, 2=2, 3=", aParamText.format("0","1","2")),
            () -> assertEquals("0=0, 2=, 3=", aParamText.format("0")),
            () -> assertEquals("0=, 2=, 3=", aParamText.format("")),
            () -> assertEquals("0=, 2=, 3=", aParamText.format(new String[0]))
        );
    }


    /**
     * Test a template with a placeholder numbers that have more than one digit.
     */
    @Test
    public void testMultiDigitParameter()
    {
        // Given
        ParamText aParamText = new ParamText("Msg=^10!");

        // Then
        String[] aParams = new String[11];
        aParams[10] = "Some text";
        assertEquals("Msg=Some text!", aParamText.format(aParams));

        // Given
        aParamText = new ParamText("Msg=^102!");

        // Then
        aParams = new String[110];
        aParams[102] = "Some text";
        assertEquals("Msg=Some text!", aParamText.format(aParams));
    }


    /**
     * Test a template with carets not followed by digits.
     */
    @Test
    public void testCaretWithoutDigit()
    {
        // Given
        ParamText aParamText = new ParamText("0=^0, 1=^1, 2=^ 3, ^x");

        // Then
        assertAll(
            () -> assertEquals("0=0, 1=1, 2=^ 3, ^x",  aParamText.format("0","1","2")),
            () -> assertEquals("0=0, 1=, 2=^ 3, ^x", aParamText.format("0")),
            () -> assertEquals("0=, 1=, 2=^ 3, ^x", aParamText.format("")),
            () -> assertEquals("0=, 1=, 2=^ 3, ^x", aParamText.format(new String[0]))
        );
    }


    /**
     * Test a template with escaped carets.
     */
    @Test
    public void testCaretEscaping()
    {
        // Given
        ParamText aParamText = new ParamText("0=^^0, 1=^1, 2=^2");

        // Then
        assertAll(
            () -> assertEquals("0=^0, 1=1, 2=2", aParamText.format("0","1","2")),
            () -> assertEquals("0=^0, 1=1, 2=", aParamText.format("0","1")),
            () -> assertEquals("0=^0, 1=, 2=", aParamText.format("")),
            () -> assertEquals("0=^0, 1=, 2=", aParamText.format(new String[0]))
        );
    }
}
