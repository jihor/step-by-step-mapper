package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.jihor.mapper.base.Converter;
import ru.jihor.mapper.registry.ClassPair;
import ru.jihor.mapper.registry.QueryableConverterRegistry;
import ru.jihor.mapper.registry.SimpleRegistry;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.converters.DemoCurrencyDictionary;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.LoanA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.PersonA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.CardB;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.LoanB;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.Money;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.PersonB;

import java.math.BigInteger;
import java.util.stream.Collectors;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
@Configuration
public class TestConfiguration {
    @Bean
    public Converter<PersonA, PersonB> personConverter() {
        return Converter
                .<PersonA, PersonB>builder()
                .initializeTarget(PersonB::new)
                .step("Copy full name", (a, b) -> {
                    b.setFirstName(a.getFirstname());
                    b.setLastName(a.getLastname());
                    b.setMiddleName(a.getMiddlename());
                })
                .step("Copy debit cards",
                      (a, b) -> b.setCards(a.getDebitCards()
                                            .stream() // parallelStream can also be used
                                            .map((cardA) ->
                                                         registry().getDefaultConverter(CardA.class,
                                                                                        CardB.class)
                                                                   .convert(cardA, CardB::new)).collect(Collectors.toSet())))
                .step("Copy credit cards",
                      (a, b) -> b.getCards().addAll(a.getCreditCards()
                                                     .stream() // parallelStream can also be used
                                                     .map((cardA) ->
                                                                  registry().getDefaultConverter(CardA.class,
                                                                                                 CardB.class)
                                                                            .convert(cardA, CardB::new)).collect(Collectors.toSet())))
                .step("Copy loans",
                      (a, b) -> b.setLoans(a.getLoans()
                                            .parallelStream()
                                            .map((loanA) ->
                                                         registry().getDefaultConverter(LoanA.class,
                                                                                        LoanB.class)
                                                                   .convert(loanA, LoanB::new)).collect(Collectors.toSet())))
                .step("Copy guarantor",
                      (a, b) -> b.setMainGuarantor(a.getMainGuarantor() != null ? personConverter().convert(a.getMainGuarantor()) : null)) //add some recursion
                .end()
                .build();
    }

    @Bean
    public Converter<CardA, CardB> cardConverter() {
        return Converter.<CardA, CardB>builder()
                .initializeTarget(CardB::new)
                .step("Copy card number", (a, b) -> b.setCardNumber(new BigInteger(a.getNumber())))
                .step("Copy validity date",
                      (a) -> !(a.getValidThru().matches("\\d{2}/\\d{4}")) ?
                              "Expected MM/YYYY format, found [" + a.getValidThru() + "]" :
                              null,
                      (a, b) -> {
                          b.setValidThruYear(Integer.valueOf(a.getValidThru().substring(3)));
                          b.setValidThruMonth(Integer.valueOf(a.getValidThru().substring(0, 2)));
                      })
                .step("Copy cardholder name", (a, b) -> b.setCardholderName(a.getHolderName()))
                .end()
                .build();
    }

    @Bean
    public Converter<LoanA, LoanB> loanConverter() {
        return Converter
                .<LoanA, LoanB>builder()
                .initializeTarget(LoanB::new)
                .step("Copy loan amount", (a, b) -> b.setLoanAmount(new Money(a.getLoanAmount(), dictionary().getCurrencyCode())))
                .step("Copy loan term", (a, b) -> b.setLoanTermInMonths(a.getLoanTermInMonths()))
                .step("Copy loan issue date", (a, b) -> b.setLoanIssueDate(a.getLoanIssueDate()))
                .step("Copy credit card data",
                      (a, b) -> b.setAttachedCard(registry().getDefaultConverter(CardA.class, CardB.class)
                                                            .convert(a.getAttachedCard(), CardB::new)))
                .end()
                .build();
    }

    @Bean
    public DemoCurrencyDictionary dictionary() {
        return new DemoCurrencyDictionary();
    }

    @Bean
    public QueryableConverterRegistry registry() {
        return new QueryableConverterRegistry() {
            @Override
            protected void configureRegistry(SimpleRegistry aRegistry) {
                aRegistry.add(new ClassPair<>(PersonA.class, PersonB.class), "personConverter", personConverter());
                aRegistry.add(new ClassPair<>(CardA.class, CardB.class), "cardConverter", cardConverter());
                aRegistry.add(new ClassPair<>(LoanA.class, LoanB.class), "loanConverter", loanConverter());
            }
        };
    }

}
