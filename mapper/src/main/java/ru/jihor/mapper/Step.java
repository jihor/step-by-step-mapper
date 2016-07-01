package ru.jihor.mapper;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public abstract class Step {
    protected abstract void accept(Visitor v);
}
