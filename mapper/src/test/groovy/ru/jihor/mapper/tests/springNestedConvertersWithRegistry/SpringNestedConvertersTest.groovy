package ru.jihor.mapper.tests.springNestedConvertersWithRegistry

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import ru.jihor.mapper.exceptions.TransformationException
import ru.jihor.mapper.registry.ConverterRegistry
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.config.TestConfiguration
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.CreditCardA
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.DebitCardA
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.LoanA
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemA.PersonA
import ru.jihor.mapper.tests.springNestedConvertersWithRegistry.entities.systemB.PersonB
import spock.lang.Specification

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
@ContextConfiguration(classes = TestConfiguration)
class SpringNestedConvertersTest extends Specification {

    @Autowired
    private ConverterRegistry registry;

    PersonA personA = new PersonA(lastname: "Smith",
            firstname: "John",
            middlename: "Dewey",
            debitCards: [
                    new DebitCardA(number: 6644885533221155,
                            holderName: "John Smith",
                            validThru: "12/2019"),
                    new DebitCardA(number: 1234654332153215,
                            holderName: "John D Smith",
                            validThru: "09/2018"),
                    new DebitCardA(number: 9870987098709999,
                            holderName: "John D. Smith",
                            validThru: "05/2017"),
            ],
            creditCards: [
                    new CreditCardA(number: 8888444422221111,
                            holderName: "John Smith",
                            validThru: "12/2019",
                            creditLimit: BigDecimal.valueOf(10_000D)),
                    new CreditCardA(number: 1122334455667788,
                            holderName: "John D Smith",
                            validThru: "09/2018",
                            creditLimit: BigDecimal.valueOf(100_000D)),
                    new CreditCardA(number: 1234123412341234,
                            holderName: "John D. Smith",
                            validThru: "05/2017",
                            creditLimit: BigDecimal.valueOf(1_000_000D))
            ],
            loans: [
                    new LoanA(loanAmount: 1_000_000,
                            loanIssueDate: new Date().parse("dd.MM.yyy", '11.02.1994'),
                            loanTermInMonths: 1188,
                            attachedCard: new CreditCardA(number: 7714654835121024,
                                    holderName: "John Smith",
                                    validThru: "11/2018")),
                    new LoanA(loanAmount: 60_000,
                            loanIssueDate: new Date().parse("dd.MM.yyy", '26.08.2012'),
                            loanTermInMonths: 72,
                            attachedCard: new CreditCardA(number: 6578243566448211,
                                    holderName: "John A. Smith",
                                    validThru: "08/2019"))
            ]
    )

    def "Test valid mapping"() {

        setup: "Source data is valid"

        when: "Map to targetClass"
        PersonB personB = registry.getDefaultConverter(PersonA.class, PersonB.class).convert(personA)

        then: "No exceptions, valid data in targetClass"
        // TODO: write full check
        personB.loans != null
    }

    def "Test wrong data mapping"() {

        setup: "Source data is invalid"
        personA.creditCards =
                [
                        new CreditCardA(number: 8888444422221111,
                                holderName: "John Smith",
                                validThru: "12347/019")        // <-- invalid credit card validity date
                ]

        when: "Map to targetClass"
        PersonB personB = registry.getDefaultConverter(PersonA.class, PersonB.class).convert(personA)

        then: "Exception is thrown"
        def te = thrown(TransformationException)
        te.getStep() == "[Copy credit cards] -> [Copy validity date]"
        te.getLocalizedMessage() == "Step [Copy validity date] failed with CheckException: Expected MM/YYYY format, found [12347/019]"
    }

    //TODO: test: add a default converter dynamically then use it
    //TODO: test: add a named converter dynamically then use it
    //TODO: test: try to add a duplicate converter
    //TODO: test: try to get default converter when it doent's exist
    //TODO: test: try to get named converter when it doent's exist
    //TODO: test: try to get default converter when there are multiple ones
}
