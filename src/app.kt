import security.RSA

fun main(args: Array<String>) {
    val rsa = RSA()
    val factory = NodeSettingsFactory()
    val serverKeyPair = rsa.generateKeyPair()
    val clientKeyPair = rsa.generateKeyPair()
    val clientSettings = factory.createNew(14, clientKeyPair)
    val serverSettings = factory.createNew(29, serverKeyPair)
    val client = Node(clientSettings)
    val server = Node(serverSettings)
    println("Registering nodes 14 and 29.")
    print("Connecting 14 and 29: ")
    if (client.connectToNode(server)) {
        println("Connected!")
    } else {
        println("Fail.")
    }

    val friend = Node(factory.createNew(100, rsa.generateKeyPair()))

    print("Connecting nodes 14 and -1337: ")
    if (client.connectToNode(friend) || server.connectToNode(friend)) {
        println("Connected!")
    } else {
        println("Fail.")
    }

    val fakeServer = Node(factory.createNew(29, Pair(serverKeyPair.first, rsa.generateKeyPair().second)))
    print("Connecting nodes 14 and fake 29: ")
    if (client.connectToNode(fakeServer)) {
        println("Connected!")
    } else {
        println("Fail.")
    }

    val fakeClient = Node(factory.createNew(14, Pair(rsa.generateKeyPair().first, clientKeyPair.second)))
    print("Connecting nodes fake 14 and 29: ")
    if (fakeClient.connectToNode(server)) {
        println("Connected!")
    } else {
        println("Fail.")
    }
}