package net.gittab.statemachine.configurers;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.state.StateRepresentation;
import net.gittab.statemachine.transition.ExternalTransition;
import net.gittab.statemachine.transition.TransitionData;

import java.util.Objects;
import java.util.function.Function;

/**
 * ExternalTransitionConfigurer.
 *
 * @author rookiedev 2020/8/26 22:50
 **/
public class ExternalTransitionConfigurer<S, E, C> extends AbstractTransitionConfigurer<S, E, C> {

    public ExternalTransitionConfigurer(StateRepresentation<S, E, C> stateRepresentation, Function<S, StateRepresentation<S, E, C>> lookup) {
        super(stateRepresentation, lookup);
    }

    /**
     * accept the specified event and transition to destination state.
     * @param event the triggered event
     * @param destination the state that the event will cause a transition to
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permit(E event, S destination) {
        enforceNotIdentityTransition(destination);
        return publicPermit(event, destination);
    }

    /**
     * accept the specified event and transition to destination state.
     * additionally a given action is performed when transitioning, this action will be executed
     * after the onExit action of the current state and before the onEntry action of the destination state.
     * @param event the triggered event
     * @param destination the state that the event will cause a transition to
     * @param action the action to be performed during transition
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permit(E event, S destination, Action<S, E, C> action) {
        enforceNotIdentityTransition(destination);
        return publicPermit(event, destination, action);
    }

    /**
     * accept the specified event and transition to destination state if guard is true.
     * @param event the triggered event
     * @param destination the state that the event will cause a transition to
     * @param guard function that must return true in order for the event to be triggered.
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard) {
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard);
    }

    /**
     * accept the specified event and transition to destination state if guard is true,  otherwise throw IllegalStateException.
     * @param event the triggered event
     * @param destination the state that the event will cause a transition to
     * @param guard function that must return true in order for the event to be triggered.
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard) {
        enforceNotIdentityTransition(destination);
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, destination, guard, this.DEFAULT_ACTION, throwAction);
    }

    /**
     * accept the specified event and transition to destination state if guard is true.
     * additionally a given action is performed when transitioning, this action will be executed
     * after the onExit action of the current state and before the onEntry action of the destination state.
     * @param event the triggered event
     * @param destination the state that the event will cause a transition to
     * @param guard function that must return true in order for the event to be triggered.
     * @param action the action to be performed during transition
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action) {
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard, action);
    }

    /**
     * accept the specified event and transition to destination state if guard is true, otherwise throw IllegalStateException.
     * additionally a given action is performed when transitioning, this action will be executed
     * after the onExit action of the current state and before the onEntry action of the destination state.
     * @param event the triggered event
     * @param destination the state that the event will cause a transition to
     * @param guard function that must return true in order for the event to be triggered.
     * @param action the action to be performed during transition
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action) {
        enforceNotIdentityTransition(destination);
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, destination, guard, action, throwAction);
    }

    /**
     * accept the specified event and transition to destination state.
     * additionally given action is performed when transitioning, if guard is true this ifAction will be executed, elseAction will be executed otherwise,
     * after the onExit action of the current state and before the onEntry action of the destination state.
     * @param event the triggered event
     * @param destination the state that the event will cause a transition to
     * @param guard function that must return true in order for the event to be triggered.
     * @param ifAction the action to be performed during transition if guard is true
     * @param elseAction the action to be performed during transition if guard is false
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        enforceNotIdentityTransition(destination);
        return publicPermitIfElse(event, destination, guard, ifAction, elseAction);
    }

    /**
     * accept the specified event, execute exit actions and re-execute entry actions. reentry behaves as though the
     * configured state transitions to an identical sibling state.
     * applies to the current state only. will not re-execute superstate actions, or cause actions to execute
     * transitioning between super-states and sub-states.
     * @param event the triggered event
     * @param action the action to be performed during transition
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitReentry(E event, Action<S, E, C> action) {
        return publicPermit(event, this.stateRepresentation.getState(), action);
    }

    /**
     * accept the specified event, execute exit actions and re-execute entry actions. reentry behaves as though the
     * configured state transitions to an identical sibling state.
     * applies to the current state only. will not re-execute superstate actions, or cause actions to execute
     * transitioning between super-states and sub-states.
     * additionally a given action is performed when transitioning if guard is true. this action will be called after
     * the onExit action and before the onEntry action (of the re-entered state).
     * @param event the triggered event
     * @param guard function that must return true in order for the event to be triggered.
     * @param action the action to be performed during transition
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitReentryIf(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        return publicPermitIf(event, this.stateRepresentation.getState(), guard, action);
    }

    /**
     * accept the specified event, execute exit actions and re-execute entry actions. reentry behaves as though the
     * configured state transitions to an identical sibling state.
     * applies to the current state only. will not re-execute superstate actions, or cause actions to execute
     * transitioning between super-states and sub-states.
     * additionally a given action is performed when transitioning. if guard is true, this action will be called after
     * the onExit action and before the onEntry action (of the re-entered state), otherwise throw IllegalStateException.
     * @param event the triggered event
     * @param guard function that must return true in order for the event to be triggered.
     * @param action the action to be performed during transition
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitReentryIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, this.stateRepresentation.getState(), guard, action, throwAction);
    }

    /**
     * accept the specified event, execute exit actions and re-execute entry actions. reentry behaves as though the
     * configured state transitions to an identical sibling state.
     * applies to the current state only. will not re-execute superstate actions, or cause actions to execute
     * transitioning between super-states and sub-states.
     * additionally a given action is performed when transitioning. if guard is true, this ifAction will be called after
     * the onExit action and before the onEntry action (of the re-entered state), otherwise elseAction will be called.
     * @param event the triggered event
     * @param guard function that must return true in order for the event to be triggered.
     * @param ifAction the action to be performed during transition if guard is true
     * @param elseAction the action to be performed during transition if guard is false
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> permitReentryIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        return publicPermitIfElse(event, this.stateRepresentation.getState(), guard, ifAction, elseAction);
    }

    /**
     * specify an action that will execute then transitioning into the configured state.
     * @param action action to execute
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> onEntry(Action<S, E, C> action) {
        assert action != null : ENTRY_ACTION_IS_NULL;
        this.stateRepresentation.addEntryAction(action);
        return this;
    }

    /**
     * specify an action that will execute when transitioning into the configured state.
     * @param event the triggered event by which the state must be entered in order for the action to execute
     * @param action action to execute
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> onEntryFrom(E event, Action<S, E, C> action) {
        assert event != null : EVENT_IS_NULL;
        assert action != null : ENTRY_ACTION_IS_NULL;
        this.stateRepresentation.addEntryActionFrom(event, action);
        return this;
    }

    /**
     * specify an action that will execute when transitioning from the configured state.
     * @param action action to execute
     * @return the transition configurer
     */
    public ExternalTransitionConfigurer<S, E, C> onExit(Action<S, E, C> action) {
        assert action != null : EXIT_ACTION_IS_NULL;
        this.stateRepresentation.addExitAction(action);
        return this;
    }

    /**
     * sets the superstate that the configured state is a sub state of.
     * sub states inherit the allowed transitions of their superstate.
     * @param superstate the super state
     * @return the transition configurer
     */
    @Override
    public ExternalTransitionConfigurer<S, E, C> subStateOf(S superstate) {
        super.subStateOf(superstate);
        return this;
    }

    @Override
    public StateMachineTransitionConfigurer<S, E, C> and() {

        return null;
    }

    ExternalTransitionConfigurer<S, E, C> publicPermit(E event, S destination){
        return publicPermitIf(event, destination, this.DEFAULT_GUARD, this.DEFAULT_ACTION);
    }

    ExternalTransitionConfigurer<S, E, C> publicPermit(E event, S destination, final Action<S, E, C> action){
        return publicPermitIf(event, destination, this.DEFAULT_GUARD, action);
    }

    ExternalTransitionConfigurer<S, E, C> publicPermitIf(E event, S destination, Guard<S, E, C> guard){
        return publicPermitIf(event, destination, guard, this.DEFAULT_ACTION);
    }

    ExternalTransitionConfigurer<S, E, C> publicPermitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action){
        assert guard != null : GUARD_IS_NULL;
        assert action != null : ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), destination, event);
        this.stateRepresentation.addEventTransition(new ExternalTransition<>(transitionData, guard, action));
        return this;
    }

    ExternalTransitionConfigurer<S, E, C> publicPermitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction){
        assert guard != null : GUARD_IS_NULL;
        assert ifAction != null : IF_ACTION_IS_NULL;
        assert elseAction != null : ELSE_ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), destination, event);
        this.stateRepresentation.addEventTransition(new ExternalTransition<>(transitionData, guard, ifAction, elseAction));
        return this;
    }

    void enforceNotIdentityTransition(S destination) {
        if (Objects.equals(destination, this.stateRepresentation.getState())) {
            throw new IllegalStateException("Permit() (and PermitIf()) require that the destination state is not equal to the source state. To accept a trigger without changing state, use either ignore(), permitInternal() or permitReentry().");
        }
    }
}
