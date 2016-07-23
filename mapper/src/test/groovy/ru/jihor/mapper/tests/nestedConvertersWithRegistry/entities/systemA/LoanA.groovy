package ru.jihor.mapper.tests.nestedConvertersWithRegistry.entities.systemA

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
    CardA attachedCreditCard
}
