package ru.jihor.mapper;

import ru.jihor.mapper.builders.ConverterBuilder;
import ru.jihor.mapper.converters.BasicConverter;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 15.08.2016
 */
public class Converters {
    public static <S, T> ConverterBuilder<S, T> builder() {
        return new ConverterBuilder<>(new BasicConverter<>());
    }
}
