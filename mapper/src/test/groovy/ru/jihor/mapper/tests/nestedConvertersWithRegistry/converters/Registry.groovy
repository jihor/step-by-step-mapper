package ru.jihor.mapper.tests.nestedConvertersWithRegistry.converters

import groovy.transform.CompileStatic
import lombok.Getter
import ru.jihor.mapper.base.DelegatingConverter
import ru.jihor.mapper.registry.ClassPair
import ru.jihor.mapper.registry.ConverterRegistry
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.CardA
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.LoanA
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.PersonA
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.CardB
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.LoanB
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.PersonB

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 23.07.2016
 */

enum Registry {

    INSTANCE

    private final ConverterRegistry registry = new ConverterRegistry()

    // Java classes don't see groovy's @Delegate unless groovy classes get compiled upfront
    // so let's make a getter field public for simplicity
    ConverterRegistry getRegistry() {
        return registry
    }

    private Registry() {
        registry.add(new ClassPair<PersonA, PersonB>(PersonA.class, PersonB.class), "personConverter", new PersonAToPersonBConverter())
        registry.add(new ClassPair<CardA, CardB>(CardA.class, CardB.class), "cardConverter", new CardAToCardBConverter())
        registry.add(new ClassPair<LoanA, LoanB>(LoanA.class, LoanB.class), "loanConverter", new LoanAToLoanBConverter())
    }
}