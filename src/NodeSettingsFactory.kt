import security.KeyRSA
import java.io.*

class NodeSettingsFactory {
    fun createNew(id: Int, keyPair: Pair<KeyRSA, KeyRSA>): NodeSettings {
        val fileName = "$id.secret"
        val resultName = createSettingsFile(fileName, id, keyPair)
        return NodeSettings(id, keyPair.first, keyPair.second, hashMapOf(), filePath = resultName!!)
    }

    fun getFromFile(path: String): NodeSettings? {
        val settingsList = readSettingsFile(path)
        return try {
            val id = settingsList[0].toInt()
            val publicKey = KeyRSA(settingsList[1])
            val privateKey = KeyRSA(settingsList[2])
            val connectedNodes = HashMap<Int, KeyRSA>()
            for (i in 3 until settingsList.count()) {
                val line = settingsList[i].split(", ")
                val guestNodeId = line[0].toInt()
                val exponent = line[1].toBigInteger()
                val modulus = line[2].toBigInteger()
                connectedNodes[guestNodeId] = KeyRSA(exponent, modulus)
            }
            NodeSettings(id, publicKey, privateKey, connectedNodes, path)
        } catch (e: Exception) {
            println("Error reading from file with specified path.")
            null
        }
    }

    private fun createSettingsFile(fileName: String, id: Int, keyPair: Pair<KeyRSA, KeyRSA>): String? {
        var resultName = "secrets/$fileName"
        var settingsFile = File(resultName)
        var counter = 1
        try {
            while (settingsFile.exists()) {
                resultName = fileName + counter++
                settingsFile = File(resultName)
            }
            settingsFile.createNewFile()
        } catch (e: Exception) {
            print("Error creating new file, sorry.")
            return ""
        }
        val writer = FileWriter(settingsFile)
        val bufferedWriter = BufferedWriter(writer)
        bufferedWriter.write(id.toString())
        bufferedWriter.newLine()
        bufferedWriter.write("${keyPair.first.exponent}, ${keyPair.first.modulus}")
        bufferedWriter.newLine()
        bufferedWriter.write("${keyPair.second.exponent}, ${keyPair.second.modulus}")
        bufferedWriter.newLine()
        bufferedWriter.close()
        writer.close()
        return resultName
    }

    private fun readSettingsFile(path: String): List<String> {
        return BufferedReader(FileReader(path)).readLines()
    }
}