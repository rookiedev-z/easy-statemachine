package net.gittab.statemachine;

import net.gittab.statemachine.builder.StateMachineBuilder;
import net.gittab.statemachine.enums.OrderEvent;
import net.gittab.statemachine.enums.OrderState;
import net.gittab.statemachine.model.OrderContext;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * StateMachineBuilderTest.
 *
 * @author rookiedev
 * @date 2020/8/26 22:08
 **/
public class StateMachineBuilderTest {

    Logger logger = Logger.getLogger(StateMachineBuilderTest.class.getName());

    @Test
    public void simpleBuilderTest(){
        simpleTest("StateA", "StateB", "event");
    }

    <S, E> void simpleTest(S source, S destination, E event){
        StateMachineBuilder.Builder<S, E, String> builder =  StateMachineBuilder.builder();
//        builder.externalConfigure(source).permit(event, destination, (transition, context) ->{
//            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
//        });
        builder.internalConfigure(source).permit(event, (transition, context) ->{
            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
        });
        StateMachine<S, E, String> stateMachine = builder.newStateMachine(source);

        stateMachine.fire(event);
        assertEquals(source, stateMachine.getState());
    }

    @Test
    public void builderTest(){
        StateMachineBuilder.Builder<OrderState, OrderEvent, OrderContext> builder =  StateMachineBuilder.builder();

        builder.externalConfigure(OrderState.TO_PAY).permit(OrderEvent.PAY_SUCCESS, OrderState.PAID, (transition, context) ->{
            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
        });

        builder.externalConfigure(OrderState.TO_PAY).permit(OrderEvent.PAY_FAIL, OrderState.PAID_FAILED, (transition, context) ->{
            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
        });

        builder.externalConfigure(OrderState.PAID).permit(OrderEvent.REFUND_SUCCESS, OrderState.REFUNDED, (transition, context) ->{
            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
        });

        builder.internalConfigure(OrderState.PAID).permit(OrderEvent.REFUND_FAIL, (transition, context) ->{
            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
        });

        StateMachine<OrderState, OrderEvent, OrderContext> stateMachine = builder.newStateMachine(OrderState.TO_PAY);

        stateMachine.fire(OrderEvent.PAY_SUCCESS);

        stateMachine.fire(OrderEvent.REFUND_FAIL);

        assertEquals(OrderState.PAID, stateMachine.getState());
    }
}
