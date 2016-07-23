package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.jihor.mapper.base.Converter;
import ru.jihor.mapper.base.DelegatingConverter;
import ru.jihor.mapper.registry.ConverterRegistry;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.LoanA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.CardB;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.LoanB;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.Money;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
public class LoanAToLoanBConverter extends DelegatingConverter<LoanA, LoanB> {

    @Autowired
    ConverterRegistry registry;

    @Autowired
    DemoCurrencyDictionary dictionary;

    @Override protected Converter<LoanA, LoanB> configureDelegate() {
        return Converter
                .<LoanA, LoanB>builder()
                .initializeTarget(LoanB::new)
                .step("Copy loan amount", (a, b) -> b.setLoanAmount(new Money(a.getLoanAmount(), dictionary.getCurrencyCode())))
                .step("Copy loan term", (a, b) -> b.setLoanTermInMonths(a.getLoanTermInMonths()))
                .step("Copy loan issue date", (a, b) -> b.setLoanIssueDate(a.getLoanIssueDate()))
                .step("Copy credit card data",
                      (a, b) -> b.setAttachedCreditCard(registry.getDefaultConverter(CardA.class, CardB.class)
                                                                .convert(a.getAttachedCreditCard(), CardB::new)))
                .end()
                .build();
    }
}
