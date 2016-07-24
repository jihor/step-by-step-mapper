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
    Set<CardB> cards
    Set<LoanB> loans
    PersonB mainGuarantor // imagine every person with debts has a main guarantor for all the debts
}
