package ru.jihor.mapper.tests.springSimpleConverter.entities

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
class SampleSource {
    TechSection techSection
    BusinessSection businessSection
    InformantSection informantSection = new InformantSection()
}

class TechSection {
    Integer errorCode
    String description
}

class BusinessSection {
    String data
}

class InformantSection {
    String infoSource = "anonymous informant"
    Number paidPrice = new Integer(100)
}