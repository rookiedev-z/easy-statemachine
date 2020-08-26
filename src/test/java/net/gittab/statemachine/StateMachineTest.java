package net.gittab.statemachine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateMachineTest.
 *
 * @author rookiedev
 * @date 2020/8/26 4:20 下午
 **/
public class StateMachineTest {

    Logger logger = Logger.getLogger(StateMachineTest.class.getName());

    private boolean fired = false;

    @Test
    public void initialStateIsCurrent() {
        StateEnum initial = StateEnum.A;
        StateMachine<StateEnum, EventEnum, Void> stateMachine = new StateMachine<>(initial, new StateMachineConfig<>());
        Assert.assertEquals(initial, stateMachine.getState());
    }

    @Test
    public void subStateIsIncludedInCurrentState() {

        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.S1);
        config.configure(StateEnum.S11).subStateOf(StateEnum.S1);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.S11, config);
        Assert.assertEquals(StateEnum.S11, stateMachine.getState());
        Assert.assertTrue(stateMachine.isInState(StateEnum.S1));

    }

//    @Test
//    public void whenSuperstateIgnoredEvent_SubStateRemainsInSubState() {
//        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
//        config.configure(StateEnum.S1).ignore(EventEnum.EVENT12);
//        config.configure(StateEnum.S11).subStateOf(StateEnum.S1);
//        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.S11, config);
//        stateMachine.fire(EventEnum.EVENT12);
//        Assert.assertEquals(StateEnum.S11, stateMachine.getState());
//    }

    @Test
    public void permittedEventIncludeSuperstatePermittedEvent() {
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.S1).permit(EventEnum.EVENT21, StateEnum.S21);
        config.configure(StateEnum.S21).subStateOf(StateEnum.S2).permit(EventEnum.EVENT2, StateEnum.S1);
        config.configure(StateEnum.S2).permit(EventEnum.EVENT1, StateEnum.S1);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.S21, config);
        List<EventEnum> permittedEvent = stateMachine.getPermittedEvents("");

        assertTrue(permittedEvent.contains(EventEnum.EVENT2));
        assertTrue(permittedEvent.contains(EventEnum.EVENT1));
        assertFalse(permittedEvent.contains(EventEnum.EVENT21));
    }

    @Test
    public void permittedEventAreDistinctValue() {
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.S21).subStateOf(StateEnum.S2).permit(EventEnum.EVENT1, StateEnum.S1);
        config.configure(StateEnum.S2).permit(EventEnum.EVENT1, StateEnum.S21);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.S21, config);
        List<EventEnum> permittedEvent = stateMachine.getPermittedEvents("");

        assertEquals(1, permittedEvent.size());
        assertEquals(EventEnum.EVENT1, permittedEvent.get(0));
    }

    @Test
    public void acceptedEventDependingOnGuard() {
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.A).permitIf(EventEnum.X, StateEnum.B, (transition, context) -> false);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, config);

        List<EventEnum> permittedEvent = stateMachine.getPermittedEvents("");
        assertEquals(0, permittedEvent.size());
    }

    @Test
    public void choosesPermittedTransitionByGuard() {
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.A).permitIf(EventEnum.X, StateEnum.B, (transition, context) -> true);
        config.configure(StateEnum.A).permitIf(EventEnum.X, StateEnum.C, (transition, context) -> false);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, config);
        stateMachine.fire(EventEnum.X);

        assertEquals(StateEnum.B, stateMachine.getState());

    }

    @Test
    public void whenUnknownEventIsFired_UnknownEventActionIsCalled() {
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, config);
        fired = false;
        stateMachine.unknownEventAction((transition, context) -> {
            fired = true;
            logger.info(transition.toString() + context);
        });
        stateMachine.fire(EventEnum.X);
        assertTrue(fired);
    }

    @Test
    public void ifReentryEvent_EntryActionExecuted() {
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.A).permitReentry(EventEnum.X, (transition, context) -> logger.info(transition.toString() + context))
                .onEntry((transition, context) -> fired = true);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, config);
        fired = false;
        stateMachine.fire(EventEnum.X);
        assertTrue(fired);
    }

    @Test(expected = IllegalStateException.class)
    public void implicitReentryIsDisallowed() {
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.A).permit(EventEnum.X, StateEnum.A);
        StateMachine<StateEnum, EventEnum, String> stateMachine = new StateMachine<>(StateEnum.A, config);
    }
}
