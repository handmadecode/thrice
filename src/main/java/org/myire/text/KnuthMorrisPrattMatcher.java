/*
 * Copyright 1997, 2004, 2009, 2016 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.text;

import java.util.function.IntConsumer;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;


/**
 * A character sequence pattern matcher using the Knuth-Morris-Pratt algorithm. This algorithm has
 * the sometimes very useful characteristic that it never needs to look twice at the same char in
 * the character sequence where it tries to find a pattern. This makes it a good choice when
 * matching patterns in stream based char sequences.
 *<p>
 * The idea behind the Knuth-Morris-Pratt algorithm is that when a mismatch is encountered, the
 * chars in the character sequence are known up till the mismatch, since the chars in the pattern
 * match the sequence up till that position. Knowing this, it should be possible to move the pattern
 * ahead to the mismatch position and continue the comparison from there (in other words, start
 * comparing from the beginning of the pattern at the current position in the character sequence).
 *<p>
 * The only case where this approach doesn't work is when the pattern is repetitive. For example, if
 * the pattern is &quot;abcabd&quot; and the character sequence is &quot;abcabcabd&quot;, there will
 * be a mismatch at the second 'c', but if the pattern is moved to that position, the match starting
 * at the second 'a' will not be found.
 *<p>
 * By analyzing the pattern it is possible to determine if it repeats itself so that if a partial
 * match has been found, there is a position in that partial match where the pattern starts again,
 * as in the example above. This is done by remembering for each position in the pattern how many
 * chars towards the beginning of the pattern there is another possible start of the pattern, given
 * that the chars up to but not including the current position are known.
 *<p>
 * Using the example from above, a mismatch will occur at position 5 in the character sequence
 * &quot;abcabcabd&quot; for the pattern &quot;abcabd&quot;. The pattern is repetitive with the
 * chars 'a' and 'b' occurring at positions 0 and 1, and again at positions 3 and 4. Since the
 * mismatch occurs after 5 chars, it can be concluded that the two chars in the char sequence
 * preceding the mismatch are 'a' and 'b', which also are the first two chars in the pattern. This
 * means that the pattern's start should be moved to two chars before the mismatch, and that the
 * comparison should continue at the current position in the char sequence, i.e. where the first
 * mismatch occurred (since the two preceding chars are known to be the start of the pattern).
 *<p>
 * The information used to move the pattern to the correct position after a mismatch is an array of
 * non-negative integers called the <i>next array</i> (or the <i>next vector</i>). This array has
 * the same size as the pattern, and each index holds the position in the pattern where the
 * comparison should continue if a mismatch is found between the corresponding position in the
 * pattern and the current position in the character sequence. Continuing with the example above, if
 * a mismatch is found after five chars (i.e. at position 5 in the pattern), the <i>next array</i>
 * will be consulted at index 5 to get the new position in the pattern that should be compared to
 * the current position in the character sequence (in this case the value will be 2).
 *<p>
 * It can be noted that a non-repetitive pattern has a <i>next array</i> containing only zeroes,
 * i.e. whenever a mismatch occurs, the comparison continues with the pattern's first char.
 *<p>
 * The Knuth-Morris-Pratt algorithm has a typical performance of
 * {@code O(char sequence size + pattern size)}, and a worst case performance of the same
 * order. This can be compared to brute force matching, which has a typical and worst case
 * performance of {@code O(char sequence size * pattern size)}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@Immutable
public class KnuthMorrisPrattMatcher
{
    // The pattern this instance looks for.
    private final char[] fPattern;

    // The next array for this instance's pattern.
    private final int[] fNextArray;


    /**
     * Create a new {@code KnuthMorrisPrattMatcher}.
     *
     * @param pPattern  The pattern this instance should look for. This array will be copied into a
     *                  private pattern array.
     *
     * @throws NullPointerException if {@code pPattern} is null.
     */
    public KnuthMorrisPrattMatcher(@Nonnull char[] pPattern)
    {
        fPattern = new char[pPattern.length];
        System.arraycopy(pPattern, 0, fPattern, 0, pPattern.length);
        fNextArray = createNextArray(fPattern);
    }


    /**
     * Create a new {@code KnuthMorrisPrattMatcher}.
     *
     * @param pPattern  The pattern this instance should look for. No reference to this instance
     *                  will be kept; its individual characters will be copied.
     *
     * @throws NullPointerException if {@code pPattern} is null.
     */
    public KnuthMorrisPrattMatcher(@Nonnull CharSequence pPattern)
    {
        fPattern = new char[pPattern.length()];
        for (int i=0; i<fPattern.length; i++)
            fPattern[i] = pPattern.charAt(i);
        fNextArray = createNextArray(fPattern);
    }


    /**
     * Find all matches of this instance's pattern in a character sequence and pass the start
     * position of each match to an {@code IntConsumer}.
     *
     * @param pCharSequence The character sequence to look for the pattern in.
     * @param pDestination  The destination for the match start positions.
     *
     * @return  The number of found matches.
     *
     * @throws NullPointerException if any of the parameters is null.
     */
    @Nonnegative
    public int findAllMatches(@Nonnull CharSequence pCharSequence, @Nonnull IntConsumer pDestination)
    {
        return findAllMatches(pCharSequence, 0, pCharSequence.length(), pDestination);
    }


    /**
     * Find all matches of this instance's pattern in a subsequence of a char sequence and pass the
     * start position of each match to an {@code IntConsumer}.
     *
     * @param pCharSequence The character sequence to look for the pattern in.
     * @param pFromIndex    The position in {@code pCharSequence} where to start looking for the
     *                      pattern.
     * @param pToIndex      The position in {@code pCharSequence} where to stop looking for the
     *                      pattern. The character at this position will <b>not</b> be included in
     *                      the search.
     * @param pDestination  The destination for the match start positions.
     *
     * @return  The number of found matches.
     *
     * @throws NullPointerException         if {@code pCharSequence} or {@code pDestination} is
     *                                      null.
     * @throws IndexOutOfBoundsException    if {@code pFromIndex} and/or {@code pToIndex-1} are
     *                                      invalid indices in {@code pCharSequence}.
     */
    @Nonnegative
    public int findAllMatches(
        @Nonnull CharSequence pCharSequence,
        @Nonnegative int pFromIndex,
        @Nonnegative int pToIndex,
        @Nonnull IntConsumer pDestination)
    {
        // Trivial case, the pattern cannot be longer than the subsequence.
        if (fPattern.length > (pToIndex - pFromIndex))
            return 0;

        // Loop as long as we find matches and haven't passed the last valid char in the char
        // sequence.
        int aNumMatches = 0;
        int aStartPos = pFromIndex;
        while (aStartPos < pToIndex)
        {
            int aMatchPos = findFirstMatch(pCharSequence, aStartPos, pToIndex);
            if (aMatchPos != -1)
            {
                pDestination.accept(aMatchPos);
                aNumMatches++;
                aStartPos = aMatchPos + 1;
            }
            else
                // No more matches.
                break;
        }

        return aNumMatches;
    }


    /**
     * Find the position of the first match of this instance's pattern in a character sequence.
     *
     * @param pCharSequence The character sequence to look for the pattern in.
     *
     * @return  The position in {@code pCharSequence} where the first occurrence of the pattern
     *          starts, or -1 if no match was found.
     *
     * @throws NullPointerException if {@code pCharSequence} is null.
     */
    @CheckForSigned
    public int findFirstMatch(@Nonnull CharSequence pCharSequence)
    {
        return findFirstMatch(pCharSequence, 0, pCharSequence.length());
    }


    /**
     * Find the position of the first match of this instance's pattern in a subsequence of a
     * character sequence.
     *
     * @param pCharSequence The character sequence to look for the pattern in.
     * @param pFromIndex    The position in {@code pCharSequence} where to start looking for the
     *                      pattern.
     * @param pToIndex      The position in {@code pCharSequence} where to stop looking for the
     *                      pattern. The character at this position will <b>not</b> be included in
     *                      the search.
     *
     * @return  The first position in {@code pCharSequence} at or after {@code pFromIndex} where an
     *          occurrence of the pattern starts, or -1 if no match was found.
     *
     * @throws NullPointerException         if {@code pCharSequence} is null.
     * @throws IndexOutOfBoundsException    if {@code pFromIndex} and/or {@code pToIndex-1} are
     *                                      invalid indices in {@code pCharSequence}.
     */
    @CheckForSigned
    public int findFirstMatch(
        @Nonnull CharSequence pCharSequence,
        @Nonnegative int pFromIndex,
        @Nonnegative int pToIndex)
    {
        // Trivial case, the pattern cannot be longer than the subsequence.
        if (fPattern.length > (pToIndex - pFromIndex))
            return -1;

        // Loop until we reach the end of the subsequence, in which case we can't compare any more,
        // or until we reach the end of the pattern, in which case we have found a match.
        int aSequencePos = pFromIndex;
        int aPatternPos = 0;
        while (aSequencePos < pToIndex && aPatternPos < fPattern.length)
        {
            // Check if the subsequence and pattern match each other at their current positions.
            if (pCharSequence.charAt(aSequencePos) == fPattern[aPatternPos])
            {
                // We have a match this far, compare the next char in the subsequence with the next
                // char in the pattern.
                aSequencePos++;
                aPatternPos++;
            }
            else
            {
                // We have a match up to here, but not at this position.
                if (aPatternPos == 0)
                    // The mismatch occurred at the pattern's first char, continue comparing the
                    // next char in the subsequence with the start of the pattern (since the
                    // pattern's current char is the first one we only need to advance the char
                    // sequence.).
                    aSequencePos++;
                else
                    // Mismatch after a partial match. Let the subsequence's current position be
                    // unchanged and compare it to the position in the pattern indicated by the
                    // next array.
                    aPatternPos = fNextArray[aPatternPos];
            }
        }

        // If we reached the end of the pattern, we found a match.
        if (aPatternPos >= fPattern.length)
            // The sequence is positioned at the end of the pattern, the match starts at the
            // beginning of it.
            return aSequencePos - fPattern.length;
        else
            return -1;
    }


    /**
     * Create the <i>next array</i> (also called the <i>next vector</i>) used by the
     * Knuth-Morris-Pratt pattern matching algorithm.
     *
     * @param pPattern  The characters to create the <i>next array</i> for.
     *
     * @return  A new {@code int} array containing the <i>next array</i> for {@code pPattern}.
     *
     * @throws NullPointerException if {@code pPattern} is null.
     */
    @Nonnull
    static private int[] createNextArray(@Nonnull char[] pPattern)
    {
        // The next array has the same size as the pattern.
        int[] aNextArray = new int[pPattern.length];

        // Calculate the next array elements.
        aNextArray[0] = -1;
        int i = 0;
        int j = -1;
        do
        {
            if (j == -1 || pPattern[i] == pPattern[j])
            {
                i++;
                j++;
                if (i < pPattern.length)
                    aNextArray[i] = j;
            }
            else
                j = aNextArray[j];
        }
        while (i < pPattern.length);

        // The first element has a value of 0 per definition.
        aNextArray[0] = 0;

        return aNextArray;
    }
}
