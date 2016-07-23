package ru.jihor.mapper.base;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
public class Pipeline {
    private final LinkedHashMap<String, Step> steps = new LinkedHashMap<>();

    public LinkedHashMap<String, Step> getSteps() {
        return steps;
    }

    public void add(String id, Step step) {
        log.debug("Registering transformation step [{}]", id);
        if (steps.containsKey(id)) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Transformation step with id [{0}] already exists in transformation pipeline on index {1}",
                    id,
                    new ArrayList<>(steps.keySet()).indexOf(id)));
        }
        steps.put(id, step);
    }
}
