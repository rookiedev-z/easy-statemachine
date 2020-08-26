package net.gittab.statemachine;

import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import net.gittab.statemachine.guard.Guard;
import net.gittab.statemachine.transition.AbstractTransition;
import net.gittab.statemachine.transition.ExternalTransition;
import net.gittab.statemachine.transition.TransitionData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ExternalTransitionTest.
 *
 * @author rookiedev
 * @date 2020/8/25 2:16 下午
 **/
public class ExternalTransitionTest {

    Guard<StateEnum, EventEnum, String> falseGuard;

    Guard<StateEnum, EventEnum, String> trueGuard;

    @Before
    public void initial(){

        falseGuard = (transition, context) -> false;

        trueGuard = (transition, context) -> true;

    }

    @Test
    public void transitionsToDestinationState(){
        TransitionData<StateEnum, EventEnum> transitionData = new TransitionData<>(StateEnum.A, StateEnum.B, EventEnum.X);
        AbstractTransition<StateEnum, EventEnum, String> transition = new ExternalTransition<>(
                transitionData, trueGuard, (tran, context) -> {});
        assertEquals(StateEnum.B, transition.transition(""));
    }

    @Test
    public void exposesCorrectEvent() {
        TransitionData<StateEnum, EventEnum> transitionData = new TransitionData<>(StateEnum.A, StateEnum.B, EventEnum.X);
        AbstractTransition<StateEnum, EventEnum, String> transition = new ExternalTransition<>(
                transitionData, trueGuard, null);
        assertEquals(EventEnum.X, transition.getTransitionData().getEvent());
    }

    @Test
    public void whenGuardConditionFalse_IsGuardConditionMetIsFalse() {
        TransitionData<StateEnum, EventEnum> transitionData = new TransitionData<>(StateEnum.A, StateEnum.B, EventEnum.X);
        AbstractTransition<StateEnum, EventEnum, String> transition = new ExternalTransition<>(
                transitionData, falseGuard, null);
        assertFalse(transition.isGuardMet(null));
    }

    @Test
    public void whenGuardConditionTrue_IsGuardConditionMetIsTrue() {
        TransitionData<StateEnum, EventEnum> transitionData = new TransitionData<>(StateEnum.A, StateEnum.B, EventEnum.X);
        AbstractTransition<StateEnum, EventEnum, String> transition = new ExternalTransition<>(
                transitionData, trueGuard, null);
        assertTrue(transition.isGuardMet(null));
    }

    @Test
    public void transitionIsInternal(){
        TransitionData<StateEnum, EventEnum> transitionData = new TransitionData<>(StateEnum.A, StateEnum.B, EventEnum.X);
        AbstractTransition<StateEnum, EventEnum, String> transition = new ExternalTransition<>(
                transitionData, trueGuard, null);
        assertFalse(transition.isInternal());
    }
}
