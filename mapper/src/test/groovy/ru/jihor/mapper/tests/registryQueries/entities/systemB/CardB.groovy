package ru.jihor.mapper.tests.registryQueries.entities.systemB

/**
 * @author Dmitry Zhikharev (jihor@ya.ru)
 *         Created on 22.07.2016
 */
class CardB {
    BigInteger cardNumber
    Integer validThruYear
    Integer validThruMonth
    String cardholderName

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        CardB cardB = (CardB) o

        if (cardNumber != cardB.cardNumber) return false
        if (cardholderName != cardB.cardholderName) return false
        if (validThruMonth != cardB.validThruMonth) return false
        if (validThruYear != cardB.validThruYear) return false

        return true
    }

    int hashCode() {
        int result
        result = (cardNumber != null ? cardNumber.hashCode() : 0)
        result = 31 * result + (validThruYear != null ? validThruYear.hashCode() : 0)
        result = 31 * result + (validThruMonth != null ? validThruMonth.hashCode() : 0)
        result = 31 * result + (cardholderName != null ? cardholderName.hashCode() : 0)
        return result
    }
}
