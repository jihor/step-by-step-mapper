package ru.jihor.mapper.base;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.builders.ConverterBuilder;
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

    public T convert(S source) {
        T target = targetInitializer.get();
        doConvert(source, target);
        return target;
    }

    public T convert(S source, Supplier<T> initializedTarget){
        T target = initializedTarget.get();
        doConvert(source, target);
        return target;
    }

    private void doConvert(S source, T target) {
        if (pipeline == null) {
            throw new TransformationException("System", "Pipeline not set");
        }
        new Visitor<>(source, target).visit(pipeline);
    }

    public static <S, T> ConverterBuilder<S, T> builder() {
        return new ConverterBuilder<>(new Converter<>());
    }

    public Supplier<T> getTargetInitializer() {
        return targetInitializer;
    }

    public void setTargetInitializer(Supplier<T> targetInitializer) {
        this.targetInitializer = targetInitializer;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }
}
