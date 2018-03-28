package auth

import auth.security.KeyRSA

interface NodeRepository {
    fun addNode(node: Node): Boolean
    fun readNode(id: String): Node?
    fun updateFiles(node: Node): Boolean

    fun getPrivateKey(id: String): KeyRSA
    fun setPrivateKey(id: String, key: KeyRSA)

    fun getPublicKey(id: String): KeyRSA
    fun setPublicKey(id: String, key: KeyRSA)

    fun setNodes(id: String, nodes: HashMap<String, String>)

    fun clear()
}