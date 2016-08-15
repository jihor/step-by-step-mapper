package ru.jihor.mapper.tests.simpleConverter.converters;

import ru.jihor.mapper.Converters;
import ru.jihor.mapper.converters.Converter;
import ru.jihor.mapper.converters.DelegatingConverter;
import ru.jihor.mapper.tests.simpleConverter.dictionaries.SimpleDictionary;
import ru.jihor.mapper.tests.simpleConverter.entities.Data;
import ru.jihor.mapper.tests.simpleConverter.entities.Error;
import ru.jihor.mapper.tests.simpleConverter.entities.SampleSource;
import ru.jihor.mapper.tests.simpleConverter.entities.SampleTarget;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 2016-07-01
 */
public class SimpleConverter extends DelegatingConverter<SampleSource, SampleTarget> {
    SimpleDictionary dictionary = new SimpleDictionary();

    @Override
    protected Converter<SampleSource, SampleTarget> configureDelegate() {
        return Converters
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
