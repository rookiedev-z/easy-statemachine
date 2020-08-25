package net.gittab.statemachine.config;

import net.gittab.statemachine.StateMachine;

/**
 * StateMachineBuilder.
 *
 * @author rookiedev
 * @date 2020/8/24 15:52
 **/
public class StateMachineBuilder {

    public StateMachineBuilder(){
    }

    public static <S, E, C> Builder<S, E, C> builder() {
        return new Builder();
    }

    public static class Builder<S, E, C> {
        private StateMachineConfigBuilder builder = new StateMachineConfigBuilder();
        private StateMachineConfig<S, E, C> stateMachineConfig;


        public Builder() {

        }

        public StateMachineConfigure<S, E, C> configure(S state){
            return this.stateMachineConfig.configure(state);
        }

        public StateMachine<S, E, C> newStateMachine(S source) {
            this.stateMachineConfig = this.builder.config();
            return new StateMachine<>(source, stateMachineConfig);
        }
    }



}
