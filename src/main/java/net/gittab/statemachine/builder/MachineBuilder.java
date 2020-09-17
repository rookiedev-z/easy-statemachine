package net.gittab.statemachine.builder;

import net.gittab.statemachine.configurers.StateMachineTransitionConfigurer;

/**
 * MachineBuilder.
 *
 * @author rookiedev 2020/9/6 00:12
 **/
public class MachineBuilder {

    public static <S, E, C> Builder<S, E, C> builder() {
        return new Builder<>();
    }

    public static class Builder<S, E, C>{
        StateMachineConfigBuilder.Builder<S, E, C> builder;
        StateMachineTransitionBuilder<S, E, C> transitionBuilder;

        public Builder() {
            builder = StateMachineConfigBuilder.builder();
            transitionBuilder = new StateMachineTransitionBuilder<>();

        }

        public StateMachineTransitionConfigurer<S, E, C> configureTransitions() {
            return transitionBuilder;
        }

    }

}
