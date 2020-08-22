package net.gittab.statemachine.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.event.EventBehaviour;

/**
 * StateRepresentation.
 *
 * @author rookiedev
 * @date 2020/8/21 2:18 下午
 **/
public class StateRepresentation<S, E> {

    private static final String ACTION_IS_NULL = "action is null";

    private static final String BEHAVIOUR_IS_NULL = "behaviour is null";

    public final S state;

    public final Map<E, List<EventBehaviour<S, E>>> stateEventBehaviour = new HashMap<>();

    public final List<Action> entryAction = new ArrayList<>();

    public final List<Action> exitAction = new ArrayList<>();

    public StateRepresentation(S state){
        this.state = state;
    }

    public void entry(){
        this.entryAction.forEach(Action::execute);
    }

    public void exit(){
        this.exitAction.forEach(Action::execute);
    }

    public void addEventBehaviour(EventBehaviour<S, E> eventBehaviour){
        assert eventBehaviour != null : BEHAVIOUR_IS_NULL;
        E event = eventBehaviour.getEvent();
        List<EventBehaviour<S, E>> eventBehaviours = this.stateEventBehaviour.computeIfAbsent(event, k -> new ArrayList<>());
        eventBehaviours.add(eventBehaviour);
    }

    public void addEntryAction(Action action){
        assert action != null : ACTION_IS_NULL;
        this.entryAction.add(action);
    }

    public void addExitAction(Action action){
        assert action != null : ACTION_IS_NULL;
        this.exitAction.add(action);
    }

    public List<EventBehaviour<S, E>> getStateEventBehaviour(E event){
        return this.stateEventBehaviour.get(event);
    }

    public Map<E, List<EventBehaviour<S, E>>> getStateEventBehaviour(){
        return this.stateEventBehaviour;
    }


    public S getState(){
        return this.state;
    }








}
