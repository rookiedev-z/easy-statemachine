package net.gittab.statemachine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import net.gittab.statemachine.transition.Transition;
import org.junit.Test;

/**
 * ExternalTransitionActionTest.
 *
 * @author rookiedev
 * @date 2020/8/25 4:13 下午
 **/
public class ExternalTransitionActionTest {

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

    static class CountAction<S, E, T extends Transition<S, E>, C> implements Action<S, E, T, C> {

        private List<Integer> orders;

        private Integer order;

        public CountAction(List<Integer> orders, Integer order){
            this.orders = orders;
            this.order = order;
        }

        @Override
        public void execute(T transition, C context) {
            this.orders.add(this.order);
        }
    }

    @Test
    public void noGuardedActionIsPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permit(EventEnum.X, StateEnum.B, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.B, stateMachine.getState());
        assertTrue(action.actionExecuted());
    }



    @Test
    public void transitionActionIsPerformedBetweenExitAndEntry() {
        List<Integer> orders = new ArrayList<>();
        Action<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> exitAction = new CountAction<>(orders, 1);
        Action<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new CountAction<>(orders, 2);
        Action<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> entryAction = new CountAction<>(orders, 3);

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).onExit(exitAction).permit(EventEnum.X, StateEnum.B, action);
        stateMachineConfig.configure(StateEnum.B).onEntry(entryAction);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.B, stateMachine.getState());
        assertEquals(3, orders.size());
        assertEquals(Integer.valueOf(1), orders.get(0));
        assertEquals(Integer.valueOf(2), orders.get(1));
        assertEquals(Integer.valueOf(3), orders.get(2));
    }

    @Test
    public void stateAndActionWithPositiveGuardIsPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitIf(EventEnum.X, StateEnum.B, (transition, context) -> true, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.B, stateMachine.getState());
        assertTrue(action.actionExecuted());
    }

    @Test
    public void stateAndActionWithNegativeGuardIsNotPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitIf(EventEnum.X, StateEnum.B, (transition, context) -> false, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertFalse(action.actionExecuted());
    }

    @Test(expected = IllegalStateException.class)
    public void actionWithNegativeGuardIsThrow() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitIfElseThrow(EventEnum.X, StateEnum.B, (transition, context) -> false, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertFalse(action.actionExecuted());
    }

    @Test
    public void actionWithPositiveGuardIsPerformed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> positiveAction = new StatusAction<>();
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> negativeAction = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A)
                .permitIf(EventEnum.X, StateEnum.B, (transition, context) -> true, positiveAction)
                .permitIf(EventEnum.X, StateEnum.C, (transition, context) -> false, negativeAction);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.B, stateMachine.getState());
        assertTrue(positiveAction.actionExecuted());
        assertFalse(negativeAction.actionExecuted());
    }

    @Test
    public void noGuardedActionIsPerformedOnReentry() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();
        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A)
                .permitReentry(EventEnum.X, action);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertTrue(action.actionExecuted());
    }

    @Test
    public void reentryActionIsPerformedBetweenExitAndEntry() {
        List<Integer> orders = new ArrayList<>();
        Action<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> exitAction = new CountAction<>(orders, 1);
        Action<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new CountAction<>(orders, 2);
        Action<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> entryAction = new CountAction<>(orders, 3);

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).onEntry(entryAction).onExit(exitAction).permitReentry(EventEnum.X, action);
        // stateMachineConfig.configure(StateEnum.B).onEntry(entryAction);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);
        assertEquals(StateEnum.A, stateMachine.getState());
        assertEquals(3, orders.size());
        assertEquals(Integer.valueOf(1), orders.get(0));
        assertEquals(Integer.valueOf(2), orders.get(1));
        assertEquals(Integer.valueOf(3), orders.get(2));

    }

    @Test
    public void actionWithPositiveGuardIsPerformedOnReentry() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitReentryIf(EventEnum.X, (transition, context) -> true, action);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);

        assertEquals(StateEnum.A, stateMachine.getState());
        assertTrue(action.actionExecuted());
    }

    @Test
    public void actionWithNegativeGuardIsNotPerformedOnReentry() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permitReentryIf(EventEnum.X, (transition, context) -> false, action);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);

        assertEquals(StateEnum.A, stateMachine.getState());
        assertFalse(action.actionExecuted());
    }

    @Test(expected = IllegalStateException.class)
    public void multipleActionsWithSameTriggerNotAllowed() {
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action1 = new StatusAction<>();
        StatusAction<StateEnum, EventEnum, Transition<StateEnum, EventEnum>, String> action2 = new StatusAction<>();

        StateMachineConfig<StateEnum, EventEnum, String> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.A).permit(EventEnum.X, StateEnum.B, action1).permit(EventEnum.X, StateEnum.B, action2);

        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, stateMachineConfig);
        stateMachine.fire(EventEnum.X);

    }
}
