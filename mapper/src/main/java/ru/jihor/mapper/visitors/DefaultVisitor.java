package ru.jihor.mapper.visitors;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.base.Pipeline;
import ru.jihor.mapper.base.Step;
import ru.jihor.mapper.base.Visitor;
import ru.jihor.mapper.exceptions.CheckException;
import ru.jihor.mapper.exceptions.TransformationException;
import ru.jihor.mapper.steps.CheckingTransformationStep;
import ru.jihor.mapper.steps.SwitchCaseStep;
import ru.jihor.mapper.steps.TransformationStep;

import java.text.MessageFormat;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 2016-07-01
 */
@Slf4j
public class DefaultVisitor<S, T> implements Visitor<S, T> {

    S source;
    T target;

    public DefaultVisitor(S source, T target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void visit(TransformationStep<S, T> step) {
        step.getTransformation().accept(source, target);
    }

    @Override
    public void visit(CheckingTransformationStep<S, T> step) {
        String checkResult = step.getCheck().apply(source);
        if (checkResult != null) {
            throw new CheckException(checkResult);
        }
        visit((TransformationStep<S, T>) step);
    }

    @Override
    public void visit(SwitchCaseStep<S, T> step) {
        for (Map.Entry<Predicate<S>, Pipeline> _case : step.getCases().entrySet()) {
            if (_case.getKey().test(source)) {
                visit(_case.getValue());
                return;
            }
        }
    }

    @Override
    public void visit(Pipeline pipeline) {
        for (Map.Entry<String, Step> entry : pipeline.getSteps().entrySet()) {
            String id = entry.getKey();
            Step step = entry.getValue();
            try {
                log.debug("Entering step [{}]", id);
                step.accept(this);
                log.debug("Exiting step [{}]", id);
            } catch (TransformationException te) {
                final String description = MessageFormat.format("Step [{0}] -> {1} failed: {2}",
                                                                id,
                                                                te.getStep(),
                                                                te.getLocalizedMessage());
                log.error(description, te);
                throw new TransformationException(id, te);
            } catch (Exception e) {
                final String description = MessageFormat.format("Step [{0}] failed with {1}: {2}",
                                                                id,
                                                                e.getClass().getSimpleName(),
                                                                e.getLocalizedMessage());
                log.error(description, e);
                throw new TransformationException(id, description);
            }
        }
    }
}