package ru.jihor.mapper.registries;

import ru.jihor.mapper.converters.Converter;
import ru.jihor.mapper.registries.util.ClassPair;

import java.util.Map;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-25
 */
public interface ConverterRegistry {
    <S, T> void add(ClassPair<S, T> classPair, String converterName, Converter< S, T> converter);

    <S, T> Converter<S, T> getDefaultConverter(S source, T target);
    <S, T> Converter<S, T> getDefaultConverter(S source, Class<T> targetClass);
    <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass);
    <S, T> Converter<S, T> getDefaultConverter(ClassPair<S, T> classPair);

    <S, T> Converter<S, T> getConverterByName(S source, T target, String converterName);
    <S, T> Converter<S, T> getConverterByName(S source, Class<T> targetClass, String converterName);
    <S, T> Converter<S, T> getConverterByName(Class<S> sourceClass, Class<T> targetClass, String converterName);
    <S, T> Converter<S, T> getConverterByName(ClassPair<S, T> classPair, String converterName);

    Map<ClassPair, ? extends Map<String, Converter>> getRegistry();
    Map<String, Converter> getConvertersForClassPair(ClassPair classPair);
    Map<String, Converter> getConvertersForClassPair(ClassPair classPair, boolean suppressException);
}
