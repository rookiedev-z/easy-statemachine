package net.gittab.statemachine.configurers;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.state.StateRepresentation;
import net.gittab.statemachine.transition.InternalTransition;
import net.gittab.statemachine.transition.TransitionData;

import java.util.function.Function;

/**
 * InternalTransitionConfigurer.
 *
 * @author rookiedev 2020/8/26 22:53
 **/
public class InternalTransitionConfigurer<S, E, C> extends AbstractTransitionConfigurer<S, E, C> {

    public InternalTransitionConfigurer(StateRepresentation<S, E, C> stateRepresentation, Function<S, StateRepresentation<S, E, C>> lookup) {
        super(stateRepresentation, lookup);
    }

    /**
     * accept the specified trigger event, execute action and stay in current state.
     * applies to the current state only. no exit or entry actions will be
     * executed and the state will not change. the only thing that happens is
     * the execution of a given action.
     * @param event the triggered event
     * @param action the action to be performed
     * @return the transition configurer
     */
    public InternalTransitionConfigurer<S, E, C> permit(E event, Action<S, E, C> action) {
        return publicPermitInternal(event, action);
    }

    /**
     * accept the specified trigger event, execute action and stay in current state.
     * applies to the current state only. no exit or entry actions will be executed and the state will not change.
     * the only thing that happens is the execution of a given action.
     * the action is only executed if the given guard returns true. otherwise ignore.
     * @param event the triggered event
     * @param guard function that must return true in order for the trigger event to be accepted
     * @param action the action to be performed
     * @return the transition configurer
     */
    public InternalTransitionConfigurer<S, E, C> permitIf(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        return publicPermitInternalIf(event, guard, action);
    }

    /**
     * accept the specified trigger event, execute action and stay in current state.
     * applies to the current state only. no exit or entry actions will be executed and the state will not change.
     * the only thing that happens is the execution of a given action.
     * the action is only executed if the given guard returns true. otherwise throw IllegalStateException.
     * @param event the triggered event
     * @param guard function that must return true in order for the trigger event to be accepted
     * @param action the action to be performed
     * @return the transition configurer
     */
    public InternalTransitionConfigurer<S, E, C> permitIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitInternalIfElse(event, guard, action, throwAction);
    }

    /**
     * accept the specified trigger event, execute action and stay in current state.
     * applies to the current state only. no exit or entry actions will be executed and the state will not change.
     * the only thing that happens is the execution of a given action.
     * the ifAction is executed if the given guard returns true. otherwise elseAction executed.
     * @param event the triggered event
     * @param guard function that must return true in order for the trigger event to be accepted
     * @param ifAction the action to be performed if guard is true
     * @param elseAction the action to be performed if guard is false
     * @return the transition configurer
     */
    public InternalTransitionConfigurer<S, E, C> permitIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        return publicPermitInternalIfElse(event, guard, ifAction, elseAction);
    }

    /**
     * sets the superstate that the configured state is a sub state of.
     * sub states inherit the allowed transitions of their superstate.
     * @param superstate the super state
     * @return the transition configurer
     */
    @Override
    public InternalTransitionConfigurer<S, E, C> subStateOf(S superstate) {
        super.subStateOf(superstate);
        return this;
    }

    @Override
    public StateMachineTransitionConfigurer<S, E, C> and() {
        return null;
    }

    InternalTransitionConfigurer<S, E, C> publicPermitInternal(E event, Action<S, E, C> action){
        return publicPermitInternalIf(event, this.DEFAULT_GUARD, action);
    }

    InternalTransitionConfigurer<S, E, C> publicPermitInternalIf(E event, Guard<S, E, C> guard, Action<S, E, C> action){
        assert guard != null : GUARD_IS_NULL;
        assert action != null : ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), this.stateRepresentation.getState(), event);
        this.stateRepresentation.addEventTransition(new InternalTransition<>(transitionData, guard, action));
        return this;
    }

    InternalTransitionConfigurer<S, E, C> publicPermitInternalIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction){
        assert guard != null : GUARD_IS_NULL;
        assert ifAction != null : IF_ACTION_IS_NULL;
        assert elseAction != null : ELSE_ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), this.stateRepresentation.getState(), event);
        this.stateRepresentation.addEventTransition(new InternalTransition<>(transitionData, guard, ifAction, elseAction));
        return this;
    }
}
