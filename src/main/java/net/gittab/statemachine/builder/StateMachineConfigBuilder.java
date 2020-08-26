package net.gittab.statemachine.builder;

import net.gittab.statemachine.config.StateMachineConfig;

/**
 * StateMachineConfigBuilder.
 *
 * @author rookiedev
 * @date 2020/8/25 22:33
 **/
public class StateMachineConfigBuilder<S, E, C> {

    public StateMachineConfigBuilder() {
    }

    protected StateMachineConfig<S, E, C> config() {
        return new StateMachineConfig<>();
    }
}
