package ru.jihor.mapper.tests.springSimpleConverter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.jihor.mapper.tests.springSimpleConverter.converters.SimpleConverter
import ru.jihor.mapper.tests.springSimpleConverter.dictionaries.SimpleDictionary

import java.beans.beancontext.BeanContext

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 23.07.2016
 */
@Configuration
class TestConfiguration {
    @Bean
    SimpleConverter simpleConverter() {
        new SimpleConverter()
    }

    @Bean
    SimpleDictionary simpleDictionary(){
        new SimpleDictionary()
    }
}
