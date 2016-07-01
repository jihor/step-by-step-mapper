package ru.jihor.mapper;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.exceptions.TransformationException;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
public class Converter<S, T> {

    private Pipeline pipeline;
    private Supplier<T> targetInitializer;

    protected T convert(S source) {
        T target = targetInitializer.get();
        doConvert(source, target);
        return target;
    }

    protected T convert(S source, T initializedTarget){
        doConvert(source, initializedTarget);
        return initializedTarget;
    }

    private void doConvert(S source, T target) {
        if (pipeline == null) {
            throw new TransformationException("System", "Pipeline not set");
        }
        new Visitor<>(source, target).visit(pipeline);
    }

    protected static <SourceType, TargetType> ConverterBuilder<SourceType, TargetType> builder() {
        return new ConverterBuilder<>(new Converter<>());
    }

    protected Supplier<T> getTargetInitializer() {
        return targetInitializer;
    }

    protected void setTargetInitializer(Supplier<T> targetInitializer) {
        this.targetInitializer = targetInitializer;
    }

    protected void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }
}
