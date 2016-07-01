package ru.jihor.mapper.exceptions;

import java.text.MessageFormat;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public class TransformationException extends RuntimeException {
    public String getStep() {
        return step;
    }

    public final String step;

    public TransformationException(String step, String message) {
        super(message);
        this.step = MessageFormat.format("[{0}]", step);
    }

    public TransformationException(String step, TransformationException te) {
        super(te.getMessage());
        this.step = MessageFormat.format("[{0}] -> {1}", step, te.getStep());
    }
}
