import auth.security.RSA
import org.junit.jupiter.api.Assertions

internal class RSATest {

    @org.junit.jupiter.api.Test
    fun testEncryption() {
        val rsa = RSA()
        val keys = rsa.generateKeyPair()
        val msg1 = "testMessage1"
        val msg2 = "Mom washed the rama"
        val emsg1 = rsa.encrypt(msg1.toByteArray(), keys.first)
        val emsg2 = rsa.encrypt(msg2.toByteArray(), keys.first)
        Assertions.assertEquals(msg1, String(rsa.decrypt(emsg1!!, keys.second)!!))
        Assertions.assertEquals(msg2, String(rsa.decrypt(emsg2!!, keys.second)!!))
    }
}