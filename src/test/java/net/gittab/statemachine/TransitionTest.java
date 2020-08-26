package net.gittab.statemachine;

import net.gittab.statemachine.transition.TransitionData;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        TransitionData<S, E> internalTransition = new TransitionData<>(source, source, event);
        return internalTransition.isReentry();
    }

    @Test
    public void externalTransitionTest(){
        assertFalse(externalReentryTest("source", "destination","event"));
    }

    <S, E> boolean externalReentryTest(S source, S destination, E event){
        TransitionData<S, E> externalTransition = new TransitionData<>(source, destination, event);
        return externalTransition.isReentry();
    }
}
