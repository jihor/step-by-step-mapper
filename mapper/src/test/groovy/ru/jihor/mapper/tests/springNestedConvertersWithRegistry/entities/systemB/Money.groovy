package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB
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
