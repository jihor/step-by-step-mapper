package ru.jihor.mapper.registry;

import ru.jihor.mapper.base.Converter;

import java.util.function.Supplier;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-25
 */
public class QueryBuilder<S> {
    private ConverterRegistry registry;
    private S source;

    public QueryBuilder(QueryableConverterRegistry registry, S source) {
        this.registry = registry;
        this.source = source;
    }

    public <T> T to(Class<T> targetClass) {
        return registry.getDefaultConverter(source, targetClass).convert(source);
    }

    public <T> T to(Supplier<T> initializedTargetSupplier) {
        final T initializedTarget = initializedTargetSupplier.get();
        return to(initializedTarget);
    }

    public <T> T to(T initializedTarget) {
        final Converter<S, T> converter = registry.getDefaultConverter(source, (Class<T>) initializedTarget.getClass());
        return converter.convert(source, initializedTarget);
    }
}
