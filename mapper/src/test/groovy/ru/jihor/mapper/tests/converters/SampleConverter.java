package ru.jihor.mapper.tests.converters;

import ru.jihor.mapper.Converter;
import ru.jihor.mapper.DelegatingConverter;
import ru.jihor.mapper.tests.dictionaries.SampleDictionary;
import ru.jihor.mapper.tests.entities.Data;
import ru.jihor.mapper.tests.entities.Error;
import ru.jihor.mapper.tests.entities.SampleSource;
import ru.jihor.mapper.tests.entities.SampleTarget;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public class SampleConverter extends DelegatingConverter<SampleSource, SampleTarget> {
    SampleDictionary dictionary = new SampleDictionary();

    @Override
    protected Converter<SampleSource, SampleTarget> configureDelegate() {
        return Converter.<SampleSource, SampleTarget>builder().initializeTarget(SampleTarget::new)
                                                           .switchCase("Check errorCode")
                                                           .when(src -> src.getTechSection().getErrorCode() == 0)
                                                                .switchCase("Check if business data exists")
                                                                .when(src -> src.getBusinessSection()!= null &&
                                                                             src.getBusinessSection().getData() != null)
                                                                    .step("Map data",
                                                                        (src, target) -> target.setData(new Data(dictionary.map(src.getBusinessSection().getData()))))
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
                                                                                                                src.getTechSection().getErrorCode() +
                                                                                                                "], description [" +
                                                                                                                src.getTechSection().getDescription() +
                                                                                                                "]")))
                                                                .end()
                                                           .endSwitch()
                                                           .end()
                                                           .build();
    }
}
