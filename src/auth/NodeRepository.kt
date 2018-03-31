package auth

interface NodeRepository {
    fun addNode(node: Node): Boolean
    fun readNode(id: String): Node?
    fun updateNode(node: Node): Boolean
    fun setNodes(id: String, nodes: HashMap<String, String>)
    fun clear()
}