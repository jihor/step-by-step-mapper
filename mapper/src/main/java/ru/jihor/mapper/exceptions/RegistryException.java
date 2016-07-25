package ru.jihor.mapper.exceptions;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 25.07.2016
 */
public class RegistryException extends RuntimeException {
    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
