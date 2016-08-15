package ru.jihor.mapper.builders;

import ru.jihor.mapper.pipelines.BasicPipeline;
import ru.jihor.mapper.pipelines.Pipeline;
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
public class PipelineBuilder<P extends Builder, S, T> implements Builder<P> {
    private final Pipeline pipeline = new BasicPipeline();
    private P parent;

    public PipelineBuilder (P parent) {
        this.parent = parent;
    }

    protected Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    public P getParent() {
        return parent;
    }

    @Override
    public void accept(Pipeline pipeline) {
        throw new UnsupportedOperationException("Direct pipeline nesting is not supported");
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
        getParent().accept(pipeline);
        return getParent();
    }

}
