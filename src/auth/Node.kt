package auth

import auth.security.AES
import auth.security.KeyRSA
import auth.security.RSA
import java.math.BigInteger
import java.util.*

class Node(val id: String, val publicKey: KeyRSA, val privateKey: KeyRSA, val connectedNodes: HashMap<String, String>) {


    private val rsa = RSA()
    private val aes = AES()
    private val repo = NodeFileRepository()
    var commonSecretList = HashMap<String, ByteArray>()
        private set
    var messageList = HashMap<String, String>()
        private set

    fun connectToNode(node: Node, isInitializerNode: Boolean = true): Boolean {
        if (!isValidNode(node)) {
            return false
        }
        val mutualAuthResult = authenticate(node, node.publicKey, isInitializerNode)
        if (mutualAuthResult.first) {
            return true
        }
        return false
    }

    private fun rememberNodes(node: Node, secret: ByteArray) {
        commonSecretList[node.id] = secret
        node.commonSecretList[this.id] = secret
        this.connectedNodes[node.id] = node.publicKey.toString()
        node.connectedNodes[this.id] = this.publicKey.toString()
        repo.setNodes(this.id, this.connectedNodes)
        repo.setNodes(node.id, node.connectedNodes)
    }

    private fun authenticate(node: Node, pubKey: KeyRSA, isInitializerNode: Boolean = true): Pair<Boolean, ByteArray?> {

        val secret: ByteArray = if (isInitializerNode) {
            aes.generateKey()
        } else {
            Random().nextInt().toString().toByteArray()
        }
        commonSecretList[node.id] = secret
        val encryptedMsg = rsa.encrypt(secret, pubKey)
        val responseMsg = node.processAuthRequest(this, encryptedMsg!!, this.publicKey)
        if (isInitializerNode) {
            if (node.connectToNode(this, false)) {
                rememberNodes(node, secret)
                return Pair(true, secret)
            }
        } else {
            if (responseMsg != null && responseMsg.contentEquals(secret)) {
                return Pair(true, secret)
            }
        }
        return Pair(false, null)
    }

    private fun processAuthRequest(node: Node, cipherText: BigInteger, pubKey: KeyRSA): ByteArray? {
        val plainText = rsa.decrypt(cipherText, privateKey)
        val responseCipherText = rsa.encrypt(plainText!!, pubKey)
        return node.processAuthResponse(this, responseCipherText!!)
    }

    private fun processAuthResponse(node: Node, cipherText: BigInteger): ByteArray? {
        val msg = rsa.decrypt(cipherText, this.privateKey)
        return if (msg!!.contentEquals(commonSecretList[node.id]!!)) {
            msg
        } else {
            commonSecretList.remove(node.id)
            null
        }
    }

    fun sendMessage(node: Node, message: String): Boolean {
        return if (node.id in commonSecretList.keys && isValidNode(node)) {
            node.addMessage(this.id, aes.encrypt(message, commonSecretList[node.id]!!))
            true
        } else {
            false
        }
    }

    private fun addMessage(senderId: String, message: ByteArray) {
        messageList[senderId] = aes.decrypt(message, commonSecretList[senderId]!!)
    }

    private fun isValidNode(node: Node): Boolean {
        return node.id !in connectedNodes.keys ||
                (node.id in connectedNodes.keys &&
                        (node.publicKey.toString() == connectedNodes[node.id]))
    }
}