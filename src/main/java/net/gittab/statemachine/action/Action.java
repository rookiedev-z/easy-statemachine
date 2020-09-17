package net.gittab.statemachine.action;

import net.gittab.statemachine.transition.TransitionData;

/**
 * Action.
 *
 * @author rookiedev 2020/8/21 3:03 下午
 **/
@FunctionalInterface
public interface Action<S, E, C> {

    /**
     * perform actions.
     * @param transitionData transition info data
     * @param context transition context
     */
    void execute(TransitionData<S, E> transitionData, C context);
}
