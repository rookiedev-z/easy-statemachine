package net.gittab.statemachine.config;

import net.gittab.statemachine.configurers.StateMachineConfigure;
import net.gittab.statemachine.state.StateRepresentation;

import java.util.HashMap;
import java.util.Map;

/**
 * StateMachineConfig.
 *
 * @author rookiedev
 * @date 2020/8/21 1:56 下午
 **/
public class StateMachineConfig<S, E, C> {

    private final Map<S, StateRepresentation<S, E, C>> stateRepresentationMap = new HashMap<>();

    public StateRepresentation<S, E, C> getStateRepresentation(S state){
        return this.stateRepresentationMap.get(state);
    }

    public StateRepresentation<S, E, C> getOrCreateStateRepresentation(S state){
        return this.stateRepresentationMap.computeIfAbsent(state, StateRepresentation::new);
    }

    public StateMachineConfigure<S, E, C> configure(S state){
        return new StateMachineConfigure<>(getOrCreateStateRepresentation(state), this::getOrCreateStateRepresentation);
    }

}
