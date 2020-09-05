package net.gittab.statemachine.builder;

import net.gittab.statemachine.config.StateMachineConfig;

/**
 * StateMachineConfigBuilder.
 *
 * @author rookiedev
 * @date 2020/8/25 22:33
 **/
public class StateMachineConfigBuilder {

    private StateMachineConfigBuilder(){}

    public static <S, E, C> Builder<S, E, C> builder() {
        return new Builder<>();
    }

    public static class Builder<S, E, C> {

        public StateMachineConfig<S, E, C> config(){
            return new StateMachineConfig<>();
        }

    }
}
