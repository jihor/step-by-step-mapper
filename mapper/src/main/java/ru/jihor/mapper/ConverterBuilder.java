package ru.jihor.mapper;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
public class ConverterBuilder<S, T> extends PipelineBuilder<ConverterBuilder<S, T>, S, T> {

    private Converter<S, T> converter;

    protected ConverterBuilder(Converter<S, T> converter) {
        super();
        this.converter = converter;
    }

    public Converter<S, T> build() {
        converter.setPipeline(getPipeline());
        return converter;
    }

    public PipelineBuilder<ConverterBuilder<S, T>, S, T> initializeTarget(Supplier<T> targetInitializer) {
        converter.setTargetInitializer(targetInitializer);
        return this;
    }

}