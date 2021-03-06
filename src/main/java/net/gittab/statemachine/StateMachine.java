package net.gittab.statemachine;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.state.StateReference;
import net.gittab.statemachine.state.StateRepresentation;
import net.gittab.statemachine.transition.AbstractTransition;
import net.gittab.statemachine.transition.TransitionData;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * StateMachine.
 *
 * @author rookiedev 2020/8/21 11:22 上午
 **/
public class StateMachine<S, E, C> {

    /**
     * state machine config.
     */
    protected final StateMachineConfig<S, E, C> config;
    /**
     * state accessor.
     */
    protected final Supplier<S> stateAccessor;
    /**
     * state mutator.
     */
    protected final Consumer<S> stateMutator;
    /**
     * state machine has it started.
     */
    private boolean isStarted = false;

    /**
     * initial state.
     */
    private S initialState;

    /**
     * unknown event handling action.
     */
    private Action<S, E, C> unknownEventAction = (transitionData, context) -> {
        throw new IllegalStateException(
                String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
        );
    };

    /**
     * construct a state machine.
     * @param initialState initial state
     * @param config state machine config
     */
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

    /**
     * fire initial transition into the initial state.
     * all super-states are entered too.
     * this method can be called only once, before state machine is used.
     */
    public void fireInitialTransition() {
        fireInitialTransition(null);
    }

    /**
     * fire initial transition into the initial state with context.
     * all super-states are entered too.
     * this method can be called only once, before state machine is used.
     * @param context transition context
     */
    public void fireInitialTransition(C context) {
        S currentState = getCurrentRepresentation().getState();
        if (isStarted || !currentState.equals(initialState)) {
            throw new IllegalStateException("Firing initial transition after state machine has been started");
        }
        isStarted = true;
        TransitionData<S, E> initialTransition = new TransitionData<>(null, currentState, null);
        getCurrentRepresentation().entry(initialTransition, context);
    }

    StateRepresentation<S, E, C> getCurrentRepresentation() {
        StateRepresentation<S, E, C> representation = config.getStateRepresentation(getState());
        return representation == null ? new StateRepresentation<>(getState()) : representation;
    }

    /**
     * the currently-permissible trigger events.
     * @param context transition context
     * @return the currently-permissible trigger events
     */
    public List<E> getPermittedEvents(C context) {
        return getCurrentRepresentation().getPermittedEvents(context);
    }

    /**
     * transition from the current state via the specified trigger event.
     * the target state is determined by the configuration of the current state.
     * actions associated with leaving the current state and entering the new one will be invoked.
     * @param event the trigger event to fire
     */
    public void fire(E event) {
        fire(event, null);
    }

    /**
     * transition with context from the current state via the specified trigger event.
     * the target state is determined by the configuration of the current state.
     * actions associated with leaving the current state and entering the new one will be invoked.
     * @param event the trigger event to fire
     * @param context transition context
     */
    public void fire(E event, C context) {
        publicFire(event, context);
    }

    void publicFire(E event, C context){

        isStarted = true;

        AbstractTransition<S, E, C> transition = getCurrentRepresentation().tryFindTransition(event, context);
        if (transition == null) {
            this.unknownEventAction.execute(getUnknownTransition(getState(), event), context);
            return;
        }
        if(transition.isGuardMet(context) || transition.existElseAction()){
            execute(transition, context);
        }
    }

    private void execute(AbstractTransition<S, E, C> transition, C context){
        if (transition.isInternal()) {
            transition.action(context);
        } else {
            S destination = transition.transition(context);
            TransitionData<S, E> transitionData = transition.getTransitionData();
            getCurrentRepresentation().exit(transitionData, context);
            transition.action(context);
            setState(destination);
            getCurrentRepresentation().entry(transitionData, context);
        }
    }

    public void unknownEventAction(Action<S, E, C> unknownEventAction) {
        if (unknownEventAction == null) {
            throw new IllegalStateException("unknown event action");
        }
        this.unknownEventAction = unknownEventAction;
    }

    private TransitionData<S, E> getUnknownTransition(S source, E event){
        return new TransitionData<>(source, null, event);
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
