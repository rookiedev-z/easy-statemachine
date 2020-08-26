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
 * @author rookiedev
 * @date 2020/8/21 11:22 上午
 **/
public class StateMachine<S, E, C> {

    protected final StateMachineConfig<S, E, C> config;
    protected final Supplier<S> stateAccessor;
    protected final Consumer<S> stateMutator;
    private boolean isStarted = false;
    private S initialState;

    private Action<S, E, C> unknownEventAction = (transitionData, context) -> {
        throw new IllegalStateException(
                String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
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
        TransitionData<S, E> initialTransition = new TransitionData<>(null, currentState, null);
        getCurrentRepresentation().entry(initialTransition, context);
    }

    StateRepresentation<S, E, C> getCurrentRepresentation() {
        StateRepresentation<S, E, C> representation = config.getStateRepresentation(getState());
        return representation == null ? new StateRepresentation<>(getState()) : representation;
    }

    public List<E> getPermittedEvents(C context) {
        return getCurrentRepresentation().getPermittedEvents(context);
    }

    public void fire(E event) {
        fire(event, null);
    }

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
        if (transition.isInternal()) {
            transition.action(context);
        } else {

            S destination = transition.transition(context);
//            if(destination == null){
//                return;
//            }
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
