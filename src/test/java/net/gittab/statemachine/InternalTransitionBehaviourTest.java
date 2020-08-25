package net.gittab.statemachine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.transition.InternalTransition;
import net.gittab.statemachine.transition.InternalTransitionBehaviour;
import net.gittab.statemachine.transition.Transition;
import net.gittab.statemachine.transition.TransitionBehaviour;
import org.junit.Before;
import org.junit.Test;

/**
 * InternalTransitionBehaviourTest.
 *
 * @author rookiedev
 * @date 2020/8/25 2:16 下午
 **/
public class InternalTransitionBehaviourTest {

    Guard<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> falseGuard;

    Guard<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> trueGuard;

    @Before
    public void initial(){

        falseGuard = (transition, context) -> false;

        trueGuard = (transition, context) -> true;

    }

    @Test
    public void stateUnChanged(){
        Transition<StateEnum, EventEnum> internalTransition = new InternalTransition<>(StateEnum.A, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new InternalTransitionBehaviour<>(
                internalTransition, null, null);
        assertEquals(StateEnum.A, transitionBehaviour.getTransition().transition());
    }

    @Test
    public void exposesCorrectEvent() {
        Transition<StateEnum, EventEnum> internalTransition = new InternalTransition<>(StateEnum.A, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new InternalTransitionBehaviour<>(
                internalTransition, null, null);
        assertEquals(EventEnum.X, transitionBehaviour.getTransition().getEvent());
    }

    @Test
    public void whenGuardConditionNull_IsGuardConditionMetIsFalse() {
        Transition<StateEnum, EventEnum> internalTransition = new InternalTransition<>(StateEnum.A, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new InternalTransitionBehaviour<>(
                internalTransition, null, null);
        assertTrue(transitionBehaviour.isGuardMet(null));
    }

    @Test
    public void whenGuardConditionFalse_IsGuardConditionMetIsFalse() {
        Transition<StateEnum, EventEnum> internalTransition = new InternalTransition<>(StateEnum.A, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new InternalTransitionBehaviour<>(
                internalTransition, falseGuard, null);
        assertFalse(transitionBehaviour.isGuardMet(null));
    }

    @Test
    public void whenGuardConditionTrue_IsGuardConditionMetIsTrue() {
        Transition<StateEnum, EventEnum> internalTransition = new InternalTransition<>(StateEnum.A, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new InternalTransitionBehaviour<>(
                internalTransition, trueGuard, null);
        assertTrue(transitionBehaviour.isGuardMet(null));
    }

    @Test
    public void transitionIsInternal(){
        Transition<StateEnum, EventEnum> internalTransition = new InternalTransition<>(StateEnum.A, EventEnum.X);
        TransitionBehaviour<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> transitionBehaviour = new InternalTransitionBehaviour<>(
                internalTransition, trueGuard, null);
        assertTrue(transitionBehaviour.isInternal());
    }
}
