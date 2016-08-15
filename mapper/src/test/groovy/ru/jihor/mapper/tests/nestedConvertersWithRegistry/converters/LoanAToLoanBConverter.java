package ru.jihor.mapper.tests.nestedConvertersWithRegistry.converters;

import ru.jihor.mapper.Converters;
import ru.jihor.mapper.converters.Converter;
import ru.jihor.mapper.converters.DelegatingConverter;
import ru.jihor.mapper.registries.ConverterRegistry;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.LoanA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.CardB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.LoanB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.Money;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
public class LoanAToLoanBConverter extends DelegatingConverter<LoanA, LoanB> {

    private ConverterRegistry registry = DemoConverterRegistry.getInstance();

    @Override protected Converter<LoanA, LoanB> configureDelegate() {
        return Converters
                .<LoanA, LoanB>builder()
                .initializeTarget(LoanB::new)
                .step("Copy loan amount", (a, b) -> b.setLoanAmount(new Money(a.getLoanAmount(), "EUR")))
                .step("Copy loan term", (a, b) -> b.setLoanTermInMonths(a.getLoanTermInMonths()))
                .step("Copy loan issue date", (a, b) -> b.setLoanIssueDate(a.getLoanIssueDate()))
                .step("Copy credit card data",
                      (a, b) -> b.setAttachedCreditCard(registry.getDefaultConverter(CardA.class, CardB.class)
                                                                .convert(a.getAttachedCreditCard(), CardB::new)))
                .end()
                .build();
    }
}
