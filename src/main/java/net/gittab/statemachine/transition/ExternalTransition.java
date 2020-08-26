package net.gittab.statemachine.transition;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;

/**
 * ExternalTransition.
 *
 * @author rookiedev
 * @date 2020/8/24 15:22
 **/
public class ExternalTransition<S, E, C> extends AbstractTransition<S, E, C> {

    public ExternalTransition(TransitionData<S, E> transitionData, Guard<S, E, C> guard, Action<S, E, C> ifAction) {
        super(transitionData, guard, ifAction);
    }

    public ExternalTransition(TransitionData<S, E> transitionData, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        super(transitionData, guard, ifAction, elseAction);
    }

    @Override
    public boolean isInternal() {
        return false;
    }

}
