package net.gittab.statemachine;

import net.gittab.statemachine.builder.StateMachineBuilder;
import net.gittab.statemachine.config.StateMachineConfig;
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
    public void builderTest(){

    }

    <S, E> void simpleTest(S source, S destination, E event){
        StateMachineBuilder.Builder<S, E, String> builder =  StateMachineBuilder.builder();
        builder.configure(source).permit(event, destination, (transition, context) ->{
            logger.info(transition.getSource() + " -> " + transition.getDestination() +  " on " + transition.getEvent() +  " event with " + context);
        });
        StateMachine<S, E, String> stateMachine = builder.newStateMachine(source);

        StateMachineConfig<S, E, Void> stateMachineConfig = new StateMachineConfig<>();

        stateMachine.fire(event);
        assertEquals(destination, stateMachine.getState());
    }
}
