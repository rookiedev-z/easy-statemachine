package net.gittab.statemachine.event;

import net.gittab.statemachine.guard.Guard;

/**
 * EventBehaviour.
 *
 * @author rookiedev
 * @date 2020/8/21 2:54 下午
 **/
public abstract class EventBehaviour<S, E> {

    private final E event;

    private final Guard guard;

    public EventBehaviour(E event, Guard guard){
        this.event = event;
        this.guard = guard;
    }

    public E getEvent(){
        return this.event;
    }

    public boolean isInternal(){
        return false;
    }

    public boolean isGuardMet(){
        return this.guard.evaluate();
    }

    public abstract void action(Object[] args);

    public abstract S transition(S source, Object[] args);
}
