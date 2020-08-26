package net.gittab.statemachine.transition;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;

/**
 * InternalTransition.
 *
 * @author rookiedev
 * @date 2020/8/24 22:37
 **/
public class InternalTransition<S, E, C> extends AbstractTransition<S, E, C> {

    public InternalTransition(TransitionData<S, E> transitionData, Guard<S, E, C> guard, Action<S, E, C> ifAction) {
        super(transitionData, guard, ifAction);
    }

    public InternalTransition(TransitionData<S, E> transitionData, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        super(transitionData, guard, ifAction, elseAction);
    }

    @Override
    public boolean isInternal() {
        return true;
    }

}
