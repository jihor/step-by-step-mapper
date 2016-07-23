package ru.jihor.mapper.builders;

import ru.jihor.mapper.base.Pipeline;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public abstract class AbstractConverterBuilder<P extends AbstractConverterBuilder, S, T> {
    private final P parent;

    public AbstractConverterBuilder(P parent) {
        this.parent = parent;
    }

    public P getParent() {
        return parent;
    }

    public abstract void accept(Pipeline pipeline);
}
