package net.gittab.statemachine;

import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.model.OrderContext;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * SimpleTest.
 *
 * @author rookiedev
 * @date 2020/8/24 14:58
 **/
public class SimpleTest {

    Logger logger = Logger.getLogger(SimpleTest.class.getName());

    @Test
    public void externalTransitionTest(){
        StateMachineConfig<StateEnum, EventEnum, OrderContext> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.TO_PAY)
                .permit(EventEnum.PAY_SUCCESS, StateEnum.PAID,
                        (transition, context) -> SimpleTest.this.logger.info(transition.getSource().name() + " -> " + transition.getDestination().name() +  " on " + transition.getEvent().name() +  " event with " + context))
                .permit(EventEnum.PAY_FAIL, StateEnum.PAID_FAILED,
                        (transition, context) -> SimpleTest.this.logger.info(transition.getSource().name() + " -> " + transition.getDestination().name() +  " on " + transition.getEvent().name() +  " event with " + context));
        StateMachine<StateEnum, EventEnum, OrderContext> stateMachine = new StateMachine<>(StateEnum.TO_PAY, stateMachineConfig);
        stateMachine.fire(EventEnum.PAY_SUCCESS);
        assert StateEnum.PAID.equals(stateMachine.getState());
    }

    @Test
    public void internalTransitionTest(){
        StateMachineConfig<StateEnum, EventEnum, OrderContext> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.configure(StateEnum.TO_PAY)
                .permit(EventEnum.PAY_SUCCESS, StateEnum.PAID,
                        (transition, context) -> SimpleTest.this.logger.info(transition.getSource().name() + " -> " + transition.getDestination().name() +  " on " + transition.getEvent().name() +  " event with " + context))
                .permit(EventEnum.PAY_FAIL, StateEnum.PAID_FAILED,
                        (transition, context) -> SimpleTest.this.logger.info(transition.getSource().name() + " -> " + transition.getDestination().name() +  " on " + transition.getEvent().name() +  " event with " + context));
        StateMachine<StateEnum, EventEnum, OrderContext> stateMachine = new StateMachine<>(StateEnum.TO_PAY, stateMachineConfig);
        stateMachine.fire(EventEnum.PAY_SUCCESS);
        assert StateEnum.PAID.equals(stateMachine.getState());
    }
}
