package net.gittab.statemachine.state;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.transition.AbstractTransition;
import net.gittab.statemachine.transition.TransitionData;

import java.util.*;

/**
 * StateRepresentation.
 *
 * @author rookiedev 2020/8/21 2:18 下午
 **/
public class StateRepresentation<S, E, C> {

    private static final String ACTION_IS_NULL = "action is null";

    private static final String TRANSITION_IS_NULL = "transition is null";

    /**
     * the current state.
     */
    public final S state;

    /**
     * the super state.
     */
    private StateRepresentation<S, E, C> superState;

    /**
     * sub state list of current state.
     */
    private final List<StateRepresentation<S, E, C>> subStates = new ArrayList<>();

    /**
     * transition list map, key is event, value is transition list.
     */
    public final Map<E, List<AbstractTransition<S, E, C>>> transitionListMap = new HashMap<>();

    /**
     * entry action list.
     */
    public final List<Action<S, E, C>> entryAction = new ArrayList<>();

    /**
     * exit action list.
     */
    public final List<Action<S, E, C>> exitAction = new ArrayList<>();

    public StateRepresentation(S state){
        this.state = state;
    }

    /**
     * try find the corresponding transition according to the trigger event.
     * @param event the trigger event
     * @param context transition context
     * @return matched the transition
     */
    public AbstractTransition<S, E, C> tryFindTransition(E event, C context){
        AbstractTransition<S, E, C> result = tryFindLocalTransition(event, context);
        if (result == null && this.superState != null) {
            result = this.superState.tryFindTransition(event, context);
        }
        return result;
    }

    AbstractTransition<S, E, C> tryFindLocalTransition(E event, C context){
        List<AbstractTransition<S, E, C>> transitions = this.transitionListMap.get(event);
        if(transitions == null || transitions.size() == 0){
            return null;
        }

        if(transitions.size() == 2){
            AbstractTransition<S, E, C> transition1 = transitions.get(0);
            AbstractTransition<S, E, C> transition2 = transitions.get(1);
            boolean guardResult1 = transition1.isGuardMet(context);
            boolean guardResult2 = transition2.isGuardMet(context);
            if(guardResult1 == guardResult2){
                throw new IllegalStateException("Multiple permitted action are configured from state '" + state + "' for event '" + event + "'. Guard clauses must be mutually exclusive.");
            }
            if(guardResult1){
                return transition1;
            }else{
                return transition2;
            }
        }

        if(transitions.size() > 1){
            throw new IllegalStateException("Multiple permitted action are configured from state '" + state + "' for event '" + event + "'. Guard clauses must be mutually exclusive.");
        }

        return transitions.get(0);
    }

    /**
     * can the given trigger event be triggered.
     * @param event the trigger event
     * @param context transition context
     * @return true if the given trigger event can trigger, otherwise false
     */
    public Boolean canFire(E event, C context) {
        AbstractTransition<S, E, C> transition = tryFindTransition(event, context);
        return transition != null && transition.isGuardMet(context);
    }


    /**
     * execute entry action with transition context.
     * @param transitionData transition data
     * @param context transition context
     */
    public void entry(TransitionData<S, E> transitionData, C context){
        if (transitionData.isReentry()) {
            executeEntryActions(transitionData, context);
        } else if (!includes(transitionData.getSource())) {
            if (this.superState != null) {
                this.superState.entry(transitionData, context);
            }
            executeEntryActions(transitionData, context);
        }
    }

    void executeEntryActions(TransitionData<S, E> transitionData, C context){
        this.entryAction.forEach(action -> action.execute(transitionData, context));
    }

    /**
     * execute exit action with transition context.
     * @param transitionData transition data
     * @param context transition context
     */
    public void exit(TransitionData<S, E> transitionData, C context){
        if (transitionData.isReentry()) {
            executeExitActions(transitionData, context);
        } else if (!includes(transitionData.getDestination())) {
            executeExitActions(transitionData, context);
            if (this.superState != null) {
                this.superState.exit(transitionData, context);
            }
        }

    }

    void executeExitActions(TransitionData<S, E> transitionData, C context){
        this.exitAction.forEach(action -> action.execute(transitionData, context));
    }

    /**
     * add transition of event trigger.
     * @param transition transition
     */
    public void addEventTransition(AbstractTransition<S, E, C> transition){
        assert transition != null : TRANSITION_IS_NULL;
        E event = transition.getTransitionData().getEvent();
        List<AbstractTransition<S, E, C>> transitions =
                this.transitionListMap.computeIfAbsent(event, key -> new ArrayList<>());
        transitions.add(transition);
    }

    public void addEntryAction(Action<S, E, C> action){
        assert action != null : ACTION_IS_NULL;
        this.entryAction.add(action);
    }

    public void addEntryActionFrom(E event, Action<S, E, C> action){
        assert action != null : ACTION_IS_NULL;
        this.entryAction.add((transition, context) -> {
            E transitionEvent = transition.getEvent();
            if(transitionEvent != null && transitionEvent.equals(event)){
                action.execute(transition, context);
            }
        });
    }

    public void addExitAction(Action<S, E, C> action){
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
        for (E event : transitionListMap.keySet()) {
            for (AbstractTransition<S, E, C> transition : transitionListMap.get(event)) {
                if (transition.isGuardMet(context)) {
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

    public List<AbstractTransition<S, E, C>> getTransition(E event){
        return this.transitionListMap.get(event);
    }

    public Map<E, List<AbstractTransition<S, E, C>>> getTransitionMap(){
        return this.transitionListMap;
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
