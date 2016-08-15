package ru.jihor.mapper.visitors;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.pipeline.Pipeline;
import ru.jihor.mapper.steps.Step;
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

    private S source;

    public S getSource() {
        return source;
    }

    @Override
    public void setSource(S source) {
        this.source = source;
    }

    private T target;

    public T getTarget() {
        return target;
    }

    @Override
    public void setTarget(T target) {
        this.target = target;
    }

    @Override
    public void visit(TransformationStep<S, T> step) {
        step.getTransformation().accept(getSource(), getTarget());
    }

    @Override
    public void visit(CheckingTransformationStep<S, T> step) {
        String checkResult = step.getCheck().apply(getSource());
        if (checkResult != null) {
            throw new CheckException(checkResult);
        }
        visit((TransformationStep<S, T>) step);
    }

    @Override
    public void visit(SwitchCaseStep<S, T> step) {
        for (Map.Entry<Predicate<S>, Pipeline> _case : step.getCases().entrySet()) {
            if (_case.getKey().test(getSource())) {
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
                beforeStep(id);
                step.accept(this);
                afterStep(id);
            } catch (TransformationException te) {
                processException(te, id);
            } catch (Exception e) {
                processException(e, id);
            }
        }
    }

    protected void processException(TransformationException te, String stepId) {
        final String description = MessageFormat.format("Step [{0}] -> {1} failed: {2}",
                                                        stepId,
                                                        te.getStep(),
                                                        te.getLocalizedMessage());
        log.error(description, te);
        throw new TransformationException(stepId, te);
    }

    protected void processException(Exception e, String stepId) {
        final String description = MessageFormat.format("Step [{0}] failed with {1}: {2}",
                                                        stepId,
                                                        e.getClass().getSimpleName(),
                                                        e.getLocalizedMessage());
        log.error(description, e);
        throw new TransformationException(stepId, description);
    }

    protected void beforeStep(String stepId) {
        log.debug("Entering step [{}]", stepId);
    }

    protected void afterStep(String stepId) {
        log.debug("Exiting step [{}]", stepId);
    }
}