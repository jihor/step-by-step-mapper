package ru.jihor.mapper.tests.registryQueries

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import ru.jihor.mapper.registry.QueryableConverterRegistry
import ru.jihor.mapper.tests.registryQueries.config.TestConfiguration
import ru.jihor.mapper.tests.registryQueries.entities.systemA.CardA
import ru.jihor.mapper.tests.registryQueries.entities.systemA.CreditCardA
import ru.jihor.mapper.tests.registryQueries.entities.systemA.DebitCardA
import ru.jihor.mapper.tests.registryQueries.entities.systemB.CardB
import spock.lang.Specification
/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
@ContextConfiguration(classes = TestConfiguration)
class RegistryQueriesSpockTest extends Specification {

    @Autowired
    private QueryableConverterRegistry registry;

    CardA a1 = new DebitCardA(number: 7890789078999999,
                              holderName: "Jane M. Doe",
                              validThru: "05/2017")

    CardA a2 = new CreditCardA(number: 6655444488887777,
                               holderName: "Jane M. Doe",
                               validThru: "03/2019",
                               creditLimit: BigDecimal.valueOf(1_000_000D))

    def "Test valid mapping"() {

        setup: "Source data is valid"

        when: "Map to targetClass"
        CardB b1 = registry.convert(a1).to(CardB.class)
        CardB b2 = registry.convert(a2).to(CardB.class)

        then: "No exceptions, valid data in targetClass"
        b1.cardNumber.toString() == a1.number
        b1.cardholderName == a1.holderName
        b1.validThruYear == 2017
        b1.validThruMonth == 5

        b2.cardNumber.toString() == a2.number
        b2.cardholderName == a2.holderName
        b2.validThruYear == 2019
        b2.validThruMonth == 3
    }
}
