package ru.jihor.mapper.registries.util;

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
        if (o == null || !(o instanceof ClassPair)) return false;

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

    public static <S, T> ClassPair<S, T> of(S source, T target) {
        return new ClassPair<>((Class<S>) source.getClass(), (Class<T>) target.getClass());
    }

    public static <S, T> ClassPair<S, T> of(S source, Class<T> targetClass) {
        return new ClassPair<>((Class<S>) source.getClass(), targetClass);
    }

    public static <S, T> ClassPair<S, T> of(Class<S> sourceClass, Class<T> targetClass) {
        return new ClassPair<>(sourceClass, targetClass);
    }
}