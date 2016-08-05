package ru.jihor.mapper.base;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.builders.ConverterBuilder;
import ru.jihor.mapper.exceptions.TransformationException;
import ru.jihor.mapper.visitors.DefaultVisitor;

import java.util.function.Supplier;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
public class Converter<S, T> {

    public static <S, T> ConverterBuilder<S, T> builder() {
        return new ConverterBuilder<>(new Converter<>());
    }

    public T convert(S source) {
        T target = targetInitializer.get();
        doConvert(source, target);
        return target;
    }

    public T convert(S source, Supplier<T> initializedTargetSupplier){
        T target = initializedTargetSupplier.get();
        doConvert(source, target);
        return target;
    }

    public T convert(S source, T initializedTarget){
        doConvert(source, initializedTarget);
        return initializedTarget;
    }

    private void doConvert(S source, T target) {
        if (pipeline == null) {
            throw new TransformationException("System", "Pipeline not set");
        }
        Visitor<S, T> visitor = getVisitorSupplier().get();
        visitor.setSource(source);
        visitor.setTarget(target);
        visitor.visit(pipeline);
    }

    private Pipeline pipeline;

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    private Supplier<Visitor<S, T>> visitorSupplier = DefaultVisitor::new;

    public Supplier<Visitor<S, T>> getVisitorSupplier() {
        return visitorSupplier;
    }

    public void setVisitorSupplier(Supplier<Visitor<S, T>> visitorSupplier) {
        this.visitorSupplier = visitorSupplier;
    }

    private Supplier<T> targetInitializer;

    public void setTargetInitializer(Supplier<T> targetInitializer) {
        this.targetInitializer = targetInitializer;
    }

    public Supplier<T> getTargetInitializer() {
        return targetInitializer;
    }
}
