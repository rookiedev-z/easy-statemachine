package net.gittab.statemachine;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.state.StateReference;
import net.gittab.statemachine.state.StateRepresentation;
import net.gittab.statemachine.transition.Transition;
import net.gittab.statemachine.transition.TransitionBehaviour;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * StateMachine.
 *
 * @author rookiedev
 * @date 2020/8/21 11:22 上午
 **/
public class StateMachine<S, E, C> {

    protected final StateMachineConfig<S, E, C> config;
    protected final Supplier<S> stateAccessor;
    protected final Consumer<S> stateMutator;
    private boolean isStarted = false;
    private S initialState;

    protected Action<S, E, Transition<S, E>, C> unknownEventAction = (transition, context) -> {
        throw new IllegalStateException(
                String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transition.getSource(), transition.getEvent(), context)
        );
    };

    public StateMachine(S initialState, StateMachineConfig<S, E, C> config) {
        this.initialState = initialState;
        this.config = config;
        final StateReference<S> reference = new StateReference<>();
        this.stateAccessor = reference::getState;
        this.stateMutator = reference::setState;
        stateMutator.accept(initialState);
    }

    public StateMachine(S initialState, Supplier<S> stateAccessor, Consumer<S> stateMutator, StateMachineConfig<S, E, C> config) {
        this.config = config;
        this.stateAccessor = stateAccessor;
        this.stateMutator = stateMutator;
        stateMutator.accept(initialState);
    }

    public void fireInitialTransition() {
        fireInitialTransition(null);
    }

    public void fireInitialTransition(C context) {
        S currentState = getCurrentRepresentation().getState();
        if (isStarted || !currentState.equals(initialState)) {
            throw new IllegalStateException("Firing initial transition after state machine has been started");
        }
        isStarted = true;
        Transition<S, E> initialTransition = new Transition<S, E>(null, currentState, null) {

            @Override
            public S transition() {
                return null;
            }
        };
        getCurrentRepresentation().entry(initialTransition, context);
    }

    StateRepresentation<S, E, C> getCurrentRepresentation() {
        StateRepresentation<S, E, C> representation = config.getStateRepresentation(getState());
        return representation == null ? new StateRepresentation<>(getState()) : representation;
    }

    public void fire(E event) {
        fire(event, null);
    }

    public void fire(E event, C context) {
        publicFire(event, context);
    }

    void publicFire(E event, C context){

        isStarted = true;

        TransitionBehaviour<S, E, Transition<S, E>, C> transitionBehaviour = getCurrentRepresentation().tryFindTransitionBehaviour(event, context);
        if (transitionBehaviour == null) {
            this.unknownEventAction.execute(getUnknownTransition(getState(), event), context);
            return;
        }
        Transition<S, E> transition = transitionBehaviour.getTransition();
        if (transitionBehaviour.isInternal()) {
            transitionBehaviour.action(context);
        } else {
            S destination = transitionBehaviour.transition(context);
            getCurrentRepresentation().exit(transition, context);
            transitionBehaviour.action(context);
            setState(destination);
            getCurrentRepresentation().entry(transition, context);
        }
    }

    private Transition<S, E> getUnknownTransition(S source, E event){
        return new Transition<S, E>(source, null, event) {
            @Override
            public S transition() {
                return null;
            }
        };
    }

    public boolean isInState(S state) {
        return getCurrentRepresentation().isIncludedIn(state);
    }

    public S getState() {
        return stateAccessor.get();
    }

    private void setState(S value) {
        stateMutator.accept(value);
    }


}
