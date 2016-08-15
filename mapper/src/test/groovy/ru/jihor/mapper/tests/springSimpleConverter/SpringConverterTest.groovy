package ru.jihor.mapper.tests.springSimpleConverter

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import ru.jihor.mapper.converters.Converter
import ru.jihor.mapper.exceptions.TransformationException
import ru.jihor.mapper.tests.springSimpleConverter.config.TestConfiguration
import ru.jihor.mapper.tests.springSimpleConverter.dictionaries.SimpleDictionary
import ru.jihor.mapper.tests.springSimpleConverter.entities.*
import spock.lang.Specification

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
@ContextConfiguration(classes = TestConfiguration)
class SpringConverterTest extends Specification {

    @Autowired
    Converter<SampleSource, SampleTarget> converter

    @Autowired
    SimpleDictionary dictionary

    def "Test valid mapping (source errorCode == 0)"() {

        setup: "Source is valid and has errorCode == 0"
        def data = "nice"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: 0, description: ""), businessSection: new BusinessSection(data: data))
        def target

        when: "Map to targetClass"
        target = converter.convert(src)

        then: "No exceptions, valid data in targetClass"
        target.data.value == dictionary.map(data)
        !target.error
    }

    def "Test valid mapping (source errorCode != 0)"() {

        setup: "Source is valid and has errorCode != 0"
        def code = 5
        def description = "some nice description"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: code, description: description))
        def target

        when: "Map to targetClass"
        target = converter.convert(src)

        then: "No exceptions, error data in targetClass"
        target.error.message == "Error code [$code], description [$description]"
    }

    def "Test valid mapping (source is invalid)"() {

        setup: "Source is valid and has errorCode != 0"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: 0, description: ""))
        def target

        when: "Map to targetClass"
        target = converter.convert(src)

        then: "No exceptions, error data in targetClass"
        target.error.message == "Source contains no error code but business data is empty"
    }

    def "Test mapping faults"() {

        setup: "Source says it's valid but causes mapping exception"
        def data = "some nonexistent value"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: 0, description: ""),
                                            businessSection: new BusinessSection(data: data),
                                            informantSection: new InformantSection(infoSource: null, paidPrice: new Long(Integer.MAX_VALUE + 1)))

        when: "Map to targetClass"
        converter.convert(src)

        then: "And exception containing all faults is thrown"
        def exception = thrown(TransformationException)
        // all errors should be there, and no duplicate errors
        exception.message ==
                "[Step [Map data] failed with IllegalArgumentException: No mapping defined for [some nonexistent value], " +
                "Step [Copy informant info] failed with NullPointerException: null, " +
                "Step [Copy how much we paid him for the info] failed with ClassCastException: java.lang.Long cannot be cast to java.lang.Integer]"
        exception.step == "[System]"
    }
}
