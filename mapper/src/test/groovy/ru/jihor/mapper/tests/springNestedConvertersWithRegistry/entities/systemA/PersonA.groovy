package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 22.07.2016
 */
class PersonA {
    String lastname
    String firstname
    String middlename
    Set<DebitCardA> debitCards
    Set<CreditCardA> creditCards
    List<LoanA> loans
}
