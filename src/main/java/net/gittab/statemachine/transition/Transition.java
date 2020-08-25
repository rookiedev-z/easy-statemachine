package net.gittab.statemachine.transition;

/**
 * Transition.
 *
 * @author rookiedev
 * @date 2020/8/24 21:23
 **/
public abstract class Transition<S, E> {

    private final S source;

    private final S destination;

    private final E event;

    public Transition(S source, S destination, E event){
        this.source = source;
        this.destination = destination;
        this.event = event;
    }

    public S getSource(){
        return this.source;
    }

    public S getDestination(){
        return this.destination;
    }

    public E getEvent(){
        return this.event;
    }

    public boolean isReentry(){
        return getSource() != null && getSource().equals(getDestination());
    }

    public abstract S transition();

}