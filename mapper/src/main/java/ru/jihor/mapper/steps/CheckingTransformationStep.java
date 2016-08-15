package ru.jihor.mapper.steps;

import ru.jihor.mapper.visitors.Visitor;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
public class CheckingTransformationStep<S, T> extends TransformationStep<S, T> {
    private final Function<S, String> check;

    public CheckingTransformationStep(Function<S, String> check, BiConsumer<S, T> transformation) {
        super(transformation);
        this.check = check;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public Function<S, String> getCheck() {
        return check;
    }
}
