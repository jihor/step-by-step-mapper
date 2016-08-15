package ru.jihor.mapper.registries;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.converters.Converter;
import ru.jihor.mapper.exceptions.RegistryException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-25
 */
@Slf4j
public abstract class QueryableConverterRegistry extends ConfigurableConverterRegistry {
    ConcurrentMap<ClassPair, ClassPair> cache = new ConcurrentHashMap<>();

    protected void registryUpdated() {
        invalidateCache();
    }

    private void invalidateCache() {
        cache = new ConcurrentHashMap<>();
    }

    public <S> QueryBuilder<S> convert(S source) {
        return new QueryBuilder<>(this, source);
    }

    @Override
    public <S, T> void add(ClassPair<S, T> classPair, String converterName, Converter<S, T> converter) {
        super.add(classPair, converterName, converter);
        registryUpdated();
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(S source, T target) {
        return getDefaultConverter((Class<S>) source.getClass(), (Class<T>) target.getClass());
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(S source, Class<T> targetClass) {
        return getDefaultConverter((Class<S>) source.getClass(), targetClass);
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass) {
        return getDefaultConverter(ClassPair.of(sourceClass, targetClass));
    }

    @Override
    public <S, T> Converter<S, T> getDefaultConverter(ClassPair<S, T> classPair) {
        ensureInitialization();
        // try to find exact match first
        final Map<String, Converter> convertersForExactMatch = getConvertersForClassPair(classPair, true);
        if (convertersForExactMatch == null || convertersForExactMatch.size() == 0) {
            // if no converters are registered for exact match, drill down into interfaces and superclasses
            return findBestMatch(classPair);
        } else {
            // if converters for exact match exists, get the default one
            return super.getDefaultConverter(classPair);
        }
    }

    private <S, T> Converter<S, T> findBestMatch(ClassPair<S, T> classPair) {
        final ClassPair cachedBestMatch = cache.get(classPair);
        if (cachedBestMatch != null) {
            log.debug("Returning cached match with {} for {}", cachedBestMatch, classPair);
            return getDefaultConverter(cachedBestMatch);
        } else {
            Class<S> sourceClass = classPair.getSourceClass();
            Class<T> targetClass = classPair.getTargetClass();
            Set<PrioritizedClassPair> registryCandidateClassPairs =
                    super.getRegistry()
                         .keySet()
                         .stream()
                         .filter(registryClassPair -> registryClassPair.getSourceClass().isAssignableFrom(sourceClass)
                                                      && registryClassPair.getTargetClass() == targetClass)
                         .map(registryClassPair -> new PrioritizedClassPair(registryClassPair,
                                                                            getInheritanceChainLength(sourceClass,
                                                                                                      registryClassPair.getSourceClass())))
                         .collect(Collectors.toSet());
            if (registryCandidateClassPairs.size() > 0) {
                ClassPair matchingClassPair = Collections.max(registryCandidateClassPairs);
                final Map<String, Converter> converters = getConvertersForClassPair(matchingClassPair, true);
                if (converters != null && converters.size() > 0) {
                    final Converter matchingConverter = getDefaultConverter(matchingClassPair);
                    log.debug("Found match with {} for {}", matchingClassPair, classPair);
                    cache.put(classPair, matchingClassPair);
                    return matchingConverter;
                }
            }
            throw new RegistryException(
                    MessageFormat.format("No converters registered for [{0}] or any [? super {1}, {2}] pair",
                                         classPair,
                                         classPair.getSourceClass().getCanonicalName(),
                                         classPair.getTargetClass().getCanonicalName()));
        }
    }

    private Integer getInheritanceChainLength(Class clazz1, Class clazz2) {
        if (clazz1 == clazz2) {
            return 0;
        } else {
            Integer level = 1;
            Set<Class> classes = getSuperclassAndInterfaces(clazz1);
            while (classes.size() > 0) {
                Set<Class> nextLevelClasses = new HashSet<>();
                for (Class c : classes) {
                    if (c == clazz2) {
                        return level;
                    } else {
                        nextLevelClasses.addAll(getSuperclassAndInterfaces(c));
                    }
                }
                level++;
                classes = nextLevelClasses;
            }
            throw new RegistryException(MessageFormat.format(
                    "Could not compute inheritance chain length, is {0} really assignable from {1}?",
                    clazz1.getCanonicalName(),
                    clazz2.getCanonicalName()));
        }
    }

    private static Set<Class> getSuperclassAndInterfaces(Class clazz1) {
        Set<Class> classes = new HashSet<>();
        final Class superclass = clazz1.getSuperclass();
        if (superclass != null) {
            classes.add(superclass);
        }
        classes.addAll(Arrays.asList(clazz1.getInterfaces()));
        return classes;
    }
}
