package ru.jihor.mapper.tests.springSimpleConverter.entities

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
class SampleTarget {
    Error error
    Data data = new Data()
}

class Error {
    Error(String message) {
        this.message = message
    }
    String message
}

class Data {
    def value
    def informationSource
    Integer price
}