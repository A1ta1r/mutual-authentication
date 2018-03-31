import auth.Node
import auth.NodeFileRepository
import auth.security.RSA
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable

internal class NodeFileRepositoryTest {

    lateinit var node1: Node
    lateinit var node2: Node
    lateinit var node3: Node
    val rsa = RSA()
    lateinit var repo: NodeFileRepository

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        val serverKeyPair = rsa.generateKeyPair()
        val clientKeyPair = rsa.generateKeyPair()
        val anotherKeyPair = rsa.generateKeyPair()
        repo = NodeFileRepository("secret_data_test\\")
        node1 = Node("14", clientKeyPair.first, clientKeyPair.second, hashMapOf(), repo)
        node2 = Node("29", serverKeyPair.first, serverKeyPair.second, hashMapOf(), repo)
        node3 = Node("35", anotherKeyPair.first, anotherKeyPair.second, hashMapOf(), repo)
        repo.clear()
    }

    @org.junit.jupiter.api.Test
    fun testAddNode() {
        Assertions.assertTrue(repo.addNode(node1))
        Assertions.assertFalse(repo.addNode(node1))
        Assertions.assertTrue(repo.addNode(node2))
        Assertions.assertTrue(repo.addNode(node3))
    }

    @org.junit.jupiter.api.Test
    fun testReadNode() {
        repo.addNode(node1)
        Assertions.assertEquals(node1.id, repo.readNode(node1.id)!!.id)
        Assertions.assertEquals(node1.privateKey.toString(), repo.readNode(node1.id)!!.privateKey.toString())
        Assertions.assertEquals(node1.publicKey.toString(), repo.readNode(node1.id)!!.publicKey.toString())
        Assertions.assertNull(repo.readNode(node2.id))
    }

    @org.junit.jupiter.api.Test
    fun testSetNodes() {
        repo.addNode(node1)
        repo.addNode(node2)
        repo.addNode(node3)
        Assertions.assertTrue(repo.readNode(node1.id)!!.connectedNodes.size == 0)
        node1.connectToNode(node2)
        node1.connectToNode(node3)
        Assertions.assertTrue(repo.readNode(node1.id)!!.connectedNodes.size == 2)
        Assertions.assertTrue(node1.connectedNodes.size == 2)
        Assertions.assertTrue(node1.connectedNodes == repo.readNode(node1.id)!!.connectedNodes)
    }

    @org.junit.jupiter.api.Test
    fun testSetNodeOnNewNode() {
        repo.addNode(node1)
        val exec = Executable({ node1.connectToNode(node2) })
        Assertions.assertThrows(NullPointerException::class.java, exec)
    }

    @org.junit.jupiter.api.Test
    fun testClear() {
        repo.addNode(node1)
        repo.addNode(node2)
        repo.addNode(node3)
        node1.connectToNode(node2)
        node1.connectToNode(node3)
        Assertions.assertNotNull(repo.readNode(node1.id))
        Assertions.assertNotNull(repo.readNode(node2.id))
        Assertions.assertNotNull(repo.readNode(node3.id))
        repo.clear()
        Assertions.assertNull(repo.readNode(node1.id))
        Assertions.assertNull(repo.readNode(node2.id))
        Assertions.assertNull(repo.readNode(node3.id))
    }

    @org.junit.jupiter.api.Test
    fun testUpdate() {
        val keys = rsa.generateKeyPair()
        Assertions.assertFalse(repo.updateNode(node1))
        Assertions.assertFalse(repo.updateNode(node2))
        Assertions.assertFalse(repo.updateNode(node3))
        repo.addNode(node1)
        Assertions.assertEquals(node1.id, repo.readNode(node1.id)!!.id)
        Assertions.assertEquals(node1.privateKey.toString(), repo.readNode(node1.id)!!.privateKey.toString())
        Assertions.assertEquals(node1.publicKey.toString(), repo.readNode(node1.id)!!.publicKey.toString())
        node1 = Node("14", keys.first, keys.second, hashMapOf())
        Assertions.assertTrue(repo.updateNode(node1))
        Assertions.assertEquals(node1.id, repo.readNode(node1.id)!!.id)
        Assertions.assertEquals(node1.privateKey.toString(), repo.readNode(node1.id)!!.privateKey.toString())
        Assertions.assertEquals(node1.publicKey.toString(), repo.readNode(node1.id)!!.publicKey.toString())
    }
}