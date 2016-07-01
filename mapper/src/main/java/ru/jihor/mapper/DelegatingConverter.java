package ru.jihor.mapper;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public abstract class DelegatingConverter<S, T> extends Converter<S, T> {

    private Converter<S, T> delegate;

    protected Converter<S, T> getDelegate() {
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
    public T convert(S source, T initializedTarget) {
        return getDelegate().convert(source, initializedTarget);
    }
}
