package ru.jihor.mapper.tests.dictionaries
/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
class SampleDictionary {
    //TODO: do a scheduled update from an external web service; also maybe do an update on start
    private static def mapping = new HashMap()
    static {
        mapping << ["nice": "Double nice",
                    "b"   : "B",
                    "c"   : "C"]
    }

    String map(String key) {
        if (!mapping.containsKey(key)) {
            throw new IllegalArgumentException("No mapping defined for [$key]")
        }
        mapping."$key"
    }

}
