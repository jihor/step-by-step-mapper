package ru.jihor.mapper.registry;

import ru.jihor.mapper.base.Converter;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
public class ConverterRegistry {
    protected ConcurrentMap<ClassPair, ConcurrentMap<String, Converter>> getRegistry() {
        return registry;
    }

    protected void setRegistry(ConcurrentMap<ClassPair, ConcurrentMap<String, Converter>> registry) {
        this.registry = registry;
    }

    private ConcurrentMap<ClassPair, ConcurrentMap<String, Converter>> registry = new ConcurrentHashMap<>();

    public <S, T> void add(ClassPair<S, T> classPair, String converterName, Converter<? super S, ? super T> converter) {

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
        ConcurrentMap<String, Converter> converters = getConvertersForClassPair(classPair);
        if (converters.size() != 1) {
            throw new IllegalArgumentException(MessageFormat.format("More than 1 converter registered for {0}", classPair));
        }
        return converters.get(converters.keySet().iterator().next());
    }

    public <S, T> Converter<S, T> getConverterByName(Class<S> sourceClass, Class<T> targetClass, String converterName) {
        ClassPair classPair = new ClassPair<>(sourceClass, targetClass);
        ConcurrentMap<String, Converter> converters = getConvertersForClassPair(classPair);
        Converter converter = converters.get(converterName);
        if (converter == null) {
            throw new IllegalArgumentException(MessageFormat.format("No converter named [{0}] registered for {1}",
                                                                    converterName,
                                                                    classPair));
        }
        return converter;
    }

    private ConcurrentMap<String, Converter> getConvertersForClassPair(ClassPair classPair){
        ConcurrentMap<String, Converter> converters = registry.get(classPair);
        if (converters == null) {
            throw new IllegalArgumentException(MessageFormat.format("No converters registered for {0}", classPair));
        }
        return converters;
    }
}
