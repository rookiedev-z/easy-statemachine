package net.gittab.statemachine.configurers;

/**
 * ExternalTransitionConfigurer.
 *
 * @author rookiedev
 * @date 2020/8/26 22:50
 **/
public interface ExternalTransitionConfigurer<S, E, C> extends TransitionConfigurer<ExternalTransitionConfigurer<S, E, C>, S, E, C> {

    ExternalTransitionConfigurer<S, E, C> target(S target);
}
