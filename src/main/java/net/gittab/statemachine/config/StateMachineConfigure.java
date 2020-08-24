package net.gittab.statemachine.config;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.state.StateRepresentation;
import net.gittab.statemachine.transition.*;

import java.util.function.Function;

/**
 * StateMachineConfigure.
 *
 * @author rookiedev
 * @date 2020/8/21 4:01 下午
 **/
public class StateMachineConfigure<S, E, C> {

    public static final String ACTION_IS_NULL = "action is null";

    public static final String GUARD_IS_NULL = "guard is null";

    public final Guard<S, E, Transition<S, E>, C> DEFAULT_GUARD = (transition, context) -> true;

    public final Action<S, E, Transition<S, E>, C> DEFAULT_ACTION = (transition, context) -> {};

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

    public StateMachineConfigure<S, E, C> permit(E event, S destination, Action<S, E, Transition<S, E>, C> action){
        enforceNotIdentityTransition(destination);
        return publicPermit(event, destination, action);
    }

    public StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, Transition<S, E>, C> guard){
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard);
    }

    public StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> action){
        enforceNotIdentityTransition(destination);
        return publicPermitIf(event, destination, guard, action);
    }

    public StateMachineConfigure<S, E, C> permitIfElse(E event, S destination, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> ifAction, Action<S, E, Transition<S, E>, C> elseAction){
        enforceNotIdentityTransition(destination);
        return publicPermitIfElse(event, destination, guard, ifAction, elseAction);
    }

    public StateMachineConfigure<S, E, C> permitInternal(E event, Action<S, E, Transition<S, E>, C> action){
        return publicPermitInternal(event, action);
    }

    public StateMachineConfigure<S, E, C> permitInternalIf(E event, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> action){
        return publicPermitInternalIf(event, guard, action);
    }

    public StateMachineConfigure<S, E, C> permitInternalIfElse(E event, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> ifAction, Action<S, E, Transition<S, E>, C> elseAction){
        return publicPermitInternalIfElse(event, guard, ifAction, elseAction);
    }

    public StateMachineConfigure<S, E, C> permitReentry(E event, Action<S, E, Transition<S, E>, C> action){
        return publicPermit(event, this.stateRepresentation.getState(), action);
    }

    public StateMachineConfigure<S, E, C> permitReentryIf(E event, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> action){
        return publicPermitIf(event, this.stateRepresentation.getState(), guard, action);
    }

    public StateMachineConfigure<S, E, C> permitReentryIfElse(E event, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> ifAction, Action<S, E, Transition<S, E>, C> elseAction){
        return publicPermitIfElse(event, this.stateRepresentation.getState(), guard, ifAction, elseAction);
    }

    StateMachineConfigure<S, E, C> publicPermitInternal(E event, Action<S, E, Transition<S, E>, C> action){
        return publicPermitInternalIf(event, null, action);
    }

    StateMachineConfigure<S, E, C> publicPermitInternalIf(E event, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> action){
        Transition<S, E> transition = new InternalTransition<>(this.stateRepresentation.getState(), event);
        this.stateRepresentation.addEventBehaviour(new InternalTransitionBehaviour<>(transition, guard, action));
        return this;
    }

    StateMachineConfigure<S, E, C> publicPermitInternalIfElse(E event, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> ifAction, Action<S, E, Transition<S, E>, C> elseAction){
        Transition<S, E> transition = new InternalTransition<>(this.stateRepresentation.getState(), event);
        this.stateRepresentation.addEventBehaviour(new InternalTransitionBehaviour<>(transition, guard, ifAction, elseAction));
        return this;
    }

    StateMachineConfigure<S, E, C> publicPermit(E event, S destination){
        return publicPermitIf(event, destination, null, null);
    }

    StateMachineConfigure<S, E, C> publicPermit(E event, S destination, final Action<S, E, Transition<S, E>, C> action){
        return publicPermitIf(event, destination, null, action);
    }

    StateMachineConfigure<S, E, C> publicPermitIf(E event, S destination, Guard<S, E, Transition<S, E>, C> guard){
        return publicPermitIf(event, destination, guard, null);
    }

    StateMachineConfigure<S, E, C> publicPermitIf(E event, S destination, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> action){
        Transition<S, E> transition = new ExternalTransition<>(this.stateRepresentation.getState(), destination, event);
        this.stateRepresentation.addEventBehaviour(new ExternalTransitionBehaviour<>(transition, guard, action));
        return this;
    }

    StateMachineConfigure<S, E, C> publicPermitIfElse(E event, S destination, Guard<S, E, Transition<S, E>, C> guard, Action<S, E, Transition<S, E>, C> ifAction, Action<S, E, Transition<S, E>, C> elseAction){
        Transition<S, E> transition = new ExternalTransition<>(this.stateRepresentation.getState(), destination, event);
        this.stateRepresentation.addEventBehaviour(new ExternalTransitionBehaviour<>(transition, guard, ifAction, elseAction));
        return this;
    }

    public StateMachineConfigure<S, E, C> onEntry(Action<S, E, Transition<S, E>, C> action){
        this.stateRepresentation.addEntryAction(action);
        return this;
    }

    public StateMachineConfigure<S, E, C> onEntryFrom(E event, Action<S, E, Transition<S, E>, C> action){
        this.stateRepresentation.addEntryActionFrom(event, action);
        return this;
    }

    public StateMachineConfigure<S, E, C> onExit(Action<S, E, Transition<S, E>, C> action){
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
        if (destination.equals(this.stateRepresentation.getState())) {
            throw new IllegalStateException("Permit() (and PermitIf()) require that the destination state is not equal to the source state. To accept a trigger without changing state, use either ignore(), permitInternal() or permitReentry().");
        }
    }





}
