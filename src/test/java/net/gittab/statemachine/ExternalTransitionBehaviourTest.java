package net.gittab.statemachine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.transition.ExternalTransition;
import net.gittab.statemachine.transition.ExternalTransitionBehaviour;
import net.gittab.statemachine.transition.Transition;
import net.gittab.statemachine.transition.TransitionBehaviour;
import org.junit.Before;
import org.junit.Test;

/**
 * ExternalTransitionBehaviourTest.
 *
 * @author rookiedev
 * @date 2020/8/25 2:16 下午
 **/
public class ExternalTransitionBehaviourTest {

    Guard<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> falseGuard;

    Guard<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> trueGuard;

    @Before
    public void initial(){

        falseGuard = (transition, context) -> false;

        trueGuard = (transition, context) -> true;

    }

    @Test
    public void transitionsToDestinationState(){
        Transition<StateEnum, EventEnum> externalTransition = new ExternalTransition<>(StateEnum.A, StateEnum.B, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new ExternalTransitionBehaviour<>(
                externalTransition, null, null);
        assertEquals(StateEnum.B, transitionBehaviour.getTransition().transition());
    }

    @Test
    public void exposesCorrectEvent() {
        Transition<StateEnum, EventEnum> externalTransition = new ExternalTransition<>(StateEnum.A, StateEnum.B, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new ExternalTransitionBehaviour<>(
                externalTransition, null, null);
        assertEquals(EventEnum.X, transitionBehaviour.getTransition().getEvent());
    }

    @Test
    public void whenGuardConditionNull_IsGuardConditionMetIsFalse() {
        Transition<StateEnum, EventEnum> externalTransition = new ExternalTransition<>(StateEnum.A, StateEnum.B, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new ExternalTransitionBehaviour<>(
                externalTransition, null, null);
        assertTrue(transitionBehaviour.isGuardMet(null));
    }

    @Test
    public void whenGuardConditionFalse_IsGuardConditionMetIsFalse() {
        Transition<StateEnum, EventEnum> externalTransition = new ExternalTransition<>(StateEnum.A, StateEnum.B, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new ExternalTransitionBehaviour<>(
                externalTransition, falseGuard, null);
        assertFalse(transitionBehaviour.isGuardMet(null));
    }

    @Test
    public void whenGuardConditionTrue_IsGuardConditionMetIsTrue() {
        Transition<StateEnum, EventEnum> externalTransition = new ExternalTransition<>(StateEnum.A, StateEnum.B, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new ExternalTransitionBehaviour<>(
                externalTransition, trueGuard, null);
        assertTrue(transitionBehaviour.isGuardMet(null));
    }

    @Test
    public void transitionIsInternal(){
        Transition<StateEnum, EventEnum> externalTransition = new ExternalTransition<>(StateEnum.A, StateEnum.B, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new ExternalTransitionBehaviour<>(
                externalTransition, trueGuard, null);
        assertFalse(transitionBehaviour.isInternal());
    }
}
