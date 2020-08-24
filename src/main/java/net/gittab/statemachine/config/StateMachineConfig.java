package net.gittab.statemachine.config;

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

    public StateRepresentation<S, E, C> getStateRepresentation(S source){
        return this.stateRepresentationMap.get(source);
    }

    public StateRepresentation<S, E, C> getOrCreateStateRepresentation(S source){
        return this.stateRepresentationMap.computeIfAbsent(source, StateRepresentation::new);
    }

    public StateMachineConfigure<S, E, C> configure(S source){
        return new StateMachineConfigure<>(getOrCreateStateRepresentation(source), this::getOrCreateStateRepresentation);
    }

}
