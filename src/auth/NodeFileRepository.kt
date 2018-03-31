package auth

import auth.security.KeyRSA
import java.io.*

class NodeFileRepository(private val dir: String = "secret_data\\") : NodeRepository {

    override fun addNode(node: Node): Boolean {
        val names = getFileNames(node.id)
        val publicFile = File(names[0])
        val privateFile = File(names[1])
        val nodesFile = File(names[2])
        if (!File(dir).isDirectory) {
            File(dir).mkdir()
        }
        return if (publicFile.exists() || privateFile.exists() || nodesFile.exists()) {
            println("Files for the Node with id=${node.id} already exist.")
            false
        } else {
            publicFile.createNewFile()
            privateFile.createNewFile()
            nodesFile.createNewFile()
            setPublicKey(node.id, node.publicKey)
            setPrivateKey(node.id, node.privateKey)
            setNodes(node.id, node.connectedNodes)
            true
        }
    }

    override fun updateNode(node: Node): Boolean {
        if (getNodeFiles(node.id) == null) {
            println("Files for this Node do not exist. Nothing to update")
            return false
        }
        setPublicKey(node.id, node.publicKey)
        setPrivateKey(node.id, node.privateKey)
        setNodes(node.id, node.connectedNodes)
        return true
    }

    override fun readNode(id: String): Node? {
        return if (getNodeFiles(id) != null) {
            val public = getPublicKey(id)
            val private = getPrivateKey(id)
            val nodes = getConnectedNodes(id)
            Node(id, public, private, nodes)
        } else {
            null
        }
    }

    private fun getPrivateKey(id: String): KeyRSA {
        val file = FileReader(getNodeFiles(id)!![1])
        val bufferedReader = BufferedReader(file)
        val private = bufferedReader.readLine()
        bufferedReader.close()
        file.close()
        return KeyRSA(private)
    }

    private fun setPrivateKey(id: String, key: KeyRSA) {
        val file = FileWriter(getNodeFiles(id)!![1])
        val bufferedWriter = BufferedWriter(file)
        bufferedWriter.write(key.toString())
        bufferedWriter.close()
        file.close()
    }

    override fun setNodes(id: String, nodes: HashMap<String, String>) {
        val writer = FileWriter(getNodeFiles(id)!![2])
        val bufferedWriter = BufferedWriter(writer)
        nodes.forEach {
            bufferedWriter.write("${it.key},${it.value}\n")
        }
        bufferedWriter.close()
        writer.close()
    }

    private fun getPublicKey(id: String): KeyRSA {
        val file = FileReader(getNodeFiles(id)!![0])
        val bufferedReader = BufferedReader(file)
        val public = bufferedReader.readLine()
        bufferedReader.close()
        file.close()
        return KeyRSA(public)
    }

    private fun setPublicKey(id: String, key: KeyRSA) {
        val file = FileWriter(getNodeFiles(id)!![0])
        val bufferedWriter = BufferedWriter(file)
        bufferedWriter.write(key.toString())
        bufferedWriter.close()
        file.close()
    }

    override fun clear() {
        File(dir).deleteRecursively()
    }

    private fun getFileNames(id: String): List<String> {
        val public = "$dir$id.public"
        val private = "$dir$id.private"
        val nodes = "$dir$id.nodes"
        return listOf(public, private, nodes)
    }

    private fun getNodeFiles(id: String): List<File>? {
        val fileNames = getFileNames(id)
        val public = File(fileNames[0])
        val private = File(fileNames[1])
        val nodes = File(fileNames[2])
        return if (public.exists() && private.exists() && nodes.exists()) {
            listOf(public, private, nodes)
        } else {
            null
        }
    }

    private fun getConnectedNodes(id: String): HashMap<String, String> {
        val file = FileReader(getNodeFiles(id)!![2])
        val bufferedReader = BufferedReader(file)
        val lines = bufferedReader.readLines()
        bufferedReader.close()
        file.close()
        val nodes = HashMap<String, String>()
        lines.forEach {
            val splitLines = it.split(",")
            nodes[splitLines[0]] = splitLines[1].replace("\n", "")
        }

        return nodes
    }

}