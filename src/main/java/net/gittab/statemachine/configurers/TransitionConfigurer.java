package net.gittab.statemachine.configurers;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.builder.AnnotationConfigurerBuilder;
import net.gittab.statemachine.guard.Guard;

/**
 * TransitionConfigurer.
 *
 * @author rookiedev
 * @date 2020/8/26 22:51
 **/
public interface TransitionConfigurer<T, S, E, C> extends AnnotationConfigurerBuilder<StateMachineTransitionConfigurer<S, E, C>> {

    StateMachineConfigure<S, E, C> permit(E event, S destination);

    StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard);

    StateMachineConfigure<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard);

    StateMachineConfigure<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action);

    StateMachineConfigure<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action);

    StateMachineConfigure<S, E, C> permitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction);

    StateMachineConfigure<S, E, C> permitReentry(E event, Action<S, E, C> action);

    StateMachineConfigure<S, E, C> permitReentryIf(E event, Guard<S, E, C> guard, Action<S, E, C> action);

    StateMachineConfigure<S, E, C> permitReentryIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action);

    StateMachineConfigure<S, E, C> permitReentryIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction);

    StateMachineConfigure<S, E, C> onEntry(Action<S, E, C> action);

    StateMachineConfigure<S, E, C> onEntryFrom(E event, Action<S, E, C> action);

    StateMachineConfigure<S, E, C> onExit(Action<S, E, C> action);

    StateMachineConfigure<S, E, C> subStateOf(S superstate);


}
