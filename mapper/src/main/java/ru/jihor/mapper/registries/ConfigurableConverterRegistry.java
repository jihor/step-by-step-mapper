package ru.jihor.mapper.registries;

import ru.jihor.mapper.converters.Converter;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
public abstract class ConfigurableConverterRegistry extends ConverterRegistry {
    private SimpleRegistry delegate;

    // Converters may be added dynamically if needed
    @Override
    public <S, T> void add(ClassPair<S, T> classPair, String converterName, Converter< S, T> converter) {
        ensureInitialization();
        delegate.add(classPair, converterName, converter);
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(S source, T target) {
        ensureInitialization();
        return delegate.getDefaultConverter(source, target);
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(S source, Class<T> targetClass) {
        ensureInitialization();
        return delegate.getDefaultConverter(source, targetClass);
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass) {
        ensureInitialization();
        return delegate.getDefaultConverter(sourceClass, targetClass);
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(ClassPair<S, T> classPair) {
        ensureInitialization();
        return delegate.getDefaultConverter(classPair);
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(S source, T target, String converterName) {
        ensureInitialization();
        return delegate.getConverterByName(source, target, converterName);
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(S source, Class<T> targetClass, String converterName) {
        ensureInitialization();
        return delegate.getConverterByName(source, targetClass, converterName);
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(Class<S> sourceClass, Class<T> targetClass, String converterName) {
        ensureInitialization();
        return delegate.getConverterByName(sourceClass, targetClass, converterName);
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(ClassPair<S, T> classPair, String converterName) {
        ensureInitialization();
        return delegate.getConverterByName(classPair, converterName);
    }

    @Override
    protected Map<ClassPair, ? extends Map<String, Converter>> getRegistry() {
        ensureInitialization();
        return delegate.getRegistry();
    }

    @Override
    public Map<String, Converter> getConvertersForClassPair(ClassPair classPair) {
        ensureInitialization();
        return delegate.getConvertersForClassPair(classPair);
    }

    @Override
    public Map<String, Converter> getConvertersForClassPair(ClassPair classPair, boolean suppressException) {
        ensureInitialization();
        return delegate.getConvertersForClassPair(classPair, suppressException);
    }

    private AtomicBoolean initialized = new AtomicBoolean(false);

    protected void ensureInitialization() {
        while (delegate == null) {
            if (initialized.compareAndSet(false, true)) {
                SimpleRegistry aRegistry = new SimpleRegistry();
                configureRegistry(aRegistry);
                delegate = aRegistry;
            }
        }
    }

    protected abstract void configureRegistry(SimpleRegistry aRegistry);
}
