package net.gittab.statemachine.guard;

import net.gittab.statemachine.transition.Transition;

/**
 * Guard.
 *
 * @author rookiedev
 * @date 2020/8/21 2:40 下午
 **/
@FunctionalInterface
public interface Guard<S, E, T extends Transition<S, E>, C> {

    /**
     * evaluate a guard condition.
     * @return true, if guard evaluation is successful, false otherwise
     */
    boolean evaluate(T transition, C context);

}
