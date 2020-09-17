package net.gittab.statemachine.config;

import net.gittab.statemachine.configurers.ExternalTransitionConfigurer;
import net.gittab.statemachine.configurers.InternalTransitionConfigurer;
import net.gittab.statemachine.configurers.StateMachineConfigure;
import net.gittab.statemachine.state.StateRepresentation;

import java.util.HashMap;
import java.util.Map;

/**
 * StateMachineConfig, the state machine config, reusable.
 *
 * @author rookiedev 2020/8/21 1:56 下午
 **/
public class StateMachineConfig<S, E, C> {

    /**
     * state representation map.
     */
    private final Map<S, StateRepresentation<S, E, C>> stateRepresentationMap = new HashMap<>();

    /**
     * return StateRepresentation for the specified state. may return null.
     * @param state the state
     * @return stateRepresentation for the specified state, or null.
     */
    public StateRepresentation<S, E, C> getStateRepresentation(S state){
        return this.stateRepresentationMap.get(state);
    }

    /**
     * return StateRepresentation for the specified state. creates representation if it does not exist.
     * @param state the state
     * @return stateRepresentation for the specified state
     */
    public StateRepresentation<S, E, C> getOrCreateStateRepresentation(S state){
        return this.stateRepresentationMap.computeIfAbsent(state, StateRepresentation::new);
    }


    @Deprecated
    public StateMachineConfigure<S, E, C> configure(S state){
        return new StateMachineConfigure<>(getOrCreateStateRepresentation(state), this::getOrCreateStateRepresentation);
    }

    /**
     * begin configuration of the entry/exit actions and allowed transitions
     * when the state machine is in a particular state.
     * @param state the state to configure
     * @return external transition configurer object through which the state can be configured
     */
    public ExternalTransitionConfigurer<S, E, C> externalConfigure(S state){
        return new ExternalTransitionConfigurer<>(getOrCreateStateRepresentation(state), this::getOrCreateStateRepresentation);
    }

    /**
     * begin configuration of the entry/exit actions and allowed transitions
     * when the state machine is in a particular state.
     * @param state the state to configure
     * @return internal transition configurer object through which the state can be configured
     */
    public InternalTransitionConfigurer<S, E, C> internalConfigure(S state){
        return new InternalTransitionConfigurer<>(getOrCreateStateRepresentation(state), this::getOrCreateStateRepresentation);
    }

}
