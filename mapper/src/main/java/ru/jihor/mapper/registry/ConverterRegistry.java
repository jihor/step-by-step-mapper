package ru.jihor.mapper.registry;

import ru.jihor.mapper.base.Converter;

import java.util.Map;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-25
 */
public abstract class ConverterRegistry {
    public abstract <S, T> void add(ClassPair<S, T> classPair, String converterName, Converter< S, T> converter);

    public abstract <S, T> Converter<S, T> getDefaultConverter(S source, T target);
    public abstract <S, T> Converter<S, T> getDefaultConverter(S source, Class<T> targetClass);
    public abstract <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass);
    public abstract <S, T> Converter<S, T> getDefaultConverter(ClassPair<S, T> classPair);

    public abstract <S, T> Converter<S, T> getConverterByName(S source, T target, String converterName);
    public abstract <S, T> Converter<S, T> getConverterByName(S source, Class<T> targetClass, String converterName);
    public abstract <S, T> Converter<S, T> getConverterByName(Class<S> sourceClass, Class<T> targetClass, String converterName);
    public abstract <S, T> Converter<S, T> getConverterByName(ClassPair<S, T> classPair, String converterName);

    protected abstract Map<ClassPair, ? extends Map<String, Converter>> getRegistry();
    protected abstract Map<String, Converter> getConvertersForClassPair(ClassPair classPair);
    protected abstract Map<String, Converter> getConvertersForClassPair(ClassPair classPair, boolean suppressException);
}
