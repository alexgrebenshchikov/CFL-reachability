import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GraphTests {
    @Test
    fun `there are no duplicating edges`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "A"))
        graph.addEdge(Edge(1, 2, "A"))
        graph.addEdge(Edge(2, 3, "A"))
        graph.addEdge(Edge(3, 0, "A"))
        assertEquals(4, graph.getAllEdges().size)
        val outEdgesBefore = graph.getOutEdges(0)
        val inEdgesBefore = graph.getInEdges(1)
        graph.addEdge(Edge(0, 1, "A"))
        assertEquals(4, graph.getAllEdges().size)
        assertEquals(outEdgesBefore, graph.getOutEdges(0))
        assertEquals(inEdgesBefore, graph.getInEdges(1))
    }

    @Test
    fun `contains edge test`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "A"))
        assert(graph.containsEdge(Edge(0, 1, "A")))
        assert(!graph.containsEdge(Edge(1, 2, "A")))
    }

    @Test
    fun `in and out edges test`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "A"))
        graph.addEdge(Edge(0, 1, "B"))
        graph.addEdge(Edge(2, 0, "A"))
        graph.addEdge(Edge(2, 0, "B"))
        assertEquals(setOf(Edge(2, 0, "A"), Edge(2, 0, "B")), graph.getInEdges(0))
        assertEquals(setOf(Edge(0, 1, "A"), Edge(0, 1, "B")), graph.getOutEdges(0))
    }

    @Test
    fun `add many edges`() {
        val graph = Graph()
        for(i in 0 until 500000) {
            graph.addEdge(Edge(i, i + 1, "A"))
        }
        assert(graph.getAllVertex().size == 500001)
    }
}