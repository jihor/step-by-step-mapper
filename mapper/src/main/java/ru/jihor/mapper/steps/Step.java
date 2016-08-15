package ru.jihor.mapper.steps;

import ru.jihor.mapper.visitors.Visitor;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public interface Step {
    void accept(Visitor v);
}
