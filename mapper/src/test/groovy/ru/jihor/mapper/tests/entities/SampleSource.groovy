package ru.jihor.mapper.tests.entities

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
class SampleSource {
    TechSection techSection
    BusinessSection businessSection
}

class TechSection {
    Integer errorCode
    String description
}

class BusinessSection {
    String data
}