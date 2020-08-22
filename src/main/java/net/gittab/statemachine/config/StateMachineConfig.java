package net.gittab.statemachine.config;

import java.util.HashMap;
import java.util.Map;

import net.gittab.statemachine.event.EventWithParam;
import net.gittab.statemachine.state.StateRepresentation;

/**
 * StateMachineConfig.
 *
 * @author rookiedev
 * @date 2020/8/21 1:56 下午
 **/
public class StateMachineConfig<S, E> {

    private final Map<S, StateRepresentation<S, E>> stateRepresentationMap = new HashMap<>();
    private final Map<E, EventWithParam<E>> eventWithParamMap = new HashMap<>();

    public StateRepresentation<S, E> getStateRepresentation(S state){
        return this.stateRepresentationMap.get(state);
    }

    public StateRepresentation<S, E> getOrCreateStateRepresentation(S state){
        StateRepresentation<S, E> stateRepresentation = this.stateRepresentationMap.get(state);
        if(stateRepresentation == null){
            stateRepresentation = new StateRepresentation<>(state);
            this.stateRepresentationMap.put(state, stateRepresentation);
        }
        return stateRepresentation;
    }




}
