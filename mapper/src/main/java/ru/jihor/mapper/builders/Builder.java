package ru.jihor.mapper.builders;

import ru.jihor.mapper.pipelines.Pipeline;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public interface Builder<P extends Builder> {
    P getParent();

    void accept(Pipeline pipeline);
}
