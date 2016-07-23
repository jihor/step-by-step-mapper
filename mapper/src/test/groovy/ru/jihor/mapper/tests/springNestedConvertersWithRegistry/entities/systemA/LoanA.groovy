package ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA
/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 22.07.2016
 */
class LoanA {
    BigDecimal loanAmount
    Long loanTermInMonths
    Date loanIssueDate
    ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.CardA attachedCreditCard
}
