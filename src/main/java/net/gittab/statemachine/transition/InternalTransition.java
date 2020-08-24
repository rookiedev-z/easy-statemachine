package net.gittab.statemachine.transition;

/**
 * InternalTransition.
 *
 * @author rookiedev
 * @date 2020/8/24 22:35
 **/
public class InternalTransition<S, E> extends Transition<S, E> {

    public InternalTransition(S source, E event) {
        super(source, source, event);
    }

    @Override
    public S transition() {
        return this.getSource();
    }
}
