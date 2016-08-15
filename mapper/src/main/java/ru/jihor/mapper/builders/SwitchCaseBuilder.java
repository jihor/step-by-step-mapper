package ru.jihor.mapper.builders;

import ru.jihor.mapper.pipeline.Pipeline;
import ru.jihor.mapper.steps.SwitchCaseStep;

import java.util.function.Predicate;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public class SwitchCaseBuilder<P extends PipelineBuilder, S, T> extends AbstractConverterBuilder<P, S, T> {
    private final String id;

    private final SwitchCaseStep<S, T> step = new SwitchCaseStep<>();

    private Predicate<S> currentCondition;

    public SwitchCaseBuilder(P parent, String id) {
        super(parent);
        this.id = id;
    }

    @Override
    public void accept(Pipeline pipeline) {
        step.addCase(currentCondition, pipeline);
    }

    public PipelineBuilder<SwitchCaseBuilder<P, S, T>, S, T> when(Predicate<S> condition){
        currentCondition = condition;
        return new PipelineBuilder<>(this);
    }

    public PipelineBuilder<SwitchCaseBuilder<P, S, T>, S, T> otherwise(){
        currentCondition = (dummy) -> true;
        return new PipelineBuilder<>(this);
    }

    public P endSwitch() {
        getParent().step(this.id, this.step);
        return getParent();
    }
}
