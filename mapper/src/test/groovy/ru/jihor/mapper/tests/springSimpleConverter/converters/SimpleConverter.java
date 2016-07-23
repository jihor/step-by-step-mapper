package ru.jihor.mapper.tests.springSimpleConverter.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.jihor.mapper.base.Converter;
import ru.jihor.mapper.base.DelegatingConverter;
import ru.jihor.mapper.tests.springSimpleConverter.dictionaries.SimpleDictionary;
import ru.jihor.mapper.tests.springSimpleConverter.entities.Data;
import ru.jihor.mapper.tests.springSimpleConverter.entities.Error;
import ru.jihor.mapper.tests.springSimpleConverter.entities.SampleSource;
import ru.jihor.mapper.tests.springSimpleConverter.entities.SampleTarget;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 2016-07-01
 */
public class SimpleConverter extends DelegatingConverter<SampleSource, SampleTarget> {
    @Autowired
    SimpleDictionary dictionary;

    @Override
    protected Converter<SampleSource, SampleTarget> configureDelegate() {
        return Converter
                .<SampleSource, SampleTarget>builder()
                .initializeTarget(SampleTarget::new)
                .switchCase("Check errorCode")
                .when(src -> src.getTechSection().getErrorCode() == 0)
                .switchCase("Check if business data exists")
                .when(src -> src.getBusinessSection() != null &&
                             src.getBusinessSection().getData() != null)
                .step("Map data",
                      (src, target) -> target.setData(new Data(dictionary.map(src.getBusinessSection()
                                                                                 .getData()))))
                .end()
                .otherwise()
                .step("Create target with error",
                      (src, target) -> target.setError(new Error(
                              "Source contains no error code but business data is empty")))
                .end()
                .endSwitch()
                .end()
                .otherwise()
                .step("Create target without business data",
                      (src, target) -> target.setError(new Error("Error code [" +
                                                                 src.getTechSection()
                                                                    .getErrorCode() +
                                                                 "], description [" +
                                                                 src.getTechSection()
                                                                    .getDescription() +
                                                                 "]")))
                .end()
                .endSwitch()
                .end()
                .build();
    }
}
