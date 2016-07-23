package ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB

import groovy.transform.TupleConstructor
import lombok.AllArgsConstructor

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 22.07.2016
 */

class Money {
    BigDecimal amount

    String currency

    Money(BigDecimal amount, String currency) {
        this.amount = amount
        this.currency = currency
    }
}
