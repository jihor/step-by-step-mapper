package ru.jihor.mapper.tests.springSimpleConverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.jihor.mapper.base.Converter;
import ru.jihor.mapper.tests.springSimpleConverter.dictionaries.SimpleDictionary;
import ru.jihor.mapper.tests.springSimpleConverter.entities.Error;
import ru.jihor.mapper.tests.springSimpleConverter.entities.SampleSource;
import ru.jihor.mapper.tests.springSimpleConverter.entities.SampleTarget;
import ru.jihor.mapper.visitors.ExceptionAggregatingVisitor;

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 23.07.2016
 */
@Configuration
public class TestConfiguration {
    @Bean
    public Converter<SampleSource, SampleTarget> simpleConverter() {
        return Converter
                .<SampleSource, SampleTarget>builder()
                .initializeTarget(SampleTarget::new)
                .switchCase("Check errorCode")
                    .when(src -> src.getTechSection().getErrorCode() == 0)
                    .switchCase("Check if business data exists")
                        .when(src -> src.getBusinessSection() != null && src.getBusinessSection().getData() != null)
                        .step("Map data", (src, target) -> target.getData().setValue(simpleDictionary().map(src.getBusinessSection().getData())))
                        .end()
                    .otherwise()
                        .step("Create target with error", (src, target) -> target.setError(new Error("Source contains no error code but business data is empty")))
                        .end()
                    .endSwitch()
                    .end()
                .otherwise()
                    .step("Write error to tech data", (src, target) -> target.setError(
                                                                new Error("Error code [" + src.getTechSection().getErrorCode() + "], "
                                                                        + "description [" + src.getTechSection().getDescription() + "]")))
                    .end()
                .endSwitch()
                .step("Copy informant info", (src, target) -> target.getData().setInformationSource(
                        src.getInformantSection().getInfoSource().contains("anonymous") ? "???" : src.getInformantSection().getInfoSource()))
                // Come on, it can't be that much! Gotta be an int, right?
                .step("Copy how much we paid him for the info", (src, target) -> target.getData().setPrice((Integer)src.getInformantSection().getPaidPrice()))
                .end()
                .useCustomVisitor(ExceptionAggregatingVisitor::new)
                .build();
    }

    @Bean
    public SimpleDictionary simpleDictionary() {
        return new SimpleDictionary();
    }

}
