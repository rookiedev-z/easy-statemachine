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
 * @author rookiedev
 * @date 2020/8/26 22:50
 **/
public class ExternalTransitionConfigurer<S, E, C> extends AbstractTransitionConfigurer<S, E, C> {

    public ExternalTransitionConfigurer(StateRepresentation<S, E, C> stateRepresentation, Function<S, StateRepresentation<S, E, C>> lookup) {
        super(stateRepresentation, lookup);
    }

    public ExternalTransitionConfigurer<S, E, C> permit(E event, S destination) {
        enforceNotIdentityTransition(destination);
        return publicPermit(event, destination);
    }

    public ExternalTransitionConfigurer<S, E, C> permit(E event, S destination, Action<S, E, C> action) {
        enforceNotIdentityTransition(destination);
        return publicPermit(event, destination, action);
    }

    public ExternalTransitionConfigurer<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard) {
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard);
    }

    public ExternalTransitionConfigurer<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard) {
        enforceNotIdentityTransition(destination);
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, destination, guard, this.DEFAULT_ACTION, throwAction);
    }

    public ExternalTransitionConfigurer<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action) {
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard, action);
    }

    public ExternalTransitionConfigurer<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action) {
        enforceNotIdentityTransition(destination);
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, destination, guard, action, throwAction);
    }

    public ExternalTransitionConfigurer<S, E, C> permitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        enforceNotIdentityTransition(destination);
        return publicPermitIfElse(event, destination, guard, ifAction, elseAction);
    }

    public ExternalTransitionConfigurer<S, E, C> permitReentry(E event, Action<S, E, C> action) {
        return publicPermit(event, this.stateRepresentation.getState(), action);
    }

    public ExternalTransitionConfigurer<S, E, C> permitReentryIf(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        return publicPermitIf(event, this.stateRepresentation.getState(), guard, action);
    }

    public ExternalTransitionConfigurer<S, E, C> permitReentryIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, this.stateRepresentation.getState(), guard, action, throwAction);
    }

    public ExternalTransitionConfigurer<S, E, C> permitReentryIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        return publicPermitIfElse(event, this.stateRepresentation.getState(), guard, ifAction, elseAction);
    }

    public ExternalTransitionConfigurer<S, E, C> onEntry(Action<S, E, C> action) {
        assert action != null : ENTRY_ACTION_IS_NULL;
        this.stateRepresentation.addEntryAction(action);
        return this;
    }

    public ExternalTransitionConfigurer<S, E, C> onEntryFrom(E event, Action<S, E, C> action) {
        assert event != null : EVENT_IS_NULL;
        assert action != null : ENTRY_ACTION_IS_NULL;
        this.stateRepresentation.addEntryActionFrom(event, action);
        return this;
    }

    public ExternalTransitionConfigurer<S, E, C> onExit(Action<S, E, C> action) {
        assert action != null : EXIT_ACTION_IS_NULL;
        this.stateRepresentation.addExitAction(action);
        return this;
    }

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
