package net.gittab.statemachine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.gittab.statemachine.transition.ExternalTransition;
import net.gittab.statemachine.transition.InternalTransition;
import org.junit.Test;

/**
 * TransitionTest.
 *
 * @author rookiedev
 * @date 2020/8/25 1:46 下午
 **/
public class TransitionTest {

    @Test
    public void internalTransitionTest(){
        assertTrue(internalReentryTest("source", "event"));
    }

    <S, E> boolean internalReentryTest(S source, E event){
        InternalTransition<S, E> internalTransition = new InternalTransition<>(source, event);
        return internalTransition.isReentry();
    }

    @Test
    public void externalTransitionTest(){
        assertFalse(externalReentryTest("source", "destination","event"));
    }

    <S, E> boolean externalReentryTest(S source, S destination, E event){
        ExternalTransition<S, E> externalTransition = new ExternalTransition<>(source, destination, event);
        return externalTransition.isReentry();
    }
}
