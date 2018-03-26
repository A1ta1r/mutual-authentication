import security.KeyRSA
import security.RSA
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap

class Node(val nodeSettings: NodeSettings?) {

    var id: Int = 0
        private set
    private var privateKey: KeyRSA? = null
    var publicKey: KeyRSA? = null
        private set
    private val rsa = RSA()
    private val commonSecretList = HashMap<Int, String>()
    var connectedNodes = HashMap<Int, KeyRSA>()
        private set

    init {
        id = nodeSettings!!.id
        privateKey = nodeSettings.privateKey
        publicKey = nodeSettings.publicKey
        connectedNodes = nodeSettings.connectedNodes
    }

    fun connectToNode(node: Node, isInitializerNode: Boolean = true): Boolean {
        val pubKeyRSA: KeyRSA?
        if (node.id in connectedNodes.keys) {
            if (node.publicKey!!.exponent == connectedNodes[node.id]!!.exponent && node.publicKey!!.modulus == connectedNodes[node.id]!!.modulus) {
                pubKeyRSA = connectedNodes[node.id]
            } else return false
        } else {
            pubKeyRSA = node.publicKey
            connectedNodes[node.id] = pubKeyRSA!!
        }
        val mutualAuthResult = authenticate(node, pubKeyRSA!!, isInitializerNode)
        if (mutualAuthResult.first) {
            setSessionKey(node, mutualAuthResult.second!!)
            nodeSettings!!.appendFile(this)
            return true
        }
        return false
    }

    private fun setSessionKey(node: Node, secret: String) {
        commonSecretList[node.id] = secret
        node.commonSecretList[this.id] = secret
    }

    private fun authenticate(node: Node, pubKey: KeyRSA, isInitializerNode: Boolean = true): Pair<Boolean, String?> {
        val rnd = Random()
        val secret = String(BigInteger.probablePrime(512, rnd).toByteArray()) //AES key generation in 2.0
        commonSecretList[node.id] = secret
        val encryptedMsg = rsa.encrypt(secret, pubKey)
        val responseMsg = node.processAuthRequest(this, encryptedMsg!!, this.publicKey!!)
        if (isInitializerNode) {
            if (node.connectToNode(this, false)) {
                return Pair(true, secret)
            }
        } else {
            if (responseMsg == secret) {
                return Pair(true, secret)
            }
        }
        return Pair(false, null)
    }

    private fun processAuthRequest(node: Node, cipherText: BigInteger, pubKey: KeyRSA): String? {
        val plainText = rsa.decrypt(cipherText, privateKey!!)
        val responseCipherText = rsa.encrypt(plainText!!, pubKey)
        return node.processAuthResponse(this, responseCipherText!!)
    }

    private fun processAuthResponse(node: Node, cipherText: BigInteger): String? {
        val msg = rsa.decrypt(cipherText, this.privateKey!!)
        return if (msg == commonSecretList[node.id]) {
            msg
        } else {
            null
        }
    }
}