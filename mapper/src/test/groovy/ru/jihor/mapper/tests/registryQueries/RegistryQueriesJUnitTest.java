package ru.jihor.mapper.tests.registryQueries;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.jihor.mapper.Converters;
import ru.jihor.mapper.converters.Converter;
import ru.jihor.mapper.exceptions.RegistryException;
import ru.jihor.mapper.registries.ClassPair;
import ru.jihor.mapper.registries.QueryableConverterRegistry;
import ru.jihor.mapper.tests.registryQueries.config.TestConfiguration;
import ru.jihor.mapper.tests.registryQueries.entities.systemA.CardA;
import ru.jihor.mapper.tests.registryQueries.entities.systemA.CreditCardA;
import ru.jihor.mapper.tests.registryQueries.entities.systemB.CardB;
import ru.jihor.mapper.tests.registryQueries.entities.systemB.CreditCardB;

import java.math.BigInteger;

import static org.junit.Assert.assertNotEquals;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class RegistryQueriesJUnitTest extends TestCase {
    @Autowired
    QueryableConverterRegistry registry;

    @Autowired
    Converter<CardA, CardB> cardConverter;

    CreditCardA a1;

    @Before
    public void setup() {
        a1 = new CreditCardA();
        a1.setNumber("7890789078999999");
        a1.setHolderName("Jane M. Doe");
        a1.setValidThru("05/2017");
    }

    @Test
    public void testTransformationInstructions() {
        final CardB cardB1 = registry.convert(a1).to(CardB::new);  // --> CardB
        final CardB cardB2 = registry.convert(a1).to(new CardB()); // --> CardB
        final CardB cardB3 = registry.convert(a1).to(CardB.class); // will take object from default initializer --> CreditCardB
        assertEquals(cardB1, cardB2);
        assertNotEquals(cardB1, cardB3);
    }

    @Test(expected = RegistryException.class)
    public void testInvalidClassAsMethodReference() {
        final CardB cardB1 = registry.convert(a1).to(CreditCardB::new);
    }

    @Test(expected = RegistryException.class)
    public void testInvalidClassAsObject() {
        final CardB cardB1 = registry.convert(a1).to(new CreditCardB());
    }

    @Test(expected = RegistryException.class)
    public void testNotExistingMapping() {
        final String cardB1 = registry.convert(a1).to(new String());
    }

    @Test(expected = RegistryException.class)
    public void testInvalidClassAsClass() {
        final CardB cardB1 = registry.convert(a1).to(CreditCardB.class);
    }

    @Test(expected = RegistryException.class)
    @DirtiesContext
    public void testDuplicateConverterAdditionAttempt() {
        // we already have a converter named "cardConverter" for [CardA.class, CardB.class] pair,
        // so this operation should fail
        registry.add(ClassPair.of(CardA.class, CardB.class), "cardConverter", cardConverter);
    }

    @Test
    @DirtiesContext
    public void testDynamicConverterAddition() {
        // we already have a converter named for [CardA.class, CardB.class] pair,
        // so the registries will map [? extends CardA] to [CardB], but not [? extends CardA] to [CreditCardB]
        registry.add(ClassPair.of(CardA.class, CreditCardB.class), "otherCardConverter",
                     Converters.<CardA, CreditCardB>builder().initializeTarget(CreditCardB::new)
                                                                 .step("Copy card number",
                                                                  (a, b) -> b.setCardNumber(new BigInteger(a.getNumber())))
                                                                 .step("Copy validity date",
                                                                  (a) -> !(a.getValidThru().matches("\\d{2}/\\d{4}")) ?
                                                                          "Expected MM/YYYY format, found [" + a.getValidThru() + "]" :
                                                                          null,
                                                                  (a, b) -> {
                                                                      b.setValidThruYear(Integer.valueOf(a.getValidThru().substring(3)));
                                                                      b.setValidThruMonth(Integer.valueOf(a.getValidThru()
                                                                                                           .substring(0, 2)));
                                                                  })
                                                                 .step("Copy cardholder name", (a, b) -> b.setCardholderName(a.getHolderName()))
                                                                 .end()
                                                                 .build());
        final CreditCardB cardB1 = registry.convert(a1).to(CreditCardB::new);
        final CreditCardB cardB2 = registry.convert(a1).to(new CreditCardB());
        final CreditCardB cardB3 = registry.convert(a1).to(CreditCardB.class);
        assertEquals(cardB1, cardB2);
        assertEquals(cardB2, cardB3);
    }


}
