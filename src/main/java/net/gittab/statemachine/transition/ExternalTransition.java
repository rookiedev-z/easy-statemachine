package net.gittab.statemachine.transition;

/**
 * ExternalTransition.
 *
 * @author rookiedev
 * @date 2020/8/24 22:09
 **/
public class ExternalTransition<S, E> extends Transition<S, E>{

    public ExternalTransition(S source, S destination, E event) {
        super(source, destination, event);
    }

    @Override
    public S transition() {
        return this.getDestination();
    }
}
