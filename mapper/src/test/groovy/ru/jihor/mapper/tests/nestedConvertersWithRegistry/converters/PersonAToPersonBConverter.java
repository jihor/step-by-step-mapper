package ru.jihor.mapper.tests.nestedConvertersWithRegistry.converters;

import ru.jihor.mapper.base.Converter;
import ru.jihor.mapper.base.DelegatingConverter;
import ru.jihor.mapper.registry.ConverterRegistry;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.LoanA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA.PersonA;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.CardB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.LoanB;
import ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB.PersonB;

import java.util.stream.Collectors;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
public class PersonAToPersonBConverter extends DelegatingConverter<PersonA, PersonB> {

    private Registry registry = Registry.INSTANCE;

    @Override
    protected Converter<PersonA, PersonB> configureDelegate() {
        return Converter
                .<PersonA, PersonB>builder()
                .initializeTarget(PersonB::new)
                .step("Copy full name", (a, b) -> {
                    b.setFirstName(a.getFirstname());
                    b.setLastName(a.getLastname());
                    b.setMiddleName(a.getMiddlename());
                })
                .step("Copy credit cards",
                      (a, b) -> b.setCards(a.getCards()
                                            .stream() // parallelStream can also be used
                                            .map((cardA) ->
                                                         registry.getDefaultConverter(CardA.class,
                                                                                      CardB.class)
                                                                 .convert(cardA, CardB::new))
                                            .toArray(CardB[]::new)))
                .step("Copy loans",
                      (a, b) -> b.setLoans(a.getLoans()
                                            .parallelStream()
                                            .map((loanA) ->
                                                         registry.getDefaultConverter(LoanA.class,
                                                                                      LoanB.class)
                                                                 .convert(loanA, LoanB::new))
                                            .toArray(LoanB[]::new)))
                .end()
                .build();
    }

}
