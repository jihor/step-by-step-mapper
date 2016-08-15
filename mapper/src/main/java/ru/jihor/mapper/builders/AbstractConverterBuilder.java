package ru.jihor.mapper.builders;

import ru.jihor.mapper.pipelines.Pipeline;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public abstract class AbstractConverterBuilder<P extends AbstractConverterBuilder, S, T> {
    private final P parent;

    protected AbstractConverterBuilder(P parent) {
        this.parent = parent;
    }

    protected P getParent() {
        return parent;
    }

    protected abstract void accept(Pipeline pipeline);
}
