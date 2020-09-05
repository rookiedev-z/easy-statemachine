package net.gittab.statemachine.builder;

/**
 * AnnotationBuilder.
 *
 * @author rookiedev
 * @date 2020/8/26 23:17
 **/
public interface AnnotationBuilder<O> {

    O build() throws Exception;
}