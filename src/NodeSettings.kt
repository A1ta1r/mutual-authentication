import security.KeyRSA
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter

data class NodeSettings(val id: Int, val publicKey: KeyRSA, val privateKey: KeyRSA, val connectedNodes: HashMap<Int, KeyRSA>, val filePath: String) {
    fun appendFile(node: Node) {
        val bufferedReader = BufferedReader(FileReader(filePath))
        val nodesInfo: List<String> = node.connectedNodes.map {
            ("${it.key}, ${it.value.exponent}, ${it.value.modulus}")
        }
        val remaining = nodesInfo.subtract(bufferedReader.lineSequence().toList())
        val bufferedWriter = BufferedWriter(FileWriter(filePath, true))
        if (bufferedReader.lineSequence().lastOrNull() == "") bufferedWriter.newLine()
        remaining.forEach { bufferedWriter.appendln(it) }
        bufferedReader.close()
        bufferedWriter.close()
    }
}