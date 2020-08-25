package net.gittab.statemachine.transition;

/**
 * TransitionBehaviour.
 *
 * @author rookiedev
 * @date 2020/8/21 2:54 下午
 **/
public abstract class TransitionBehaviour<S, E, T extends Transition<S, E>, C> {

    private final T transition;

    public TransitionBehaviour(T transition){
        this.transition = transition;
    }

    public T getTransition(){
        return this.transition;
    }

    public abstract boolean isInternal();

    public abstract boolean isGuardMet(C context);

    public abstract void action(C context);

    public abstract S transition(C context);
}
