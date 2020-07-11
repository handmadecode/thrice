/*
 * Copyright 2004, 2009, 2016, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.text;

import java.util.BitSet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit tests for {@code KnuthMorrisPrattMatcher}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class KnuthMorrisPrattMatcherTest
{
    /**
     * The {@code findFirstMatch} method should return {@code -1} when passed a char sequence that
     * does not contain the pattern.
     */
    @Test
    public void findFirstMatchReturnsMinus1ForNoMatch()
    {
        // Given
        String aCharSequence = "The quick brown fox jumps over the lazy dog";
        String aPattern ="jumps over them";

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence);

        // Then
        assertEquals(-1, aPos);
    }


    /**
     * The {@code findFirstMatch} method should return {@code -1} when passed a char sequence that
     * is shorter than the pattern.
     */
    @Test
    public void findFirstMatchReturnsMinus1ForTooShortCharSequence()
    {
        // Given
        String aPattern = "Find me!";
        String aCharSequence = aPattern.substring(0, aPattern.length() - 1);

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern.toCharArray());
        int aPosition = aMatcher.findFirstMatch(aCharSequence);

        // Then
        assertEquals(-1, aPosition);
    }


    /**
     * The {@code findFirstMatch} method should not find a match in a char sequence when the match
     * occurs before the start index.
     */
    @Test
    public void findFirstMatchIgnoresMatchBeforeStartIndex()
    {
        // Given
        String aCharSequence = "When I had journeyed half of our life's way";
        String aPattern ="I had";
        int aPosAfterMatch = aCharSequence.indexOf(aPattern) + 1;

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence, aPosAfterMatch, aCharSequence.length());

        // Then
        assertEquals(-1, aPos);
    }


    /**
     * The {@code findFirstMatch} method should not find a match in a char sequence when the match
     * occurs after the end index.
     */
    @Test
    public void findFirstMatchIgnoresMatchAfterEndIndex()
    {
        // Given
        String aCharSequence = "It was a bright cold day in April, and the clocks were striking thirteen";
        String aPattern = " April";
        int aLastPosInMatch = aCharSequence.indexOf(aPattern) + aPattern.length() - 1;

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence, 0, aLastPosInMatch);

        // Then
        assertEquals(-1, aPos);
    }


    /**
     * The {@code findFirstMatch} method should find a match in a char sequence when the pattern
     * occurs only once.
     */
    @Test
    public void findFirstMatchFindsOnlyMatch()
    {
        // Given
        String aCharSequence = "Stately, plump Buck Mulligan came from the stairhead";
        String aPattern = "came from";
        int aExpectedPos = aCharSequence.indexOf(aPattern);

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence);

        // Then
        assertEquals(aExpectedPos, aPos);
    }


    /**
     * The {@code findFirstMatch} method should find the first match in a char sequence when it
     * the pattern occurs multiple times.
     */
    @Test
    public void findFirstMatchFindsFirstMatch()
    {
        // Given
        String aCharSequence = "In a hole in the ground there lived a hobbit";
        String aPattern = " a ho";
        int aExpectedPos = aCharSequence.indexOf(aPattern);

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence);

        // Then
        assertEquals(aExpectedPos, aPos);
    }


    /**
     * The {@code findFirstMatch} method should return the absolute position of the match, not the
     * position relative to the start index.
     */
    @Test
    public void findFirstMatchReturnsAbsolutePosition()
    {
        // Given
        String aCharSequence = "C is a general-purpose programming language";
        String aPattern = "program";
        int aExpectedPos = aCharSequence.indexOf(aPattern);
        int aStartPos = aExpectedPos - 1;

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence, aStartPos, aCharSequence.length());

        // Then
        assertEquals(aExpectedPos, aPos);
    }


    /**
     * The {@code findFirstMatch} method should find the first match after the start index and
     * ignore matches before the start index.
     */
    @Test
    public void findFirstMatchFindsMatchAfterStartIndex()
    {
        // Given
        String aCharSequence = "The fair Ophelia! Nymph, in thy orisons";
        String aPattern = "he";
        int aFirstMatchPos = aCharSequence.indexOf(aPattern);
        int aSecondMatchPos = aCharSequence.indexOf(aPattern, aFirstMatchPos + 1);

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence, aFirstMatchPos + 1, aCharSequence.length());

        // Then
        assertEquals(aSecondMatchPos, aPos);
    }


    /**
     * The {@code findFirstMatch} method should find a match at the very start of the character
     * sequence.
     */
    @Test
    public void findFirstMatchFindsMatchAtStart()
    {
        // Given
        String aCharSequence = "As Gregor Samsa awoke one morning from uneasy dreams";
        String aPattern = "Samsa awoke one morning";
        int aMatchPos = aCharSequence.indexOf(aPattern);

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence, aMatchPos, aCharSequence.length());

        // Then
        assertEquals(aMatchPos, aPos);
    }


    /**
     * The {@code findFirstMatch} method should find a match at the very end of the character
     * sequence.
     */
    @Test
    public void findFirstMatchFindsMatchAtEnd()
    {
        // Given
        String aCharSequence = "Arla i urtid";
        String aPattern = "rtid";
        int aMatchPos = aCharSequence.length() - aPattern.length();

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence, aMatchPos, aCharSequence.length());

        // Then
        assertEquals(aMatchPos, aPos);
    }


    /**
     * The {@code findFirstMatch} method should ignore a partial match before an exact match.
     */
    @Test
    public void findFirstMatchIgnoresPartialMatchBeforeExactMatch()
    {
        // Given
        String aCharSequence = "abcabcabda";
        String aPattern ="abcabd";
        int aExpectedPos = 3;

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aPos = aMatcher.findFirstMatch(aCharSequence);

        // Then
        assertEquals(aExpectedPos, aPos);
    }


    /**
     * The {@code findAllMatches} method should not report any matches when passed a char sequence
     * that is shorter than the pattern.
     */
    @Test
    public void findAllMatchesFindsNothingInTooShortCharSequence()
    {
        // Given
        String aPattern = "Alice was beginning to get very tired of sitting by her sister";
        char[] aPatternChars = aPattern.toCharArray();
        String aText = aPattern.substring(0, aPattern.length() - 1);

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPatternChars);
        int aNumMatches = aMatcher.findAllMatches(aText, p -> {/*Discard*/});

        // Then
        assertEquals(0, aNumMatches);
    }


    /**
     * The {@code findAllMatches} method should find all occurrences of the pattern in the char
     * sequence.
     */
    @Test
    public void findAllMatchesFindsAllMatches()
    {
        // Given
        String aCharSequence = "Much to my surprise, it was such a touch of evil";
        String aPattern = "uch";
        int aNumExpectedMatches = 3;
        BitSet aPositions = new BitSet();

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aNumMatches = aMatcher.findAllMatches(aCharSequence, aPositions::set);

        // Then
        assertAll(
            () -> assertEquals(aNumExpectedMatches, aNumMatches),
            () -> assertEquals(aNumExpectedMatches, aPositions.cardinality()),
            () -> assertTrue(aPositions.get(1)),
            () -> assertTrue(aPositions.get(29)),
            () -> assertTrue(aPositions.get(37))
        );
    }


    /**
     * The {@code findAllMatches} method should find matches at the very start and end of the
     * character sequence.
     */
    @Test
    public void findAllMatchesFindsMatchesAtStartAndEnd()
    {
        // Given
        String aCharSequence = "The Weeping Willow was weeping and weeping silently";
        String aPattern = "eeping";
        int aStartMatchPos = aCharSequence.indexOf(aPattern);
        int aMiddleMatchPos = aCharSequence.indexOf(aPattern, aStartMatchPos + 1);
        int aEndMatchPos = aCharSequence.indexOf(aPattern, aMiddleMatchPos + 1);
        BitSet aPositions = new BitSet();

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aNumMatches = aMatcher.findAllMatches(
                aCharSequence,
                aStartMatchPos,
                aEndMatchPos + aPattern.length(),
                aPositions::set);

        // Then
        assertAll(
            () -> assertEquals(3, aNumMatches),
            () -> assertEquals(3, aPositions.cardinality()),
            () -> assertTrue(aPositions.get(aStartMatchPos)),
            () -> assertTrue(aPositions.get(aMiddleMatchPos)),
            () -> assertTrue(aPositions.get(aEndMatchPos))
        );
    }


    /**
     * The {@code findAllMatches} method should find all occurrences of a pattern that is repeated
     * throughout the char sequence.
     */
    @Test
    public void findAllMatchesFindsSlidingPattern()
    {
        // Given
        String aCharSequence = "aaaaaaaaaa";
        String aPattern = "aaaa";
        int aNumExpectedMatches = aCharSequence.length() - aPattern.length() + 1;
        BitSet aPositions = new BitSet();

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aNumMatches = aMatcher.findAllMatches(aCharSequence, aPositions::set);

        // Then
        assertEquals(aNumExpectedMatches, aNumMatches);
        assertEquals(aNumExpectedMatches, aPositions.cardinality());
        for (int i=0; i<aNumMatches; i++)
            assertTrue(aPositions.get(i));
    }


    /**
     * The {@code findAllMatches} method should find all occurrences of a one-char pattern that is
     * repeated throughout the char sequence.
     */
    @Test
    public void findAllMatchesFindsSlidingChar()
    {
        // Given
        String aCharSequence = "aaaaaaaaaa";
        char[] aPattern = new char[]{'a'};
        int aNumExpectedMatches = aCharSequence.length();
        BitSet aPositions = new BitSet();

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aNumMatches = aMatcher.findAllMatches(aCharSequence, aPositions::set);

        // Then
        assertEquals(aNumExpectedMatches, aNumMatches);
        assertEquals(aNumExpectedMatches, aPositions.cardinality());
        for (int i=0; i<aNumMatches; i++)
            assertTrue(aPositions.get(i));
    }


    /**
     * The {@code findAllMatches} method should not find a match in a char sequence when the match
     * occurs before the start index.
     */
    @Test
    public void findAllMatchesIgnoresMatchBeforeStartIndex()
    {
        // Given
        String aCharSequence = "A spectre is haunting Europe";
        String aPattern ="spectre";
        int aPosAfterMatch = aCharSequence.indexOf(aPattern) + 1;
        BitSet aPositions = new BitSet();

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aNumMatches = aMatcher.findAllMatches(aCharSequence, aPosAfterMatch, aCharSequence.length(), aPositions::set);

        // Then
        assertAll(
            () -> assertEquals(0, aNumMatches),
            () -> assertEquals(0, aPositions.cardinality())
        );
    }


    /**
     * The {@code findAllMatches} method should not find a match in a char sequence when the match
     * occurs after the end index.
     */
    @Test
    public void findAllMatchesIgnoresMatchAfterEndIndex()
    {
        // Given
        String aCharSequence = "All states, all powers, that have held and hold rule over men";
        String aPattern ="rule";
        int aMatchPos = aCharSequence.indexOf(aPattern);
        BitSet aPositions = new BitSet();

        // When
        KnuthMorrisPrattMatcher aMatcher = new KnuthMorrisPrattMatcher(aPattern);
        int aNumMatches = aMatcher.findAllMatches(aCharSequence, 0, aMatchPos, aPositions::set);

        // Then
        assertAll(
            () -> assertEquals(0, aNumMatches),
            () -> assertEquals(0, aPositions.cardinality())
        );
    }
}
