package ru.jihor.mapper.pipelines;

import ru.jihor.mapper.steps.Step;

import java.util.LinkedHashMap;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 15.08.2016
 */
public interface Pipeline {
    LinkedHashMap<String, Step> getSteps();

    void add(String id, Step step);
}
