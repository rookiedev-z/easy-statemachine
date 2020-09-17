package net.gittab.statemachine.builder;

import net.gittab.statemachine.configurers.ExternalTransitionConfigurer;
import net.gittab.statemachine.configurers.InternalTransitionConfigurer;
import net.gittab.statemachine.configurers.StateMachineTransitionConfigurer;

/**
 * StateMachineTransitionBuilder.
 *
 * @author rookiedev 2020/8/26 22:54
 **/
public class StateMachineTransitionBuilder<S, E, C> implements StateMachineTransitionConfigurer<S, E, C>, AnnotationBuilder<String>{

    @Override
    public ExternalTransitionConfigurer<S, E, C> withExternal() throws Exception {
        return null;
    }

    @Override
    public InternalTransitionConfigurer<S, E, C> withInternal() throws Exception {
        return null;
    }

    @Override
    public String build() throws Exception {
        return null;
    }
}
