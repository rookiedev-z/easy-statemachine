package net.gittab.statemachine.configurers;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.guard.Guard;

/**
 * DefaultInternalTransitionConfigurer.
 *
 * @author rookiedev
 * @date 2020/8/26 23:27
 **/
public class DefaultInternalTransitionConfigurer<S, E, C> implements InternalTransitionConfigurer<S, E, C>{

    @Override
    public StateMachineConfigure<S, E, C> permit(E event, S destination) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitReentry(E event, Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitReentryIf(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitReentryIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> permitReentryIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> onEntry(Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> onEntryFrom(E event, Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> onExit(Action<S, E, C> action) {
        return null;
    }

    @Override
    public StateMachineConfigure<S, E, C> subStateOf(S superstate) {
        return null;
    }

    @Override
    public StateMachineTransitionConfigurer<S, E, C> and() {
        return null;
    }
}
