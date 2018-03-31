import org.junit.jupiter.api.Assertions

internal class HelpersTest {

    @org.junit.jupiter.api.Test
    fun gcd() {
        var a = 15
        var b = 8
        Assertions.assertEquals(1, auth.security.gcd(a.toBigInteger(), b.toBigInteger()).toInt())
        a = 8
        b = 4
        Assertions.assertEquals(4, auth.security.gcd(a.toBigInteger(), b.toBigInteger()).toInt())
    }

    @org.junit.jupiter.api.Test
    fun lcm() {
        var a = 15
        var b = 10
        Assertions.assertEquals(30, auth.security.lcm(a.toBigInteger(), b.toBigInteger()).toInt())
        a = 7
        b = 4
        Assertions.assertEquals(28, auth.security.lcm(a.toBigInteger(), b.toBigInteger()).toInt())
    }

    @org.junit.jupiter.api.Test
    fun carmichaelNFromPQ() {
        var a = 16
        var b = 11
        Assertions.assertEquals(30, auth.security.carmichaelNFromPQ(a.toBigInteger(), b.toBigInteger()).toInt())
        a = 9
        b = 6
        Assertions.assertEquals(40, auth.security.carmichaelNFromPQ(a.toBigInteger(), b.toBigInteger()).toInt())
    }

    @org.junit.jupiter.api.Test
    fun isCoprime() {
        var a = 16
        var b = 11
        Assertions.assertEquals(30, auth.security.carmichaelNFromPQ(a.toBigInteger(), b.toBigInteger()).toInt())
        a = 9
        b = 6
        Assertions.assertEquals(40, auth.security.carmichaelNFromPQ(a.toBigInteger(), b.toBigInteger()).toInt())
    }

    @org.junit.jupiter.api.Test
    fun getCoprime() {
        val a = 15
        val b = 3657
        Assertions.assertEquals(4, auth.security.getCoprime(a.toBigInteger()).toInt())
        Assertions.assertEquals(61, auth.security.getCoprime(b.toBigInteger()).toInt())
    }
}