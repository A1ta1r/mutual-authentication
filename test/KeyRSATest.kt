import auth.security.KeyRSA
import auth.security.RSA
import org.junit.jupiter.api.Assertions

internal class KeyRSATest {

    @org.junit.jupiter.api.Test
    fun testCreateFromString() {
        val rsa = RSA()
        val keys = rsa.generateKeyPair()
        val pub = KeyRSA(keys.first.toString())
        val priv = KeyRSA(keys.second.toString())
        Assertions.assertEquals(keys.first.toString(), pub.toString())
        Assertions.assertEquals(keys.second.toString(), priv.toString())
    }
}