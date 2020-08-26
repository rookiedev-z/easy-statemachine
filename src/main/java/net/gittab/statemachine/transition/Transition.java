package net.gittab.statemachine.transition;

/**
 * Transition.
 *
 * @author rookiedev
 * @date 2020/8/26 22:39
 **/
public interface Transition<S, C> {

    boolean isInternal();

    boolean isGuardMet(C context);

    void action(C context);

    S transition(C context);

}
