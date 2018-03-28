import auth.Node
import auth.NodeFileRepository
import auth.security.RSA

fun main(args: Array<String>) {
    val rsa = RSA()
    val repo: NodeFileRepository = NodeFileRepository()
//    val serverKeyPair = rsa.generateKeyPair()
//    val clientKeyPair = rsa.generateKeyPair()
//    val anotherKeyPair = rsa.generateKeyPair()
//
//    val node1 = Node("14", clientKeyPair.first, clientKeyPair.second, hashMapOf())
//    val node2 = Node("29", serverKeyPair.first, serverKeyPair.second, hashMapOf())
//    val node3 = Node("35", anotherKeyPair.first, anotherKeyPair.second, hashMapOf())
//
//    repo.addNode(node1)
//    repo.addNode(node2)
//    repo.addNode(node3)

    val node1 = repo.readNode("14")
    val node2 = repo.readNode("29")
    val node3 = repo.readNode("35")

    print("Connecting nodes 14 and 29: ")
    if (node1!!.connectToNode(node2!!)) {
        println("Connected!")
    } else {
        println("Fail.")
    }

    print("Connecting nodes 14 and 35: ")
    if (node1.connectToNode(node3!!)) {
        println("Connected!")
    } else {
        println("Fail.")
    }
}