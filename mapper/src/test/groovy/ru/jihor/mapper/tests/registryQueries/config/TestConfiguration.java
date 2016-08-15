package ru.jihor.mapper.tests.registryQueries.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.jihor.mapper.Converters;
import ru.jihor.mapper.converters.Converter;
import ru.jihor.mapper.registries.util.ClassPair;
import ru.jihor.mapper.registries.queryable.QueryableConverterRegistry;
import ru.jihor.mapper.registries.SimpleRegistry;
import ru.jihor.mapper.tests.registryQueries.entities.systemA.CardA;
import ru.jihor.mapper.tests.registryQueries.entities.systemB.CardB;
import ru.jihor.mapper.tests.registryQueries.entities.systemB.CreditCardB;

import java.math.BigInteger;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
@Configuration
public class TestConfiguration {

    @Bean
    public Converter<CardA, CardB> cardConverter() {
        return Converters.<CardA, CardB>builder()
                .initializeTarget(CreditCardB::new)
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
    public QueryableConverterRegistry registry() {
        return new QueryableConverterRegistry() {
            @Override
            protected void configureRegistry(SimpleRegistry aRegistry) {
                aRegistry.add(new ClassPair<>(CardA.class, CardB.class), "cardConverter", cardConverter());
            }
        };
    }

}
