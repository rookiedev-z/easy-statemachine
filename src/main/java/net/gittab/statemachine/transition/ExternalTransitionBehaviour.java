package net.gittab.statemachine.transition;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;

/**
 * ExternalTransitionBehaviour.
 *
 * @author rookiedev
 * @date 2020/8/24 15:22
 **/
public class ExternalTransitionBehaviour<S, E, T extends Transition<S, E>, C> extends TransitionBehaviour<S, E, T, C> {

    private final Guard<S, E, T, C> guard;
    private final Action<S, E, T, C> ifAction;
    private Action<S, E, T, C> elseAction;

    public ExternalTransitionBehaviour(T transition, Guard<S, E, T, C> guard, Action<S, E, T, C> ifAction) {
        super(transition);
        this.guard = guard;
        this.ifAction = ifAction;
    }

    public ExternalTransitionBehaviour(T transition, Guard<S, E, T, C> guard, Action<S, E, T, C> ifAction, Action<S, E, T, C> elseAction) {
        this(transition, guard, ifAction);
        this.elseAction = elseAction;
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public boolean isGuardMet(C context) {
        return this.guard.evaluate(this.getTransition(), context);
    }

    @Override
    public void action(C context) {
        if(isGuardMet(context)){
            this.ifAction.execute(this.getTransition(), context);
        }else if(this.elseAction != null){
            this.elseAction.execute(this.getTransition(), context);
        }
    }

    @Override
    public S transition(C context) {
        if(isGuardMet(context)){
            return this.getTransition().transition();
        }
        return null;
    }
}
