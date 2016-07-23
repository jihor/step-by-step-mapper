package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.converters;

import ru.jihor.mapper.base.Converter;
import ru.jihor.mapper.base.DelegatingConverter;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.CardA;
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.CardB;

import java.math.BigInteger;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
public class CardAToCardBConverter extends DelegatingConverter<CardA, CardB> {
    @Override
    protected Converter<CardA, CardB> configureDelegate() {
        return Converter.<CardA, CardB>builder()
                .initializeTarget(CardB::new)
                .step("Copy card number", (a, b) -> b.setCardNumber(new BigInteger(a.getNumber())))
                .step("Copy validity date", (a) -> !(a.getValidThru().matches("\\d{2}/\\d{4}")) ? "Expected MM/YYYY format, found [" + a.getValidThru() + "]" : null,
                        (a, b) -> {
                            b.setValidThruYear(Integer.valueOf(a.getValidThru().substring(3)));
                            b.setValidThruMonth(Integer.valueOf(a.getValidThru().substring(0, 2)));
                        })
                .step("Copy cardholder name", (a, b) -> b.setCardholderName(a.getHolderName()))
                .end()
                .build();
    }
}
