package ru.jihor.mapper.registry;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-25
 */
public class PrioritizedClassPair extends ClassPair implements Comparable<PrioritizedClassPair> {
    private Integer inheritanceChainLength;

    public PrioritizedClassPair(Integer inheritanceChainLength, Class sourceClass, Class targetClass) {
        super(sourceClass, targetClass);
        this.inheritanceChainLength = inheritanceChainLength;
    }

    public PrioritizedClassPair(ClassPair classPair, Integer inheritanceChainLength) {
        super(classPair.getSourceClass(), classPair.getTargetClass());
        this.inheritanceChainLength = inheritanceChainLength;
    }

    public Integer getInheritanceChainLength() {
        return inheritanceChainLength;
    }

    @Override
    public int compareTo(PrioritizedClassPair o) {
        // Smaller class chain level have higher priorites.
        return o.getInheritanceChainLength() - inheritanceChainLength;
    }
}
