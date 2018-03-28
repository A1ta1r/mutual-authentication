package auth.security

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec


class AES {

    private val aes = "AES"

    fun generateKey(): ByteArray {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(128, SecureRandom())
        return keyGen.generateKey().encoded
    }

    fun encrypt(plainText: String, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, aes)
        val cipher = Cipher.getInstance(aes)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        return cipher.doFinal(plainText.toByteArray())
    }

    fun decrypt(cipherText: ByteArray, key: ByteArray): String {
        val secretKey = SecretKeySpec(key, aes)
        val cipher = Cipher.getInstance(aes)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        return String(cipher.doFinal(cipherText))
    }
}