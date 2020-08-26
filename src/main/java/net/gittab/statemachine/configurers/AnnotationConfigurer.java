package net.gittab.statemachine.configurers;

import net.gittab.statemachine.builder.AnnotationBuilder;

/**
 * AnnotationConfigurer.
 *
 * @author rookiedev
 * @date 2020/8/26 23:17
 **/
public interface AnnotationConfigurer<O, B extends AnnotationBuilder<O>> {

    void init(B builder) throws Exception;

    void configure(B builder) throws Exception;
}
