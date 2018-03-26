package security

import java.math.BigInteger
import java.security.SecureRandom

class RSA {
    fun generateKeyPair(): Pair<KeyRSA, KeyRSA> {
        val rnd = SecureRandom()
        val p = BigInteger.probablePrime(2048, rnd)
        val q = BigInteger.probablePrime(2048, rnd)
        val keyModulus = p.times(q)
        val totient = carmichaelNFromPQ(p, q)
        val pubKeyExponent = getCoprime(totient)
        val privateKeyExponent = pubKeyExponent.modInverse(totient)
        val publicKey = KeyRSA(pubKeyExponent, keyModulus)
        val privateKey = KeyRSA(privateKeyExponent, keyModulus)
        return Pair(publicKey, privateKey)
    }

    fun encrypt(plainText: String, publicKey: KeyRSA): BigInteger? {
        return BigInteger(plainText.toByteArray()).modPow(publicKey.exponent, publicKey.modulus)
    }

    fun decrypt(cipherText: BigInteger, privateKey: KeyRSA): String? {
        return String((cipherText).modPow(privateKey.exponent, privateKey.modulus).toByteArray())
    }
}