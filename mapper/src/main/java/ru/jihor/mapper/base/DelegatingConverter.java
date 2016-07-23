package ru.jihor.mapper.base;

import java.util.function.Supplier;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public abstract class DelegatingConverter<S, T> extends Converter<S, T> {

    private Converter<S, T> delegate;

    public Converter<S, T> getDelegate() {
        return delegate;
    };

    protected abstract Converter<S, T> configureDelegate();

    protected DelegatingConverter() {
        delegate = configureDelegate();
    }

    @Override
    public T convert(S source) {
        return getDelegate().convert(source);
    }

    @Override
    public T convert(S source, Supplier<T> initializedTarget) {
        return getDelegate().convert(source, initializedTarget);
    }
}
