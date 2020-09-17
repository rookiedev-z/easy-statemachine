package net.gittab.statemachine.transition;

/**
 * Transition.
 *
 * @author rookiedev 2020/8/26 22:39
 **/
public interface Transition<S, C> {

    /**
     * is it an internal transition.
     * @return true, if internal transition, false otherwise
     */
    boolean isInternal();

    /**
     * dose guard meet conditions.
     * @param context transition context
     * @return true, if guard meet conditions, false otherwise
     */
    boolean isGuardMet(C context);

    /**
     * perform actions, if isGuardMet meet condition, perform action.
     * @param context transition context
     */
    void action(C context);

    /**
     * perform transition, if isGuardMet meet condition, perform transition.
     * @param context transition context
     * @return the destination, if isGuardMet meet condition, source otherwise
     */
    S transition(C context);

}
