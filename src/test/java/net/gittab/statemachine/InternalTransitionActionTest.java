package net.gittab.statemachine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import net.gittab.statemachine.transition.Transition;
import org.junit.Test;

/**
 * InternalTransitionActionTest.
 *
 * @author rookiedev
 * @date 2020/8/25 2:40 下午
 **/
public class InternalTransitionActionTest {


    static class StatusAction<S, E, T extends Transition<S, E>, C> implements Action<S, E, T, C> {

        private boolean status;

        public StatusAction(){
            this.status = false;
        }

        public boolean actionExecuted(){
            return this.status;
        }

        @Override
        public void execute(T transition, C context) {
            this.status = true;
        }
    }

    @Test
    public void unguardedActionIsPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitInternal(EventEnum.X, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertTrue(action.actionExecuted());
    }

    @Test
    public void exitAndEntryAreNotPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> entryAction = new StatusAction<>();
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> exitAction = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).onEntry(entryAction).onExit(exitAction).permitInternal(EventEnum.X, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertTrue(action.actionExecuted());
        assertFalse(entryAction.actionExecuted());
        assertFalse(exitAction.actionExecuted());
    }

    @Test
    public void actionWithPositiveGuardIsPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitInternalIf(EventEnum.X, (transition, context) -> true, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertTrue(action.actionExecuted());
    }

    @Test
    public void actionWithNegativeGuardIsNotPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitInternalIf(EventEnum.X, (transition, context) -> false, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertFalse(action.actionExecuted());
    }

    @Test(expected = IllegalStateException.class)
    public void actionWithNegativeGuardIsThrow() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitInternalIfElseThrow(EventEnum.X, (transition, context) -> false, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertFalse(action.actionExecuted());
    }



    @Test(expected = IllegalStateException.class)
    public void multipleInternalActionsWithSameTriggerNotAllowed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action1 = new StatusAction<>();
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action2 = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitInternal(EventEnum.X, action1).permitInternal(EventEnum.X, action2);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);

    }

    @Test
    public void multipleInternalActionsWithDistinctTriggersAreAllowed() {

        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action1 = new StatusAction<>();
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action2 = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitInternal(EventEnum.X, action1).permitInternal(EventEnum.Y, action2);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertTrue(action1.actionExecuted());
        assertFalse(action2.actionExecuted());

        stateMachine.fire(EventEnum.Y);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertTrue(action2.actionExecuted());

    }
}
