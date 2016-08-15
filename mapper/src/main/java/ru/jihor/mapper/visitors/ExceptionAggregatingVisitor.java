package ru.jihor.mapper.visitors;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.pipeline.Pipeline;
import ru.jihor.mapper.exceptions.TransformationException;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 2016-07-01
 */
@Slf4j
public class ExceptionAggregatingVisitor<S, T> extends DefaultVisitor<S, T>{

    List<String> exceptions = new LinkedList<>();
    Pipeline rootPipeline;

    @Override
    public void visit(Pipeline pipeline) {
        // first pipeline we encounter is the root one, let's store a reference to it
        if (rootPipeline == null){
            rootPipeline = pipeline;
        }
        super.visit(pipeline);
        // don't throw errors on inner pipelines so no duplicate errors end up in the exceptions list
        if (pipeline == rootPipeline && exceptions.size() > 0){
            throw new TransformationException("System", exceptions.toString());
        }
    }

    @Override
    protected void processException(TransformationException te, String stepId) {
        final String description = MessageFormat.format("Step [{0}] -> {1} failed: {2}",
                                                        stepId,
                                                        te.getStep(),
                                                        te.getLocalizedMessage());
        exceptions.add(description);
    }

    @Override
    protected void processException(Exception e, String stepId) {
        final String description = MessageFormat.format("Step [{0}] failed with {1}: {2}",
                                                        stepId,
                                                        e.getClass().getSimpleName(),
                                                        e.getLocalizedMessage());
        exceptions.add(description);
    }

}