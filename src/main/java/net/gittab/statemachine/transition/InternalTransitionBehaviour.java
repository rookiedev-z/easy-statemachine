package net.gittab.statemachine.transition;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;

/**
 * InternalTransitionBehaviour.
 *
 * @author rookiedev
 * @date 2020/8/24 22:37
 **/
public class InternalTransitionBehaviour<S, E, T extends Transition<S, E>, C> extends TransitionBehaviour<S, E, T, C>{

    private final Guard<S, E, T, C> guard;
    private final Action<S, E, T, C> ifAction;
    private Action<S, E, T, C> elseAction;

    public InternalTransitionBehaviour(T transition, Guard<S, E, T, C> guard, Action<S, E, T, C> ifAction) {
        super(transition);
        this.guard = guard;
        this.ifAction = ifAction;
    }

    public InternalTransitionBehaviour(T transition, Guard<S, E, T, C> guard, Action<S, E, T, C> ifAction, Action<S, E, T, C> elseAction) {
        this(transition, guard, ifAction);
        this.elseAction = elseAction;
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    @Override
    public boolean isGuardMet(C context) {
        if(this.guard == null){
            return true;
        }
        return this.guard.evaluate(this.getTransition(), context);
    }

    @Override
    public void action(C context) {
        if(this.ifAction == null){
            return;
        }
        if(isGuardMet(context)){
            this.ifAction.execute(this.getTransition(), context);
        }else if(this.elseAction != null){
            this.elseAction.execute(this.getTransition(), context);
        }
    }

    @Override
    public S transition(C context) {
        return this.getTransition().transition();
    }
}
