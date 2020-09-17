package net.gittab.statemachine.configurers;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.state.StateRepresentation;

import java.util.function.Function;

/**
 * AbstractTransitionConfigurer.
 *
 * @author rookiedev 2020/8/26 23:15
 **/
public abstract class AbstractTransitionConfigurer<S, E, C> implements TransitionConfigurer<S, E, C>{

    protected static final String GUARD_IS_NULL = "guard is null";

    protected static final String ACTION_IS_NULL = "action is null";

    protected static final String IF_ACTION_IS_NULL = "if action is null";

    protected static final String ELSE_ACTION_IS_NULL = "else action is null";

    protected static final String ENTRY_ACTION_IS_NULL = "entry action is null";

    protected static final String EXIT_ACTION_IS_NULL = "exit action is null";

    protected static final String EVENT_IS_NULL = "trigger is null";

    /**
     * default guard.
     */
    protected final Guard<S, E, C> DEFAULT_GUARD = (transitionData, context) -> true;

    /**
     * default action.
     */
    protected final Action<S, E, C> DEFAULT_ACTION = (transitionData, context) -> {};

    protected final StateRepresentation<S, E, C> stateRepresentation;

    protected final Function<S, StateRepresentation<S, E, C>> lookup;

    public AbstractTransitionConfigurer(final StateRepresentation<S, E, C> stateRepresentation, final Function<S, StateRepresentation<S, E, C>> lookup){
        assert stateRepresentation != null : "state representation is null";
        this.stateRepresentation = stateRepresentation;
        this.lookup = lookup;
    }

    public AbstractTransitionConfigurer<S, E, C> subStateOf(S superstate) {
        StateRepresentation<S, E, C> superRepresentation = this.lookup.apply(superstate);
        this.stateRepresentation.setSuperstate(superRepresentation);
        superRepresentation.addSubState(this.stateRepresentation);
        return this;
    }

}
