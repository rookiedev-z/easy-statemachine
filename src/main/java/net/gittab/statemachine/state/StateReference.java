package net.gittab.statemachine.state;

/**
 * StateReference.
 *
 * @author rookiedev 2020/8/24 17:40
 **/
public class StateReference<S> {

    private S state;

    public S getState() {
        return state;
    }

    public void setState(S value) {
        state = value;
    }
}
