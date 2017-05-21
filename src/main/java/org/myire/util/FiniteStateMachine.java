/*
 * Copyright 2009, 2016 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.util;

import java.util.function.BiFunction;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;


/**
 * A <i>Finite State Machine</i> is defined by
 *<ul>
 * <li>a finite set of possible states, {@code S}</li>
 * <li>a initial state {@code s0}, which is a member of {@code S}</li>
 * <li>a finite set of recognized input symbols {@code I}, called the <i>input alphabet</i></li>
 * <li>a finite set of output symbols {@code O}, called the <i>output alphabet</i></li>
 * <li>a state transition function (also called the <i>next</i> function) {@code T: S x I -> S} that
 *     maps pairs of state and input symbol to states</li>
 * <li>an output function {@code G: S x I -> O} that maps pairs of state and input symbol to output
 *     symbols</li>
 *</ul>
 * This implementation is a <i>Mealy machine</i>, meaning that the output is determined both by its
 * current state and the current input (as opposed to a <i>Moore machine</i> whose output is
 * determined solely by the current state).
 *<p>
 * The concept of final (or accepting) states is not supported by this implementation. Users can
 * however easily add this by comparing the FSM's current state to an externally defined set of
 * final states when there is no more input.
 *<p>
 * Whether or not {@code null} is valid state or a valid symbol in the input and output alphabets is
 * a property of the next and output functions. The FSM itself puts no restrictions on using
 * {@code null}.
 *<p>
 * The possible states, input alphabet, and output alphabet are all parametrized types. There are
 * no restrictions on these types, but the fact that that the sets are finite may imply that using
 * {@code Enum} types is a good choice.
 *<p>
 * Instances of this class are <b>not</b> safe for use by multiple threads without external
 * synchronization.
 *
 * @param <S>   The type that defines the set of possible states for the FSM.
 * @param <I>   The type that defines the input alphabet for the FSM.
 * @param <O>   The type that defines the output alphabet.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
@NotThreadSafe
public class FiniteStateMachine<S, I, O>
{
    private final S fInitialState;
    private S fCurrentState;
    private final BiFunction<S, I, S> fNextFunction;
    private final BiFunction<S, I, O> fOutputFunction;


    /**
     * Create a new {@code FiniteStateMachine}.
     *
     * @param pInitialState     The FSM's initial state.
     * @param pNextFunction     The FSM's next function.
     * @param pOutputFunction   The FSM's output function.
     *
     * @throws NullPointerException if any of the function parameters is null.
     */
    public FiniteStateMachine(
        @Nullable S pInitialState,
        @Nonnull BiFunction<S, I, S> pNextFunction,
        @Nonnull BiFunction<S, I, O> pOutputFunction)
    {
        fInitialState = pInitialState;
        fCurrentState = pInitialState;
        fNextFunction = requireNonNull(pNextFunction);
        fOutputFunction = requireNonNull(pOutputFunction);
    }


    /**
     * Get the current state of the FSM.
     *
     * @return  The current state of the FSM.
     */
    @Nullable
    public S getCurrentState()
    {
        return fCurrentState;
    }


    /**
     * Consume an input symbol and emit the result of the output function. The FSM will be
     * transitioned into the appropriate state as defined by the state transition function. The
     * new current state can be retrieved by calling {@link #getCurrentState()}.
     *
     * @param pInput    The input symbol.
     *
     * @return  The result of the output function.
     */
    @Nullable
    public O consumeAndEmit(@Nullable I pInput)
    {
        S aNextState = fNextFunction.apply(fCurrentState, pInput);
        O aOutput = fOutputFunction.apply(fCurrentState, pInput);
        fCurrentState = aNextState;
        return aOutput;
    }


    /**
     * Reset the FSM by setting its current state to the initial state. This is not a state
     * transition; neither the next function nor the output function will be called, and no output
     * will be emitted.
     */
    public void reset()
    {
        fCurrentState = fInitialState;
    }
}
