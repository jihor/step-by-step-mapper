package ru.jihor.mapper;

import java.util.LinkedHashMap;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
public class SwitchCaseStep<S, T> extends Step {
    private final LinkedHashMap<Predicate<S>, Pipeline> cases = new LinkedHashMap<>();

    protected LinkedHashMap<Predicate<S>, Pipeline> getCases() {
        return cases;
    }

    protected void addCase(Predicate<S> condition, Pipeline pipeline) {
        log.debug("Registering case # " + (cases.size() + 1));
        if (cases.containsKey(condition)) {
            throw new IllegalArgumentException("Duplicate condition in SwitchCase statement");
        }
        cases.put(condition, pipeline);
    }

    @Override
    protected void accept(Visitor v) {
        v.visit(this);
    }
}
