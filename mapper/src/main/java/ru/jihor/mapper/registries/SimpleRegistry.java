package ru.jihor.mapper.registries;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.converters.Converter;
import ru.jihor.mapper.exceptions.RegistryException;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
@Slf4j
public class SimpleRegistry extends ConverterRegistry {
    protected ConcurrentMap<ClassPair, ConcurrentMap<String, Converter>> getRegistry() {
        return registry;
    }

    private ConcurrentMap<ClassPair, ConcurrentMap<String, Converter>> registry = new ConcurrentHashMap<>();

    @Override
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
                throw new RegistryException(MessageFormat.format("Converter named [{0}] is already registered for {1}",
                                                                        converterName,
                                                                        classPair));
            });
            return existing;
        });
        registryUpdated();
    }

    protected void registryUpdated() {
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(S source, T target) {
        return getDefaultConverter(ClassPair.of(source, target));
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(S source, Class<T> targetClass) {
        return getDefaultConverter(ClassPair.of(source, targetClass));
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass) {
        return getDefaultConverter(ClassPair.of(sourceClass, targetClass));
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(ClassPair<S, T> classPair) {
        Map<String, Converter> converters = getConvertersForClassPair(classPair);
        final Converter defaultConverter = converters.get("default");
        if (defaultConverter == null) {
            if (converters.size() > 1) {
                throw new RegistryException(MessageFormat.format(
                        "More than 1 converter registered for {0} and no default converter is assigned",
                        classPair));
            } else if (converters.size() == 1) {
                String converterName = converters.keySet().iterator().next();
                log.debug("Only one converter found for {}, with name = [{}], returning this converter as default", classPair, converterName);
                return converters.get(converterName);
            } else {
                throw new IllegalStateException(MessageFormat.format("Got empty converters map for {0}", classPair));
            }
        } else {
            return defaultConverter;
        }
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(S source, T target, String converterName) {
        return getConverterByName(ClassPair.of(source, target), converterName);
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(S source, Class<T> targetClass, String converterName) {
        return getConverterByName(ClassPair.of(source, targetClass), converterName);
    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(Class<S> sourceClass, Class<T> targetClass, String converterName) {
        return getConverterByName(ClassPair.of(sourceClass, targetClass), converterName);

    }

    @Override
    public <S, T> Converter<S, T> getConverterByName(ClassPair<S, T> classPair, String converterName) {
        ConcurrentMap<String, Converter> converters = getConvertersForClassPair(classPair);
        Converter converter = converters.get(converterName);
        if (converter == null) {
            throw new RegistryException(MessageFormat.format("No converter named [{0}] registered for {1}",
                                                             converterName,
                                                             classPair));
        }
        return converter;
    }

    @Override
    public ConcurrentMap<String, Converter> getConvertersForClassPair(ClassPair classPair) {
        return getConvertersForClassPair(classPair, false);
    }

    @Override
    public ConcurrentMap<String, Converter> getConvertersForClassPair(ClassPair classPair, boolean suppressException) {
        ConcurrentMap<String, Converter> converters = registry.get(classPair);
        if ((converters == null || converters.size() == 0) && !suppressException) {
            throw new RegistryException(MessageFormat.format("No converters registered for {0}", classPair));
        }
        return converters;
    }
}
