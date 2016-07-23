package ru.jihor.mapper.registry;

import lombok.Getter;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
public class ClassPair<S, T> {
    @Getter
    private final Class<S> sourceClass;
    @Getter
    private final Class<T> targetClass;

    private final int hashCode;

    public ClassPair (Class<S> sourceClass, Class<T> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.hashCode = this.sourceClass.hashCode() + 7 * this.targetClass.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassPair classPair = (ClassPair) o;

        if (!sourceClass.equals(classPair.sourceClass)) return false;
        return targetClass.equals(classPair.targetClass);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override public String toString() {
        return "Class pair [" + sourceClass.getCanonicalName() + ", " + targetClass.getCanonicalName() + "]";
    }
}