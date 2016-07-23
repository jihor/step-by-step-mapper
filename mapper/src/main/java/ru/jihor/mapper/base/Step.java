package ru.jihor.mapper.base;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public abstract class Step {
    public abstract void accept(Visitor v);
}
