package net.gittab.statemachine;

import java.util.logging.Logger;

import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import org.junit.Before;
import org.junit.Test;

/**
 * InitialStateEntryTest.
 *
 * @author rookiedev
 * @date 2020/8/25 5:51 下午
 **/
public class InitialStateEntryTest {

    Logger logger = Logger.getLogger(SimpleTest.class.getName());

    private StateMachine<StateEnum, EventEnum, String> fsm;

    @Before
    public void initial(){
        StateMachineConfig<StateEnum, EventEnum, String> config = new StateMachineConfig<>();
        config.configure(StateEnum.S1).onEntry((transition, context) -> logger.info("entry " + transition.getDestination() + " state"));
        config.configure(StateEnum.S11).subStateOf(StateEnum.S1).onEntry((transition, context) -> logger.info("entry " + transition.getDestination() + " state"))
        .permit(EventEnum.EVENT11, StateEnum.S12);
        config.configure(StateEnum.S12).subStateOf(StateEnum.S1).onEntry((transition, context) -> logger.info("entry " + transition.getDestination() + " state"))
                .permit(EventEnum.EVENT12, StateEnum.S11);
        fsm = new StateMachine<>(StateEnum.S11, config);
    }

    @Test
    public void initialStateActionsNotExecutedByDefault() {

        fsm.isInState(StateEnum.S1);
        fsm.isInState(StateEnum.S11);

        fsm.fire(EventEnum.EVENT11);

    }
}

