package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.jihor.mapper.registry.ClassPair;
import ru.jihor.mapper.registry.ConverterRegistry;
import ru.jihor.mapper.registry.DelegatingConverterRegistry;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.converters.CardAToCardBConverter;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.converters.DemoCurrencyDictionary;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.converters.LoanAToLoanBConverter;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.converters.PersonAToPersonBConverter;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.LoanA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.PersonA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.CardB;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.LoanB;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.PersonB;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
@Configuration
public class TestConfiguration {
    @Bean
    public PersonAToPersonBConverter personConverter() {
        return new PersonAToPersonBConverter();
    }

    @Bean
    public CardAToCardBConverter cardConverter() {
        return new CardAToCardBConverter();
    }

    @Bean
    public LoanAToLoanBConverter loanConverter() {
        return new LoanAToLoanBConverter();
    }

    @Bean
    public DemoCurrencyDictionary dictionary() {
        return new DemoCurrencyDictionary();
    }

    @Bean
    public ConverterRegistry registry() {
        return new DelegatingConverterRegistry() {
            @Override
            protected void initializeRegistry(ConverterRegistry aRegistry) {
                aRegistry.add(new ClassPair<>(PersonA.class, PersonB.class), "personConverter", personConverter());
                aRegistry.add(new ClassPair<>(CardA.class, CardB.class), "cardConverter", cardConverter());
                aRegistry.add(new ClassPair<>(LoanA.class, LoanB.class), "loanConverter", loanConverter());
            }
        };
    }

}
