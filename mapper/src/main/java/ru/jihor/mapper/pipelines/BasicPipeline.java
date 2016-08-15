package ru.jihor.mapper.pipelines;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.steps.Step;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
public class BasicPipeline implements Pipeline {
    private final LinkedHashMap<String, Step> steps = new LinkedHashMap<>();

    @Override
    public LinkedHashMap<String, Step> getSteps() {
        return steps;
    }

    @Override
    public void add(String id, Step step) {
        log.debug("Registering transformation step [{}]", id);
        if (steps.containsKey(id)) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Transformation step with id [{0}] already exists in transformation pipelines on index {1}",
                    id,
                    new ArrayList<>(steps.keySet()).indexOf(id)));
        }
        steps.put(id, step);
    }
}
