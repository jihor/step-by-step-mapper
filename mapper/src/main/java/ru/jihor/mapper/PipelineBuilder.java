package ru.jihor.mapper;

import java.util.function.BiConsumer;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public class PipelineBuilder<P extends AbstractConverterBuilder, S, T> extends AbstractConverterBuilder<P, S, T> {
    private final Pipeline pipeline = new Pipeline();

    public Pipeline getPipeline() {
        return pipeline;
    }

    protected PipelineBuilder () {
        super(null);
    }
    protected PipelineBuilder (P parent) {
        super(parent);
    }

    @Override
    protected void accept(Pipeline pipeline) {
        throw new UnsupportedOperationException("Pipeline builder doesn't support nested pipelines");
    }

    protected PipelineBuilder<P, S, T> step(String id, Step step) {
        pipeline.add(id, step);
        return this;
    }

    public PipelineBuilder<P, S, T> step(String id, BiConsumer<S, T> transformation) {
        pipeline.add(id, new TransformationStep<>(transformation));
        return this;
    }

    public SwitchCaseBuilder<PipelineBuilder<P, S, T>, S, T> switchCase(String id){
        return new SwitchCaseBuilder<>(this, id);
    }

    public P end(){
        if (getParent() != null){
            getParent().accept(pipeline);
            return getParent();
        } else {
            return (P)this;
        }
    }

}
