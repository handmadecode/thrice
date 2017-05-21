/*
 * Copyright 2004-2009 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static org.myire.util.Ascii.isAsciiDigit;


/**
 * A {@code ParamText} is used to construct strings from a parametrized template. An instance of
 * {@code ParamText} operates on a template string with a number of parameters. New strings are
 * created from this template by replacing the parameters with actual {@code CharSequence} values.
 *<p>
 * The parameters use a simple syntax. Each parameter has an ID which corresponds to the index in
 * the parameter value array provided in calls to {@link #format(CharSequence[])}. The parameter ID
 * is prefixed with a caret character ('^'), e.g. &quot;This is parameter 1: ^1&quot;. In other
 * words, a caret followed by one or more digits (0-9) is interpreted as a  parameter in the
 * template string. A caret that is followed by non-digits will be treated as any ordinary character
 * in the template, as will any digit sequence that is not immediately preceded by a caret.
 *<p>
 * If you really must have a caret followed by a digit sequence in the template string that should
 * not be interpreted as a parameter, put another caret before the one preceding the digits, e.g.
 * &quot;This is not a parameter: ^^7&quot;.
 *<p>
 * The parameter IDs need not to appear in numeric ascending order in the template string, and it is
 * legal to have several parameters with the same ID. As an example, the following template string
 * is legal: &quot;To ^1 or not to ^1, that is the ^0&quot;, and calling
 * {@code format("question","be")} would produce the first line of a well-known monologue.
 *<p>
 * Note that no formatting can be specified for a parameter. All formatting of e.g. numbers must be
 * done by the caller before invoking {@link #format(CharSequence[])}.
 *<p>
 * This class is immutable, and instances of its can safely be shared by multiple threads.
 *<p>
 * Seasoned Macintosh Toolbox programmers will most likely have used a function with characteristics
 * similar to the ones of this class.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@Immutable
public class ParamText
{
    static private final char[] EMPTY_CHAR_ARRAY = {};
    static private final int[] EMPTY_INT_ARRAY = {};


    // The individual characters of the template with the parameters removed, and the logical size
    // of this array.
    private final char[] fTemplateChars;
    private final int fNumChars;

    // The ID and position of each parameter in the template. The array is (at least) twice as long
    // as the number of parameters in the template. The first parameter's ID is at element 0, the
    // first parameter's position in the template is at element 1, and so on.
    private final int[] fParameterIDsAndPositions;
    private final int fNumParameters;


    /**
     * Create a new {@code ParamText}.
     *
     * @param pTemplate The template that the new {@code ParamText} should create strings from. No
     *                  reference will be kept to this instance; its contents will be copied.
     *
     * @throws NullPointerException if {@code pTemplate} is null.
     */
    public ParamText(@Nonnull CharSequence pTemplate)
    {
        int aLength = pTemplate.length();
        if (aLength < 2)
        {
            // A template of size 0 or 1 cannot have any parameters; at least a caret and a digit
            // is required.
            fTemplateChars = aLength == 0 ? EMPTY_CHAR_ARRAY : new char[]{pTemplate.charAt(0)};
            fParameterIDsAndPositions = EMPTY_INT_ARRAY;
            fNumChars = aLength;
            fNumParameters = 0;
        }
        else
        {
            // Create arrays with the maximum lengths given the template size. The maximum number
            // of non-parameter chars is when there are no parameters, i.e. the entire template
            // size. A parameter occupies at least two chars, a caret and one or more digits, so
            // the maximum number of parameters is half the template size, but the array holds two
            // values for each parameter.
            fTemplateChars = new char[aLength];
            fParameterIDsAndPositions = new int[aLength];

            // Parse the template and set the logical lengths of the two arrays.
            long aNumParametersAndChars = parseTemplate(pTemplate, fTemplateChars, fParameterIDsAndPositions);
            fNumChars = (int) (aNumParametersAndChars >>> 32);
            fNumParameters = (int) aNumParametersAndChars;
        }
    }


    /**
     * Format a string by replacing the parameters in the template with the supplied values.
     *
     * @param pValues   The values to replace the parameters with. The value at index 0 will replace
     *                  all occurrences of parameter ID 0 in the template, the value at index 1 will
     *                  replace all occurrences of parameter ID 1, and so on. If the template
     *                  contains parameters with IDs larger than the highest index in the array,
     *                  those parameters will be replaced with an empty string. If the template
     *                  doesn't contain a parameter with an ID corresponding to an index in the
     *                  supplied array, that value will be ignored. If a value is null, the string
     *                  &quot;null&quot; will be used as value for the corresponding parameter.
     *
     * @return  A new string with the parameters replaced with the values in {@code pValues}. If
     *          {@code pValues} is null, the template string with all parameters set to to the empty
     *          string is returned.
     */
    @Nonnull
    public String format(@Nullable CharSequence... pValues)
    {
        // If no parameters were specified we return the template with all parameters removed.
        if (pValues == null)
            return new String(fTemplateChars, 0, fNumChars);

        // Estimate the size of the formatted string.
        int aFormattedLength = fNumChars;
        for (CharSequence aValue : pValues)
            if (aValue != null)
                aFormattedLength += aValue.length();

        StringBuilder aResult = new StringBuilder(aFormattedLength);
        int aPrevPos = 0;
        for (int i=0; i<fNumParameters; i++)
        {
            // Two values for each parameter -> the parameter's ID is at index i*2.
            int aIndex = i << 1;
            int aID = fParameterIDsAndPositions[aIndex];
            int aPos = fParameterIDsAndPositions[aIndex + 1];

            // Append the template characters between this parameter and the previous.
            aResult.append(fTemplateChars, aPrevPos, aPos - aPrevPos);
            aPrevPos = aPos;

            // Append the parameter value at the element indicated by the parameters ID.
            if (aID < pValues.length)
                aResult.append(pValues[aID]);
        }

        // Append the template characters after the last parameter.
        if (aPrevPos < fNumChars)
            aResult.append(fTemplateChars, aPrevPos, fNumChars - aPrevPos);

        return aResult.toString();
    }


    /**
     * Format a string by replacing all occurrences of parameter ID 0 in the template with the
     * supplied value. This is equivalent to calling {@link #format(CharSequence[])} with an array
     * containing {@code pValue} as its only element.
     *
     * @param pValue    The value to replace all occurrences of parameter ID 0 with. If this value
     *                  is null, the string &quot;null&quot; will be used as value for that
     *                  parameter. Passing an empty string will return the template string with all
     *                  parameters set to to the empty string.
     *
     * @return  A new string with the occurrences of parameter ID 0 replaced with {@code pValue}.
     */
    @Nonnull
    public String format(@Nullable CharSequence pValue)
    {
        // Estimate the size of the formatted string.
        int aFormattedLength = fNumChars;
        if (pValue != null)
            aFormattedLength += pValue.length();

        StringBuilder aResult = new StringBuilder(aFormattedLength);
        int aPrevPos = 0;
        for (int i=0; i<fNumParameters; i++)
        {
            // Two values for each parameter -> the parameter's ID is at index i*2.
            int aIndex = i << 1;
            int aID = fParameterIDsAndPositions[aIndex];
            int aPos = fParameterIDsAndPositions[aIndex + 1];

            // Append the template characters between this parameter and the previous.
            aResult.append(fTemplateChars, aPrevPos, aPos - aPrevPos);
            aPrevPos = aPos;

            // Append the parameter value at the element indicated by the parameters ID.
            if (aID == 0)
                aResult.append(pValue);
        }

        // Append the template characters after the last parameter.
        if (aPrevPos < fNumChars)
            aResult.append(fTemplateChars, aPrevPos, fNumChars - aPrevPos);

        return aResult.toString();
    }


    /**
     * Parse a template and extract the parameters and the characters not belonging to the
     * parameters.
     *
     * @param pTemplate                 The template to parse.
     * @param pTemplateChars            An array to copy the template characters into.
     * @param pParameterIDsAndPositions An array to set the parameter IDs and positions in.
     *
     * @return  A 64-bit value with the number of non-parameter characters in the high 32 bits and
     *          the number of parameters in the low 32 bits.
     */
    static private long parseTemplate(
        @Nonnull CharSequence pTemplate,
        @Nonnull char[] pTemplateChars,
        @Nonnull int[] pParameterIDsAndPositions)
    {
        int aNumParameters = 0;
        int aNumChars = 0;
        int aPos = 0;
        int aLength = pTemplate.length();
        while (aPos < aLength)
        {
            char aChar = pTemplate.charAt(aPos);
            if (aChar == '^' && aPos < aLength - 1)
            {
                // A caret not at the end of the template, check the character following it.
                aChar = pTemplate.charAt(++aPos);
                if (isAsciiDigit(aChar))
                {
                    // A caret followed by at least one digit, this is a parameter. Parse the
                    // digit(s) into the parameter ID.
                    int aID = aChar - '0';
                    while (++aPos < aLength && isAsciiDigit(aChar = pTemplate.charAt(aPos)))
                        aID = aID * 10 +  aChar - '0';

                    pParameterIDsAndPositions[aNumParameters*2] = aID;
                    pParameterIDsAndPositions[aNumParameters*2 + 1] = aNumChars;
                    aNumParameters++;
                }
                else if (aChar == '^')
                {
                    // A caret followed by a caret, the first is an escape marker and is ignored,
                    // the second one is part of the template text.
                    pTemplateChars[aNumChars++] = aChar;
                    aPos++;
                }
                else
                {
                    // A caret followed by something other than a digit or a caret. Both the caret
                    // and the something else count as a template character.
                    pTemplateChars[aNumChars] = '^';
                    pTemplateChars[aNumChars+1] = aChar;
                    aNumChars += 2;
                    aPos++;
                }
            }
            else
            {
                // Anything but a caret (or a caret at the end of the template), this character is
                // not part of a parameter.
                pTemplateChars[aNumChars++] = aChar;
                aPos++;
            }
        }

        return ((long) aNumChars) << 32 | aNumParameters;
    }
}
