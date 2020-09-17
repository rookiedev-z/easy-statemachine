package net.gittab.statemachine.configurers;

/**
 * StateMachineTransitionConfigurer.
 *
 * @author rookiedev 2020/8/26 22:50
 **/
public interface StateMachineTransitionConfigurer<S, E, C> {

    ExternalTransitionConfigurer<S, E, C> withExternal() throws Exception;

    InternalTransitionConfigurer<S, E, C> withInternal() throws Exception;
}
