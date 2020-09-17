package net.gittab.statemachine.configurers;

import net.gittab.statemachine.builder.AnnotationConfigurerBuilder;

/**
 * TransitionConfigurer.
 *
 * @author rookiedev 2020/8/26 22:51
 **/
public interface TransitionConfigurer<T, S, E, C> extends AnnotationConfigurerBuilder<StateMachineTransitionConfigurer<S, E, C>> {

//    AbstractTransitionConfigurer<S, E, C> permit(E event, S destination);
//
//    AbstractTransitionConfigurer<S, E, C> permit(E event, S destination, Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard);
//
//    AbstractTransitionConfigurer<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard);
//
//    AbstractTransitionConfigurer<S, E, C> permitIf(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> permitIfElseThrow(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> permitIfElse(E event, S destination, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction);
//
//    AbstractTransitionConfigurer<S, E, C> permitReentry(E event, Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> permitReentryIf(E event, Guard<S, E, C> guard, Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> permitReentryIfElseThrow(E event, Guard<S, E, C> guard, Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> permitReentryIfElse(E event, Guard<S, E, C> guard, Action<S, E, C> ifAction, Action<S, E, C> elseAction);
//
//    AbstractTransitionConfigurer<S, E, C> onEntry(Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> onEntryFrom(E event, Action<S, E, C> action);
//
//    AbstractTransitionConfigurer<S, E, C> onExit(Action<S, E, C> action);

}
