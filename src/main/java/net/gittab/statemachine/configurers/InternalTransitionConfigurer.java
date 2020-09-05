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
 * @author rookiedev
 * @date 2020/8/26 22:53
 **/
public class InternalTransitionConfigurer<S, E, C> extends AbstractTransitionConfigurer<S, E, C> {

    public InternalTransitionConfigurer(StateRepresentation<S, E, C> stateRepresentation, Function<S, StateRepresentation<S, E, C>> lookup) {
        super(stateRepresentation, lookup);
    }

    public InternalTransitionConfigurer<S, E, C> permit(E event, Action<S, E, C> action) {
        return publicPermitInternal(event, action);
    }

    public InternalTransitionConfigurer<S, E, C> permitIf(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        return publicPermitInternalIf(event, guard, action);
    }

    public InternalTransitionConfigurer<S, E, C> permitIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitInternalIfElse(event, guard, action, throwAction);
    }

    
    public InternalTransitionConfigurer<S, E, C> permitIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        return publicPermitInternalIfElse(event, guard, ifAction, elseAction);
    }

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
