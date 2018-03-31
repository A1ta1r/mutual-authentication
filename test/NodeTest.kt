import auth.Node
import auth.NodeFileRepository
import auth.security.RSA
import org.junit.jupiter.api.Assertions

internal class NodeTest {

    lateinit var node1: Node
    lateinit var node2: Node
    lateinit var node3: Node
    val rsa = RSA()
    val repo = NodeFileRepository("secret_data_test\\")

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        val serverKeyPair = rsa.generateKeyPair()
        val clientKeyPair = rsa.generateKeyPair()
        val anotherKeyPair = rsa.generateKeyPair()
        node1 = Node("14", clientKeyPair.first, clientKeyPair.second, hashMapOf(), repo)
        node2 = Node("29", serverKeyPair.first, serverKeyPair.second, hashMapOf(), repo)
        node3 = Node("35", anotherKeyPair.first, anotherKeyPair.second, hashMapOf(), repo)
        repo.clear()
        repo.addNode(node1)
        repo.addNode(node2)
        repo.addNode(node3)
    }

    @org.junit.jupiter.api.Test
    fun testAuthenticateFirstTime() {
        Assertions.assertTrue(node1.connectToNode(node2))
        Assertions.assertTrue(node1.connectToNode(node3))
        Assertions.assertTrue(node2.connectToNode(node3))
    }

    @org.junit.jupiter.api.Test
    fun testAuthenticateTwice() {
        Assertions.assertTrue(node1.connectToNode(node2))
        Assertions.assertTrue(node1.connectToNode(node2))
    }

    @org.junit.jupiter.api.Test
    fun testAuthenticationFail() {
        Assertions.assertTrue(node1.connectToNode(node2))
        val keys = rsa.generateKeyPair()
        val truePub = node1.publicKey
        val truePriv = node1.privateKey
        val trueMap = node1.connectedNodes
        node1 = Node("14", keys.first, keys.second, trueMap, repo)
        Assertions.assertFalse(node1.connectToNode(node2))
        Assertions.assertFalse(node2.connectToNode(node1))
        node1 = Node("14", truePub, keys.second, trueMap, repo)
        Assertions.assertFalse(node1.connectToNode(node2))
        Assertions.assertFalse(node2.connectToNode(node1))
        node1 = Node("14", keys.first, truePriv, trueMap, repo)
        Assertions.assertFalse(node1.connectToNode(node2))
        Assertions.assertFalse(node2.connectToNode(node1))
    }

    @org.junit.jupiter.api.Test
    fun testSendMessages() {
        node1.connectToNode(node2)
        Assertions.assertTrue(node1.sendMessage(node2, "Hello"))
        Assertions.assertTrue(node2.sendMessage(node1, "World"))
        Assertions.assertFalse(node1.sendMessage(node3, "Failed test"))
        Assertions.assertFalse(node2.sendMessage(node3, "Sad sad test"))
        Assertions.assertFalse(node3.sendMessage(node2, "Failed too"))
        Assertions.assertFalse(node3.sendMessage(node1, "Ehhhhh"))
    }

}