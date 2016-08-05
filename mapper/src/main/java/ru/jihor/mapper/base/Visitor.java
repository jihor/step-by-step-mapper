package ru.jihor.mapper.base;

import ru.jihor.mapper.steps.CheckingTransformationStep;
import ru.jihor.mapper.steps.SwitchCaseStep;
import ru.jihor.mapper.steps.TransformationStep;

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2016-08-03
 */
public interface Visitor<S, T> {
    void visit(TransformationStep<S, T> step);

    void visit(CheckingTransformationStep<S, T> step);

    void visit(SwitchCaseStep<S, T> step);

    void visit(Pipeline pipeline);
}
