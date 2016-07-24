package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB
/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 22.07.2016
 */
class LoanB {
    Money loanAmount
    Long loanTermInMonths
    Date loanIssueDate
    CardB attachedCard
}
