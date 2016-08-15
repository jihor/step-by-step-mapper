package ru.jihor.mapper.builders;

import ru.jihor.mapper.pipeline.Pipeline;
import ru.jihor.mapper.steps.Step;
import ru.jihor.mapper.steps.CheckingTransformationStep;
import ru.jihor.mapper.steps.TransformationStep;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public class PipelineBuilder<P extends AbstractConverterBuilder, S, T> extends AbstractConverterBuilder<P, S, T> {
    private final Pipeline pipeline = new Pipeline();

    protected Pipeline getPipeline() {
        return pipeline;
    }

    public PipelineBuilder () {
        super(null);
    }
    public PipelineBuilder (P parent) {
        super(parent);
    }

    @Override
    protected void accept(Pipeline pipeline) {
        throw new UnsupportedOperationException("Pipeline builder doesn't support nested pipelines directly");
    }

    public PipelineBuilder<P, S, T> step(String id, Step step) {
        pipeline.add(id, step);
        return this;
    }

    public PipelineBuilder<P, S, T> step(String id, BiConsumer<S, T> transformation) {
        pipeline.add(id, new TransformationStep<>(transformation));
        return this;
    }

    // check is considered failed if function returns anything but null
    public PipelineBuilder<P, S, T> step(String id, Function<S, String> check, BiConsumer<S, T> transformation) {
        pipeline.add(id, new CheckingTransformationStep<>(check, transformation));
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
