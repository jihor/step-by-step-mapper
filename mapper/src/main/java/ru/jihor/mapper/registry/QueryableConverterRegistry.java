package ru.jihor.mapper.registry;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-25
 */
public abstract class QueryableConverterRegistry extends ConfigurableConverterRegistry {
    public QueryBuilder convert(Object source){
        return new QueryBuilder(this, source);
    }
}
