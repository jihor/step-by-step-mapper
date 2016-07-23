package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB
/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 22.07.2016
 */
class PersonB {
    String lastName
    String firstName
    String middleName
    ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.CardB[] cards
    ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.LoanB[] loans
}
