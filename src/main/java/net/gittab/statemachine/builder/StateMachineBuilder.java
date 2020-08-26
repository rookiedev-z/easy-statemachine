package net.gittab.statemachine.builder;

import net.gittab.statemachine.StateMachine;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.configurers.StateMachineConfigure;

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

        public StateMachineConfigure<S, E, C> and(S state){
            return this.stateMachineConfig.configure(state);
        }

        public StateMachine<S, E, C> newStateMachine(S initialState) {
            this.stateMachineConfig = this.builder.config();
            return new StateMachine<>(initialState, stateMachineConfig);
        }
    }



}
