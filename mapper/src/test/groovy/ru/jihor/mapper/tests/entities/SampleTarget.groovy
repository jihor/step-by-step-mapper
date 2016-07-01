package ru.jihor.mapper.tests.entities

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
class SampleTarget {
    Error error
    Data data
}

class Error {
    Error(String message) {
        this.message = message
    }
    String message
}

class Data {
    Data(value) {
        this.value = value
    }
    def value
}