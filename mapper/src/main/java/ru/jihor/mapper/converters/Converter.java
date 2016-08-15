package ru.jihor.mapper.converters;

import ru.jihor.mapper.pipeline.Pipeline;
import ru.jihor.mapper.visitors.Visitor;

import java.util.function.Supplier;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 15.08.2016
 */
public interface Converter<S, T> {
    T convert(S source);

    T convert(S source, Supplier<T> initializedTargetSupplier);

    T convert(S source, T initializedTarget);

    void setPipeline(Pipeline pipeline);

    void setVisitorSupplier(Supplier<Visitor<S, T>> visitorSupplier);

    void setTargetInitializer(Supplier<T> targetInitializer);
}
