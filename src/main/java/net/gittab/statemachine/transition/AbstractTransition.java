package net.gittab.statemachine.transition;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;

/**
 * AbstractTransition.
 *
 * @author rookiedev 2020/8/27 00:40
 **/
public abstract class AbstractTransition<S, E, C> implements Transition<S, C>{

    private final TransitionData<S, E> transitionData;
    private final Guard<S, E, C> guard;
    private final Action<S, E, C> ifAction;
    private Action<S, E, C> elseAction;

    protected Boolean isGuardMet = null;

    public AbstractTransition(TransitionData<S, E> transitionData, Guard<S, E, C> guard, Action<S, E, C> ifAction) {
        this.transitionData = transitionData;
        this.guard = guard;
        this.ifAction = ifAction;
    }

    public AbstractTransition(TransitionData<S, E> transitionData, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        this(transitionData, guard, ifAction);
        this.elseAction = elseAction;
    }

    @Override
    public boolean isGuardMet(C context) {
        this.isGuardMet = this.guard.evaluate(this.transitionData, context);
        return isGuardMet;
    }

    @Override
    public void action(C context) {
        if(this.isGuardMet == null){
            isGuardMet(context);
        }
        if(this.isGuardMet){
            this.ifAction.execute(this.transitionData, context);
        }else if(this.elseAction != null){
            this.elseAction.execute(this.transitionData, context);
        }
    }

    @Override
    public S transition(C context) {
        if(this.isGuardMet == null){
            isGuardMet(context);
        }
        if(this.isGuardMet || this.elseAction != null){
            return this.transitionData.getDestination();
        }
        return this.transitionData.getSource();
    }

    /**
     * dose is exist else action.
     * @return true, there exist else action, false otherwise
     */
    public boolean existElseAction(){
        return this.elseAction != null;
    }

    public TransitionData<S, E> getTransitionData(){
        return this.transitionData;
    }
}
