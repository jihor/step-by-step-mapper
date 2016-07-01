package ru.jihor.mapper;

import java.util.function.BiConsumer;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public class TransformationStep<S, T> extends Step {
    private BiConsumer<S, T> transformation;

    protected TransformationStep(BiConsumer<S, T> transformation) {
        this.transformation = transformation;
    }

    protected BiConsumer<S, T> getTransformation() {
        return transformation;
    }

    @Override
    protected void accept(Visitor v) {
        v.visit(this);
    }
}
