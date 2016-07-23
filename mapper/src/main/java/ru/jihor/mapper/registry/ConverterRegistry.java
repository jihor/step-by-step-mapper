package ru.jihor.mapper.registry;

import ru.jihor.mapper.base.Converter;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
public class ConverterRegistry {
    private ConcurrentMap<ClassPair, ConcurrentMap<String, Converter>> registry = new ConcurrentHashMap<>();

    public <S, T> void add(ClassPair<S, T> classPair, String converterName, Converter<S, T> converter) {

        /*
        * If no map of converters exists for this class pair, then create new map.
        * If map exists and contains no converter for given name, then add converter to map
        * If map exists and has a converter for given name, then throw an exception.
        *
        * Concurrent map implementation may retry these steps when multiple threads attempt updates
        * including potentially calling the remapping functions multiple times.
        * */
        ConcurrentMap<String, Converter> newMap = new ConcurrentHashMap<>();
        newMap.put(converterName, converter);

        registry.merge(classPair, newMap, (existing, dummy) -> {
            existing.merge(converterName, converter, (a, b) -> {
                throw new IllegalArgumentException(MessageFormat.format("Converter named [{0}] is already registered for {1}",
                                                                        converterName,
                                                                        classPair));
            });
            return existing;
        });

    }

    public <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass) {
        ClassPair classPair = new ClassPair<>(sourceClass, targetClass);

        ConcurrentMap converters = registry.get(classPair);
        if (converters == null) {
            throw new IllegalArgumentException(MessageFormat.format("No converters registered for {0}", classPair));
        }

        if (converters.size() != 1) {
            throw new IllegalArgumentException(MessageFormat.format("More than 1 converter registered for {0}", classPair));
        }

        return (Converter) converters.get(converters.keySet().iterator().next());
    }

    public <S, T> Converter<S, T> getConverterByName(S source, T target, String converterName) {
        ClassPair classPair = new ClassPair<>(source.getClass(), target.getClass());

        ConcurrentMap converters = registry.get(classPair);
        if (converters == null) {
            throw new IllegalArgumentException(MessageFormat.format("No converters registered for {0}", classPair));
        }

        Converter<S, T> converter = (Converter) converters.get(converterName);
        if (converter == null) {
            throw new IllegalArgumentException(MessageFormat.format("No converter named [{0}] registered for {1}",
                                                                    converterName,
                                                                    classPair));
        }

        return converter;
    }
}