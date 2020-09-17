package net.gittab.statemachine.configurers;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.state.StateRepresentation;
import net.gittab.statemachine.transition.ExternalTransition;
import net.gittab.statemachine.transition.InternalTransition;
import net.gittab.statemachine.transition.TransitionData;

import java.util.Objects;
import java.util.function.Function;

/**
 * StateMachineConfigure.
 *
 * @author rookiedev 2020/8/21 4:01 下午
 **/
public class StateMachineConfigure<S, E, C> {

    public static final String GUARD_IS_NULL = "guard is null";

    public static final String ACTION_IS_NULL = "action is null";

    public static final String IF_ACTION_IS_NULL = "if action is null";

    public static final String ELSE_ACTION_IS_NULL = "else action is null";

    private static final String ENTRY_ACTION_IS_NULL = "entry action is null";

    private static final String EXIT_ACTION_IS_NULL = "exit action is null";

    private static final String EVENT_IS_NULL = "trigger is null";

    public final Guard<S, E, C> DEFAULT_GUARD = (transitionData, context) -> true;

    public final Action<S, E, C> DEFAULT_ACTION = (transitionData, context) -> {};

    private final StateRepresentation<S, E, C> stateRepresentation;

    private final Function<S, StateRepresentation<S, E, C>> lookup;

    public StateMachineConfigure(final StateRepresentation<S, E, C> stateRepresentation, final Function<S, StateRepresentation<S, E, C>> lookup){
        assert stateRepresentation != null : "state representation is null";
        this.stateRepresentation = stateRepresentation;
        this.lookup = lookup;
    }

    public StateMachineConfigure<S, E, C> permit(E event, S destination){
        enforceNotIdentityTransition(destination);
        return publicPermit(event, destination);
    }

    public StateMachineConfigure<S, E, C> permit(E event, S destination, Action<S, E, C> action){
        enforceNotIdentityTransition(destination);
        return publicPermit(event, destination, action);
    }

    public StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard){
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard);
    }

    public StateMachineConfigure<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard){
        enforceNotIdentityTransition(destination);
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, destination, guard, this.DEFAULT_ACTION, throwAction);
    }

    public StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action){
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard, action);
    }

    public StateMachineConfigure<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action){
        enforceNotIdentityTransition(destination);
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, destination, guard, action, throwAction);
    }

    public StateMachineConfigure<S, E, C> permitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction){
        enforceNotIdentityTransition(destination);
        return publicPermitIfElse(event, destination, guard, ifAction, elseAction);
    }

    public StateMachineConfigure<S, E, C> permitInternal(E event, Action<S, E, C> action){
        return publicPermitInternal(event, action);
    }

    public StateMachineConfigure<S, E, C> permitInternalIf(E event, Guard<S, E, C> guard, Action<S, E, C> action){
        return publicPermitInternalIf(event, guard, action);
    }

    public StateMachineConfigure<S, E, C> permitInternalIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action){
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitInternalIfElse(event, guard, action, throwAction);
    }

    public StateMachineConfigure<S, E, C> permitInternalIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction){
        return publicPermitInternalIfElse(event, guard, ifAction, elseAction);
    }

    public StateMachineConfigure<S, E, C> permitReentry(E event, Action<S, E, C> action){
        return publicPermit(event, this.stateRepresentation.getState(), action);
    }

    public StateMachineConfigure<S, E, C> permitReentryIf(E event, Guard<S, E, C> guard, Action<S, E, C> action){
        return publicPermitIf(event, this.stateRepresentation.getState(), guard, action);
    }

    public StateMachineConfigure<S, E, C> permitReentryIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action){
        Action<S, E, C> throwAction = (transitionData, context) -> {
            throw new IllegalStateException(
                    String.format("No valid leaving transitions are permitted from state '%s' for event '%s' with '%s'. Consider ignoring the event.", transitionData.getSource(), transitionData.getEvent(), context)
            );
        };
        return publicPermitIfElse(event, this.stateRepresentation.getState(), guard, action, throwAction);
    }

    public StateMachineConfigure<S, E, C> permitReentryIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction){
        return publicPermitIfElse(event, this.stateRepresentation.getState(), guard, ifAction, elseAction);
    }

    StateMachineConfigure<S, E, C> publicPermitInternal(E event, Action<S, E, C> action){
        return publicPermitInternalIf(event, this.DEFAULT_GUARD, action);
    }

    StateMachineConfigure<S, E, C> publicPermitInternalIf(E event, Guard<S, E, C> guard, Action<S, E, C> action){
        assert guard != null : GUARD_IS_NULL;
        assert action != null : ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), this.stateRepresentation.getState(), event);
        this.stateRepresentation.addEventTransition(new InternalTransition<>(transitionData, guard, action));
        return this;
    }

    StateMachineConfigure<S, E, C> publicPermitInternalIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction){
        assert guard != null : GUARD_IS_NULL;
        assert ifAction != null : IF_ACTION_IS_NULL;
        assert elseAction != null : ELSE_ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), this.stateRepresentation.getState(), event);
        this.stateRepresentation.addEventTransition(new InternalTransition<>(transitionData, guard, ifAction, elseAction));
        return this;
    }

    StateMachineConfigure<S, E, C> publicPermit(E event, S destination){
        return publicPermitIf(event, destination, this.DEFAULT_GUARD, this.DEFAULT_ACTION);
    }

    StateMachineConfigure<S, E, C> publicPermit(E event, S destination, final Action<S, E, C> action){
        return publicPermitIf(event, destination, this.DEFAULT_GUARD, action);
    }

    StateMachineConfigure<S, E, C> publicPermitIf(E event, S destination, Guard<S, E, C> guard){
        return publicPermitIf(event, destination, guard, this.DEFAULT_ACTION);
    }

    StateMachineConfigure<S, E, C> publicPermitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action){
        assert guard != null : GUARD_IS_NULL;
        assert action != null : ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), destination, event);
        this.stateRepresentation.addEventTransition(new ExternalTransition<>(transitionData, guard, action));
        return this;
    }

    StateMachineConfigure<S, E, C> publicPermitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction){
        assert guard != null : GUARD_IS_NULL;
        assert ifAction != null : IF_ACTION_IS_NULL;
        assert elseAction != null : ELSE_ACTION_IS_NULL;
        TransitionData<S, E> transitionData = new TransitionData<>(this.stateRepresentation.getState(), destination, event);
        this.stateRepresentation.addEventTransition(new ExternalTransition<>(transitionData, guard, ifAction, elseAction));
        return this;
    }

//    public StateMachineConfigure<S, E, C> ignore(E event){
//        return ignoreIf(event, this.DEFAULT_GUARD);
//    }
//
//    public StateMachineConfigure<S, E, C> ignoreIf(E event, Guard<S, E, Transition<S, E>, C> guard){
//        Transition<S, E> transitionData = new InternalTransition<>(this.stateRepresentation.getState(), event);
//        this.stateRepresentation.addEventTransition(new InternalTransition<>(transitionData, guard, this.DEFAULT_ACTION));
//        return this;
//    }

    public StateMachineConfigure<S, E, C> onEntry(Action<S, E, C> action){
        assert action != null : ENTRY_ACTION_IS_NULL;
        this.stateRepresentation.addEntryAction(action);
        return this;
    }

    public StateMachineConfigure<S, E, C> onEntryFrom(E event, Action<S, E, C> action){
        assert event != null : EVENT_IS_NULL;
        assert action != null : ENTRY_ACTION_IS_NULL;
        this.stateRepresentation.addEntryActionFrom(event, action);
        return this;
    }

    public StateMachineConfigure<S, E, C> onExit(Action<S, E, C> action){
        assert action != null : EXIT_ACTION_IS_NULL;
        this.stateRepresentation.addExitAction(action);
        return this;
    }

    public StateMachineConfigure<S, E, C> subStateOf(S superstate) {
        StateRepresentation<S, E, C> superRepresentation = this.lookup.apply(superstate);
        this.stateRepresentation.setSuperstate(superRepresentation);
        superRepresentation.addSubState(this.stateRepresentation);
        return this;
    }

    void enforceNotIdentityTransition(S destination) {
        if (Objects.equals(destination, this.stateRepresentation.getState())) {
            throw new IllegalStateException("Permit() (and PermitIf()) require that the destination state is not equal to the source state. To accept a trigger without changing state, use either ignore(), permitInternal() or permitReentry().");
        }
    }

}
