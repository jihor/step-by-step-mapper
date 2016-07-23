package ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemB
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
    CardB[] cards
    LoanB[] loans
}
