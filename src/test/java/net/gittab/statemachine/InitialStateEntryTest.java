package net.gittab.statemachine;

import net.gittab.statemachine.action.Action;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import net.gittab.statemachine.transition.TransitionData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * InitialStateEntryTest.
 *
 * @author rookiedev
 * @date 2020/8/25 5:51 下午
 **/
public class InitialStateEntryTest {

    private StateMachine<StateEnum, EventEnum, String> fsm;

    private StatusAction<StateEnum, EventEnum, String> entryS1Action;

    private StatusAction<StateEnum, EventEnum, String> entryS11Action;

    private StatusAction<StateEnum, EventEnum, String> entryS12Action;

    static class StatusAction<S, E, C> implements Action<S, E, C> {

        private boolean status;

        public StatusAction(){
            this.status = false;
        }

        public boolean actionExecuted(){
            return this.status;
        }

        @Override
        public void execute(TransitionData<S, E> transitionData, C context) {
            this.status = true;
        }
    }

    @Before
    public void initial(){
        entryS1Action = new StatusAction<>();
        entryS11Action = new StatusAction<>();
        entryS12Action = new StatusAction<>();
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.externalConfigure(StateEnum.S1).onEntry(entryS1Action);
        config.externalConfigure(StateEnum.S11).subStateOf(StateEnum.S1).onEntry(entryS11Action)
        .permit(EventEnum.EVENT11, StateEnum.S12);
        config.externalConfigure(StateEnum.S12).subStateOf(StateEnum.S1).onEntry(entryS12Action)
                .permit(EventEnum.EVENT12, StateEnum.S11);
        fsm = new StateMachine<>(StateEnum.S11, config);
    }

    @Test
    public void initialStateActionsNotExecutedByDefault() {

        // state machine is configured with initial state
        // initial state is a sub state of parent
        // state machine is not started yet
        Assert.assertTrue(fsm.isInState(StateEnum.S1));
        Assert.assertTrue(fsm.isInState(StateEnum.S11));

        // state machine handles transition
        fsm.fire(EventEnum.EVENT11);

        // transition is performed
        // initial state entry actions are not called
        // destination state entry actions are called
        Assert.assertFalse(entryS1Action.actionExecuted());
        Assert.assertFalse(entryS11Action.actionExecuted());
        Assert.assertTrue(entryS12Action.actionExecuted());

    }

    @Test
    public void initialStateTransition(){
        // state machine is configured with initial state
        // initial state is a sub state of parent
        // state machine is not started yet
        Assert.assertTrue(fsm.isInState(StateEnum.S1));
        Assert.assertTrue(fsm.isInState(StateEnum.S11));

        // initial transition is fired
        fsm.fireInitialTransition();

        // state machine enters super state
        // state machine enters inner state (initial)
        Assert.assertTrue(entryS1Action.actionExecuted());
        Assert.assertTrue(entryS11Action.actionExecuted());
        Assert.assertFalse(entryS12Action.actionExecuted());
    }

    @Test(expected = IllegalStateException.class)
    public void initialStateTransitionCanBeTriggeredOnlyOnce() {
        // state machine is configured with initial state
        // initial state is a sub state of parent
        // state machine initial transition has been called
        Assert.assertTrue(fsm.isInState(StateEnum.S1));
        Assert.assertTrue(fsm.isInState(StateEnum.S11));

        // initial transition is fired
        fsm.fireInitialTransition();

        // initial transition is fired 2nd time
        fsm.fireInitialTransition();

        // exception is thrown
    }

    @Test(expected = IllegalStateException.class)
    public void initialStateTransitionCanBeTriggeredBeforeAnyTrigger() {
        // state machine is configured with initial state
        // initial state is a sub state of parent
        // state machine normal transition has been performed
        Assert.assertTrue(fsm.isInState(StateEnum.S1));
        Assert.assertTrue(fsm.isInState(StateEnum.S11));

        // state machine handles transition
        fsm.fire(EventEnum.EVENT11);
        Assert.assertTrue(fsm.isInState(StateEnum.S12));

        // initial transition is fired after state machine has been started
        fsm.fireInitialTransition();

        // exception is thrown
    }
}

