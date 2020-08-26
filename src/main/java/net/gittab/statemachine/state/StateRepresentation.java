package net.gittab.statemachine.state;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.transition.Transition;
import net.gittab.statemachine.transition.TransitionBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public final Map<E, List<TransitionBehaviour<S, E, Transition<S, E>, C>>> transitionBehaviourListMap = new HashMap<>();

    public final List<Action<S, E, Transition<S, E>, C>> entryAction = new ArrayList<>();

    public final List<Action<S, E, Transition<S, E>, C>> exitAction = new ArrayList<>();

    public StateRepresentation(S state){
        this.state = state;
    }

    public TransitionBehaviour<S, E, Transition<S, E>, C> tryFindTransitionBehaviour(E event, C context){
        TransitionBehaviour<S, E, Transition<S, E>, C> result = tryFindLocalTransitionBehaviour(event, context);
        if (result == null && this.superState != null) {
            result = this.superState.tryFindTransitionBehaviour(event, context);
        }
        return result;
    }

    TransitionBehaviour<S, E, Transition<S, E>, C> tryFindLocalTransitionBehaviour(E event, C context){
        List<TransitionBehaviour<S, E, Transition<S, E>, C>> transitionBehaviours = this.transitionBehaviourListMap.get(event);
        if(transitionBehaviours == null || transitionBehaviours.size() == 0){
            return null;
        }

        if(transitionBehaviours.size() == 2){
            TransitionBehaviour<S, E, Transition<S, E>, C> transitionBehaviour1 = transitionBehaviours.get(0);
            TransitionBehaviour<S, E, Transition<S, E>, C> transitionBehaviour2 = transitionBehaviours.get(1);
            boolean guardResult1 = transitionBehaviour1.isGuardMet(context);
            boolean guardResult2 = transitionBehaviour2.isGuardMet(context);
            if(guardResult1 == guardResult2){
                throw new IllegalStateException("Multiple permitted action are configured from state '" + state + "' for event '" + event + "'. Guard clauses must be mutually exclusive.");
            }
            if(guardResult1){
                return transitionBehaviour1;
            }else{
                return transitionBehaviour2;
            }
        }

        if(transitionBehaviours.size() > 1){
            throw new IllegalStateException("Multiple permitted action are configured from state '" + state + "' for event '" + event + "'. Guard clauses must be mutually exclusive.");
        }

        return transitionBehaviours.get(0);
    }

    public Boolean canFire(E event, C context) {
        TransitionBehaviour<S, E, Transition<S, E>, C> transitionBehaviour = tryFindTransitionBehaviour(event, context);
        return transitionBehaviour != null && transitionBehaviour.isGuardMet(context);
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
        E event = transitionBehaviour.getTransition().getEvent();
        List<TransitionBehaviour<S, E, Transition<S, E>, C>> transitionBehaviours =
                this.transitionBehaviourListMap.computeIfAbsent(event, key -> new ArrayList<>());
        transitionBehaviours.add(transitionBehaviour);
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

    public boolean isIncludedIn(S stateToCheck) {
        return this.state.equals(stateToCheck) || (this.superState != null && this.superState.isIncludedIn(stateToCheck));
    }

    public List<E> getPermittedEvents(C context) {
        Set<E> result = new HashSet<>();
        for (E event : transitionBehaviourListMap.keySet()) {
            for (TransitionBehaviour<S, E, Transition<S, E>, C> transitionBehaviour : transitionBehaviourListMap.get(event)) {
                if (transitionBehaviour.isGuardMet(context)) {
                    result.add(event);
                    break;
                }
            }
        }

        if (getSuperState() != null) {
            result.addAll(getSuperState().getPermittedEvents(context));
        }
        return new ArrayList<>(result);
    }

    public List<TransitionBehaviour<S, E, Transition<S, E>, C>> getTransitionBehaviour(E event){
        return this.transitionBehaviourListMap.get(event);
    }

    public Map<E, List<TransitionBehaviour<S, E, Transition<S, E>, C>>> getTransitionBehaviourMap(){
        return this.transitionBehaviourListMap;
    }

    public void setSuperstate(StateRepresentation<S, E, C> superState) {
        this.superState = superState;
    }

    public StateRepresentation<S, E, C> getSuperState() {
        return this.superState;
    }

    public void addSubState(StateRepresentation<S, E, C> subState) {
        assert subState != null : "sub state is null";
        this.subStates.add(subState);
    }


    public S getState(){
        return this.state;
    }

}
