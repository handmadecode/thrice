/*
 * Copyright 2009, 2016, 2020 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for {@link org.myire.util.FiniteStateMachine}.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
public class FiniteStateMachineTest
{
    private enum State { STATE1, STATE2, STATE3, STATE4 }
    private enum Input { INPUT1, INPUT2, INPUT3 }

    static private final BiFunction<State, Input, State> IDENTITY_NEXT_FN = (s, i) -> s;
    static private final BiFunction<State, Input, Object> NULL_OUT_FN = (s, i) -> null;


    /**
     * The constructor should throw a {@code NullPointerException} for a null next function
     * argument.
     */
    @SuppressWarnings("unused")
    @Test
    public void ctorThrowsForNullNextFunction()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                new FiniteStateMachine<>(State.STATE1, null, NULL_OUT_FN)
        );
    }


    /**
     * The constructor should throw a {@code NullPointerException} for a null output function
     * argument.
     */
    @SuppressWarnings("unused")
    @Test
    public void ctorThrowsForNullOutputFunction()
    {
        assertThrows(
            NullPointerException.class,
            () ->
                new FiniteStateMachine<>(State.STATE2, IDENTITY_NEXT_FN, null)
        );
    }


    /**
     * The {@code getCurrentState} method should return the initial state immediately after
     * construction.
     */
    @Test
    public void getCurrentStateReturnsInitialStateAfterConstruction()
    {
        // Given
        State aInitialState = State.STATE3;

        // When
        FiniteStateMachine<State, Input, Object> aFSM =
                new FiniteStateMachine<>(aInitialState, IDENTITY_NEXT_FN, NULL_OUT_FN);

        // Then
        assertEquals(aInitialState, aFSM.getCurrentState());
    }


    /**
     * The {@code consumeAndEmit} method should perform the expected state transition.
     */
    @Test
    public void consumeAndEmitPerformsTransition()
    {
        // Given
        State aNextState = State.STATE4;
        FiniteStateMachine<State, Input, Object> aFSM =
                new FiniteStateMachine<>(State.STATE1, (s, i) -> aNextState, NULL_OUT_FN);

        // When
        aFSM.consumeAndEmit(Input.INPUT2);

        // Then
        assertEquals(aNextState, aFSM.getCurrentState());
    }


    /**
     * The {@code consumeAndEmit} method should return the expected output symbol.
     */
    @Test
    public void consumeAndEmitReturnsTheExpectedOutput()
    {
        // Given
        Object aOutput = new Object();
        FiniteStateMachine<State, Input, Object> aFSM =
                new FiniteStateMachine<>(State.STATE1, IDENTITY_NEXT_FN, (s, i) -> aOutput);

        // When
        Object aEmitted = aFSM.consumeAndEmit(Input.INPUT3);

        // Then
        assertSame(aEmitted, aOutput);
    }


    /**
     * The {@code reset} method should set the current state to the initial state.
     */
    @Test
    public void resetSetsCurrentStateToInitial()
    {
        // Given
        State aInitialState = State.STATE1;
        State aNextState = State.STATE2;
        FiniteStateMachine<State, Input, Object> aFSM =
                new FiniteStateMachine<>(aInitialState, (s, i) -> aNextState, NULL_OUT_FN);
        aFSM.consumeAndEmit(Input.INPUT1);
        assertEquals(aNextState, aFSM.getCurrentState());

        // When
        aFSM.reset();

        // Then
        assertEquals(aInitialState, aFSM.getCurrentState());
    }


    /**
     * An input sequence fed to {@code consumeAndEmit} method should perform the expected
     * transitions and return the expected output.
     */
    @SuppressWarnings("boxing")
    @Test
    public void inputSequenceGeneratesTheExpectedTransitionsAndOutput()
    {
        // Given
        final Integer ZERO = 0;
        final Integer ONE = 1;
        final Integer TWO = 2;
        FiniteStateMachine<Modulo3State, Modulo3Input, Integer> aFSR =
                new FiniteStateMachine<>(
                        Modulo3State.ZERO,
                        FiniteStateMachineTest::mod3NextFunction,
                        FiniteStateMachineTest::mod3OutputFunction);

        // Then
        assertEquals(Modulo3State.ZERO, aFSR.getCurrentState());

        assertEquals(ONE, aFSR.consumeAndEmit(Modulo3Input.ADD_1));
        assertEquals(Modulo3State.ONE, aFSR.getCurrentState());

        assertEquals(TWO, aFSR.consumeAndEmit(Modulo3Input.ADD_1));
        assertEquals(Modulo3State.TWO, aFSR.getCurrentState());

        assertEquals(ZERO, aFSR.consumeAndEmit(Modulo3Input.ADD_1));
        assertEquals(Modulo3State.ZERO, aFSR.getCurrentState());

        assertEquals(ZERO, aFSR.consumeAndEmit(Modulo3Input.ADD_0));
        assertEquals(Modulo3State.ZERO, aFSR.getCurrentState());

        assertEquals(TWO, aFSR.consumeAndEmit(Modulo3Input.ADD_2));
        assertEquals(Modulo3State.TWO, aFSR.getCurrentState());

        assertEquals(ONE, aFSR.consumeAndEmit(Modulo3Input.ADD_2));
        assertEquals(Modulo3State.ONE, aFSR.getCurrentState());

        assertEquals(ONE, aFSR.consumeAndEmit(Modulo3Input.ADD_0));
        assertEquals(Modulo3State.ONE, aFSR.getCurrentState());

        assertEquals(ZERO, aFSR.consumeAndEmit(Modulo3Input.ADD_2));
        assertEquals(Modulo3State.ZERO, aFSR.getCurrentState());

        assertEquals(TWO, aFSR.consumeAndEmit(Modulo3Input.ADD_2));
        assertEquals(Modulo3State.TWO, aFSR.getCurrentState());
    }


    // States, input, next function, and output function for a simple modulo-3 state machine.
    private enum Modulo3State { ZERO, ONE, TWO }
    static private final Modulo3State[] cModulo3States = Modulo3State.values();
    private enum Modulo3Input { ADD_0, ADD_1, ADD_2 }

    static private Modulo3State mod3NextFunction(Modulo3State pState, Modulo3Input pInput)
    {
        switch (pInput)
        {
            case ADD_1:
                return cModulo3States[(pState.ordinal() + 1) % cModulo3States.length];
            case ADD_2:
                return cModulo3States[(pState.ordinal() + 2) % cModulo3States.length];
            default:
                return pState;
        }
    }

    @SuppressWarnings("boxing")
    static private Integer mod3OutputFunction(Modulo3State pState, Modulo3Input pInput)
    {
        switch (pInput)
        {
            case ADD_1:
                return (pState.ordinal() + 1) % cModulo3States.length;
            case ADD_2:
                return (pState.ordinal() + 2) % cModulo3States.length;
            default:
                return pState.ordinal();
        }
    }
}
