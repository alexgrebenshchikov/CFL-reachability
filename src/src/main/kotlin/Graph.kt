data class Edge(val left: Int, val right: Int, val label: Label) {
    constructor(leftP: Int, rightP: Int, labelStr: String) : this(leftP, rightP, Label.Simple(labelStr))
}

class Graph {
    private val edgesSet = mutableSetOf<Edge>()
    private val vertexToOutEdges = mutableMapOf<Int, MutableSet<Edge>>()
    private val vertexToInEdges = mutableMapOf<Int, MutableSet<Edge>>()
    private val vertexSet = mutableSetOf<Int>()

    fun addEdge(e: Edge) {
        edgesSet.add(e)

        if(!vertexToOutEdges.containsKey(e.left)) {
            vertexToOutEdges[e.left] = mutableSetOf()
        }
        vertexToOutEdges[e.left]?.add(e)

        if(!vertexToInEdges.containsKey(e.right)) {
            vertexToInEdges[e.right] = mutableSetOf()
        }
        vertexToInEdges[e.right]?.add(e)

        vertexSet.add(e.left)
        vertexSet.add(e.right)
    }

    fun getOutEdges(v: Int): Set<Edge> {
        return vertexToOutEdges[v] ?: setOf()
    }

    fun getInEdges(v: Int): Set<Edge> {
        return vertexToInEdges[v] ?: setOf()
    }

    fun getAllEdges(): Set<Edge> {
        return edgesSet
    }

    fun getAllVertex(): Set<Int> {
        return vertexSet
    }

    fun containsEdge(e: Edge): Boolean {
        return edgesSet.contains(e)
    }
}