package security

import java.math.BigInteger

fun gcd(a: BigInteger, b: BigInteger): BigInteger {
    return if (b == BigInteger.ZERO) {
        a
    } else {
        gcd(b, a.mod(b))
    }
}

fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    return a.abs().div(gcd(a, b)).multiply(b.abs())
}

fun carmichaelNFromPQ(a: BigInteger, b: BigInteger): BigInteger {
    return lcm(a.dec(), b.dec())
}

fun isCoprime(a: BigInteger, b: BigInteger): Boolean {
    return gcd(a, b) == BigInteger.ONE
}

fun getCoprime(a: BigInteger): BigInteger {
    var testNumber = a.sqrt().inc()
    while (!isCoprime(a, testNumber)) {
        testNumber = testNumber.minus(BigInteger.ONE)
    }
    return testNumber
}
