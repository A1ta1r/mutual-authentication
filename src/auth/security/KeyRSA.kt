package auth.security

import java.math.BigInteger

class KeyRSA(var exponent: BigInteger, var modulus: BigInteger) {
    constructor(string: String) : this(BigInteger.ZERO, BigInteger.ZERO) {
        val splitString = string.split(":")
        exponent = BigInteger(splitString[0])
        modulus = BigInteger(splitString[1])
    }

    override fun toString(): String {
        return "$exponent:$modulus"
    }
}