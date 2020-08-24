package net.gittab.statemachine.state;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.transition.Transition;
import net.gittab.statemachine.transition.TransitionBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StateRepresentation.
 *
 * @author rookiedev
 * @date 2020/8/21 2:18 下午
 **/
public class StateRepresentation<S, E, C> {

    private static final String ACTION_IS_NULL = "action is null";

    private static final String BEHAVIOUR_IS_NULL = "behaviour is null";

    public final S state;

    private StateRepresentation<S, E, C> superState;

    private final List<StateRepresentation<S, E, C>> subStates = new ArrayList<>();

    public final Map<E, TransitionBehaviour<S, E, Transition<S, E>, C>> transitionBehaviourMap = new HashMap<>();

    public final List<Action<S, E, Transition<S, E>, C>> entryAction = new ArrayList<>();

    public final List<Action<S, E, Transition<S, E>, C>> exitAction = new ArrayList<>();

    public StateRepresentation(S state){
        this.state = state;
    }

    public TransitionBehaviour<S, E, Transition<S, E>, C> tryFindTransitionBehaviour(E event){
        return this.transitionBehaviourMap.get(event);
    }

    public void entry(Transition<S, E> transition, C context){
        if (transition.isReentry()) {
            executeEntryActions(transition, context);
        } else if (!includes(transition.getSource())) {
            if (this.superState != null) {
                this.superState.entry(transition, context);
            }
            executeEntryActions(transition, context);
        }
    }

    void executeEntryActions(Transition<S, E> transition, C context){
        this.entryAction.forEach(action -> action.execute(transition, context));
    }

    public void exit(Transition<S, E> transition, C context){
        if (transition.isReentry()) {
            executeExitActions(transition, context);
        } else if (!includes(transition.getDestination())) {
            executeExitActions(transition, context);
            if (this.superState != null) {
                this.superState.exit(transition, context);
            }
        }

    }

    void executeExitActions(Transition<S, E> transition, C context){
        this.exitAction.forEach(action -> action.execute(transition, context));
    }

    public void addEventBehaviour(TransitionBehaviour<S, E, Transition<S, E>, C> transitionBehaviour){
        assert transitionBehaviour != null : BEHAVIOUR_IS_NULL;
        this.transitionBehaviourMap.put(transitionBehaviour.getTransition().getEvent(), transitionBehaviour);
    }

    public void addEntryAction(Action<S, E, Transition<S, E>, C> action){
        assert action != null : ACTION_IS_NULL;
        this.entryAction.add(action);
    }

    public void addEntryActionFrom(E event, Action<S, E, Transition<S, E>, C> action){
        assert action != null : ACTION_IS_NULL;
        this.entryAction.add(new Action<S, E, Transition<S, E>, C>() {
            @Override
            public void execute(Transition<S, E> transition, C context) {
                E transitionEvent = transition.getEvent();
                if(transitionEvent != null && transitionEvent.equals(event)){
                    action.execute(transition, context);
                }
            }
        });
    }

    public void addExitAction(Action<S, E, Transition<S, E>, C> action){
        assert action != null : ACTION_IS_NULL;
        this.exitAction.add(action);
    }

    public boolean includes(S stateToCheck) {
        for (StateRepresentation<S, E, C> s : this.subStates) {
            if (s.includes(stateToCheck)) {
                return true;
            }
        }
        return this.state.equals(stateToCheck);
    }

    public TransitionBehaviour<S, E, Transition<S, E>, C> getTransitionBehaviour(E event){
        return this.transitionBehaviourMap.get(event);
    }

    public Map<E, TransitionBehaviour<S, E, Transition<S, E>, C>> getTransitionBehaviourMap(){
        return this.transitionBehaviourMap;
    }

    public void setSuperstate(StateRepresentation<S, E, C> superState) {
        this.superState = superState;
    }

    public void addSubState(StateRepresentation<S, E, C> subState) {
        assert subState != null : "sub state is null";
        this.subStates.add(subState);
    }


    public S getState(){
        return this.state;
    }

}
