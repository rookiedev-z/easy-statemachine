package net.gittab.statemachine.adapter;

import net.gittab.statemachine.builder.AnnotationBuilder;
import net.gittab.statemachine.configurers.AnnotationConfigurer;

/**
 * AnnotationConfigurerAdapter.
 *
 * @author rookiedev
 * @date 2020/8/26 23:18
 **/
public abstract class AnnotationConfigurerAdapter<O,I,B extends AnnotationBuilder<O>>
        implements AnnotationConfigurer<O,B> {

    private B builder;

    @Override
    public void init(B builder) throws Exception {}

    @Override
    public void configure(B builder) throws Exception {}

    public I and() {
        // we're either casting to itself or its interface
        return (I) getBuilder();
    }

    protected final B getBuilder() {
        if(builder == null) {
            throw new IllegalStateException("annotationBuilder cannot be null");
        }
        return builder;
    }
}
