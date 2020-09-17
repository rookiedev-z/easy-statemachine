package net.gittab.statemachine.builder;

import net.gittab.statemachine.StateMachine;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.configurers.ExternalTransitionConfigurer;
import net.gittab.statemachine.configurers.InternalTransitionConfigurer;

/**
 * StateMachineBuilder.
 *
 * @author rookiedev 2020/8/24 15:52
 **/
public class StateMachineBuilder {

    private StateMachineBuilder(){
    }

    public static <S, E, C> Builder<S, E, C> builder() {
        return new Builder<>();
    }

    public static class Builder<S, E, C> {
        private final StateMachineConfig<S, E, C> stateMachineConfig;

        public Builder() {
            StateMachineConfigBuilder.Builder<S, E, C> builder = StateMachineConfigBuilder.builder();
            stateMachineConfig = builder.config();
        }

        public ExternalTransitionConfigurer<S, E, C> externalConfigure(S state){
            return this.stateMachineConfig.externalConfigure(state);
        }

        public InternalTransitionConfigurer<S, E, C> internalConfigure(S state){
            return this.stateMachineConfig.internalConfigure(state);
        }

        public StateMachine<S, E, C> newStateMachine(S initialState) {
            return new StateMachine<>(initialState, stateMachineConfig);
        }
    }

}
