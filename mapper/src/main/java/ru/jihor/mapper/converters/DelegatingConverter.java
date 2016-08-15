package ru.jihor.mapper.converters;

import ru.jihor.mapper.pipelines.Pipeline;
import ru.jihor.mapper.visitors.Visitor;

import java.util.function.Supplier;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 2016-07-01
 */
public abstract class DelegatingConverter<S, T> implements Converter<S, T> {

    private Converter<S, T> delegate;

    private Converter<S, T> getDelegate() {
        return delegate;
    }

    protected abstract Converter<S, T> configureDelegate();

    protected DelegatingConverter() {
        delegate = configureDelegate();
    }

    @Override
    public T convert(S source) {
        return getDelegate().convert(source);
    }

    @Override
    public T convert(S source, Supplier<T> initializedTargetSupplier) {
        return getDelegate().convert(source, initializedTargetSupplier);
    }

    @Override
    public T convert(S source, T initializedTarget) {
        return getDelegate().convert(source, initializedTarget);
    }

    @Override
    public void setPipeline(Pipeline pipeline) {
        getDelegate().setPipeline(pipeline);
    }

    @Override
    public void setVisitorSupplier(Supplier<Visitor<S, T>> visitorSupplier) {
        getDelegate().setVisitorSupplier(visitorSupplier);
    }

    @Override
    public void setTargetInitializer(Supplier<T> targetInitializer) {
        getDelegate().setTargetInitializer(targetInitializer);
    }
}
