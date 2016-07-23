package ru.jihor.mapper.tests.nestedConvertersWithRegistry.converters;

import ru.jihor.mapper.base.Converter;
import ru.jihor.mapper.registry.ClassPair;
import ru.jihor.mapper.registry.ConverterRegistry;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.LoanA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.PersonA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.CardB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.LoanB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.PersonB;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
public enum Registry {
    INSTANCE;

    private ConverterRegistry registry;

    // Java classes don't see groovy's @Delegate unless groovy classes get compiled upfront
    // so let's delegate methods manually
    public <S, T> Converter<S, T> getDefaultConverter(Class<S> sourceClass, Class<T> targetClass) {
        ensureInitialization();
        return registry.getDefaultConverter(sourceClass, targetClass);
    }

    public <S, T> Converter<S, T> getConverterByName(S source, T target, String converterName) {
        ensureInitialization();
        return registry.getConverterByName(source, target, converterName);
    }

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private void ensureInitialization() {
        while (registry == null) {
            if (initialized.compareAndSet(false, true)) {
                ConverterRegistry aRegistry = new ConverterRegistry();
                aRegistry.add(new ClassPair<>(PersonA.class, PersonB.class), "personConverter", new PersonAToPersonBConverter());
                aRegistry.add(new ClassPair<>(CardA.class, CardB.class), "cardConverter", new CardAToCardBConverter());
                aRegistry.add(new ClassPair<>(LoanA.class, LoanB.class), "loanConverter", new LoanAToLoanBConverter());
                this.registry = aRegistry;
            }
        }
    }
}
