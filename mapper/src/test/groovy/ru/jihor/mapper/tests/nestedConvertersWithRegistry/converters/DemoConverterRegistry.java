package ru.jihor.mapper.tests.nestedConvertersWithRegistry.converters;

import ru.jihor.mapper.registry.ClassPair;
import ru.jihor.mapper.registry.ConverterRegistry;
import ru.jihor.mapper.registry.DelegatingConverterRegistry;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.LoanA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.PersonA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.CardB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.LoanB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.PersonB;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
public class DemoConverterRegistry extends DelegatingConverterRegistry {

    public final static DemoConverterRegistry instance = new DemoConverterRegistry();

    public static ConverterRegistry getInstance() {
        return instance;
    }

    private DemoConverterRegistry(){}

    @Override protected void initializeRegistry(ConverterRegistry aRegistry) {
        aRegistry.add(new ClassPair<>(PersonA.class, PersonB.class), "personConverter", new PersonAToPersonBConverter());
        aRegistry.add(new ClassPair<>(CardA.class, CardB.class), "cardConverter", new CardAToCardBConverter());
        aRegistry.add(new ClassPair<>(LoanA.class, LoanB.class), "loanConverter", new LoanAToLoanBConverter());
    }
}
