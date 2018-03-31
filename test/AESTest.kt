import auth.security.AES
import org.junit.jupiter.api.Assertions

internal class AESTest {

    @org.junit.jupiter.api.Test
    fun testEncryption() {
        val aes = AES()
        val key = aes.generateKey()
        val msg1 = "testMessage1"
        val msg2 = "Mom washed the rama"
        val emsg1 = aes.encrypt(msg1, key)
        val emsg2 = aes.encrypt(msg2, key)
        Assertions.assertEquals(msg1, (aes.decrypt(emsg1, key)))
        Assertions.assertEquals(msg2, (aes.decrypt(emsg2, key)))
    }
}