package ru.jihor.mapper.registry;

import ru.jihor.mapper.base.Converter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
public abstract class ConfigurableConverterRegistry extends ConverterRegistry {
    private ConverterRegistry delegate;

    // Converters may be added dynamically if needed
    @Override
    public <S, T> void add(ClassPair<S, T> classPair, String converterName, Converter<? super S, ? super T> converter) {
        ensureInitialization();
        delegate.add(classPair, converterName, converter);
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass) {
        ensureInitialization();
        return delegate.getDefaultConverter(sourceClass, targetClass);
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(Class<S> sourceClass, Class<T> targetClass, String converterName) {
        ensureInitialization();
        return delegate.getConverterByName(sourceClass, targetClass, converterName);
    }

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private void ensureInitialization() {
        while (delegate == null) {
            if (initialized.compareAndSet(false, true)) {
                ConverterRegistry aRegistry = new ConverterRegistry();
                configureRegistry(aRegistry);
                this.delegate = aRegistry;
            }
        }
    }

    protected abstract void configureRegistry(ConverterRegistry aRegistry);
}
