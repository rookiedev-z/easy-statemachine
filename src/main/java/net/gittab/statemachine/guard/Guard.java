package net.gittab.statemachine.guard;

import net.gittab.statemachine.transition.TransitionData;

/**
 * Guard.
 *
 * @author rookiedev
 * @date 2020/8/21 2:40 下午
 **/
@FunctionalInterface
public interface Guard<S, E, C> {

    /**
     * evaluate a guard condition.
     * @return true, if guard evaluation is successful, false otherwise
     */
    boolean evaluate(TransitionData<S, E> transitionData, C context);

}
