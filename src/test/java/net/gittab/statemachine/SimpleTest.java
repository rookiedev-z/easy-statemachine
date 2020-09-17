package net.gittab.statemachine;

import net.gittab.statemachine.builder.StateMachineBuilder;
import net.gittab.statemachine.config.StateMachineConfig;
import net.gittab.statemachine.enums.OrderEvent;
import net.gittab.statemachine.enums.OrderState;
import net.gittab.statemachine.model.OrderContext;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * SimpleTest.
 *
 * @author rookiedev
 * @date 2020/8/24 14:58
 **/
public class SimpleTest {

    Logger logger = Logger.getLogger(SimpleTest.class.getName());

    @Test
    public void simpleTest(){
        simpleTest("StateA", "StateB", "event");
    }

    <S, E> void simpleTest(S source, S destination, E event){
        StateMachineBuilder.Builder<S, E, String> builder =  StateMachineBuilder.builder();
        StateMachineConfig<S, E, Void> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.externalConfigure(source)
                .permit(event, destination, (transition, context) ->{
                    logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
                });
        StateMachine<S, E, Void> stateMachine = new StateMachine<>(source, stateMachineConfig);
        stateMachine.fire(event);
        assertEquals(destination, stateMachine.getState());
    }

    @Test
    public void stateMachineTest(){
        StateMachineConfig<OrderState, OrderEvent, OrderContext> stateMachineConfig = new StateMachineConfig<>();
        stateMachineConfig.externalConfigure(OrderState.TO_PAY)
                .permit(OrderEvent.PAY_SUCCESS, OrderState.PAID,
                        (transition, context) -> SimpleTest.this.logger.info(transition.getSource().name() + " -> " + transition.getDestination().name() +  " on " + transition.getEvent().name() +  " event with " + context))
                .permit(OrderEvent.PAY_FAIL, OrderState.PAID_FAILED,
                        (transition, context) -> SimpleTest.this.logger.info(transition.getSource().name() + " -> " + transition.getDestination().name() +  " on " + transition.getEvent().name() +  " event with " + context));
        StateMachine<OrderState, OrderEvent, OrderContext> stateMachine = new StateMachine<>(OrderState.TO_PAY, stateMachineConfig);
        stateMachine.fire(OrderEvent.PAY_SUCCESS);
        assertEquals(OrderState.PAID, stateMachine.getState());
    }

}
