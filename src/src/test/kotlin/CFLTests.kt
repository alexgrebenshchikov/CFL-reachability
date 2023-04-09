import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CFLTests {
    @Test
    fun `grammar with empty rules`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "A"))
        graph.addEdge(Edge(1, 2, "A"))
        graph.addEdge(Edge(2, 0, "A"))
        val grammar = Grammar()
        grammar.addEmptyRule(Rule.Empty("B"))
        grammar.addEmptyRule(Rule.Empty("C"))
        cflReachability(graph, grammar)
        assertEquals(9, graph.getAllEdges().size)
    }

    @Test
    fun `grammar with unary rules`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "a"))
        graph.addEdge(Edge(1, 2, "a"))
        graph.addEdge(Edge(2, 0, "b"))
        val grammar = Grammar()
        grammar.addUnaryRule(Rule.Unary("A", "a"))
        grammar.addUnaryRule(Rule.Unary("B", "A"))
        cflReachability(graph, grammar)
        assert(graph.getAllEdges().count { it.label.main == "A" } == 2)
        assert(graph.getAllEdges().count { it.label.main == "B" } == 2)
    }

    @Test
    fun `grammar with binary rules one step`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "a"))
        graph.addEdge(Edge(1, 2, "b"))
        graph.addEdge(Edge(2, 3, "c"))
        graph.addEdge(Edge(3, 0, "d"))
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary("A", "a", "b"))
        grammar.addBinaryRule(Rule.Binary("B", "c", "d"))
        cflReachability(graph, grammar)
        assert(graph.containsEdge(Edge(0, 2, "A")))
        assert(graph.containsEdge(Edge(2, 0, "B")))
    }

    @Test
    fun `grammar with binary rules two step`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "a"))
        graph.addEdge(Edge(1, 2, "b"))
        graph.addEdge(Edge(2, 3, "c"))
        graph.addEdge(Edge(3, 0, "d"))
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary("A", "a", "b"))
        grammar.addBinaryRule(Rule.Binary("B", "c", "d"))
        grammar.addBinaryRule(Rule.Binary("C", "A", "B"))
        cflReachability(graph, grammar)
        assert(graph.containsEdge(Edge(0, 2, "A")))
        assert(graph.containsEdge(Edge(2, 0, "B")))
        assert(graph.containsEdge(Edge(0, 0, "C")))
    }

    @Test
    fun `grammar with binary rules three step`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "a"))
        graph.addEdge(Edge(1, 2, "b"))
        graph.addEdge(Edge(2, 3, "c"))
        graph.addEdge(Edge(3, 0, "d"))
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary("A", "b", "c"))
        grammar.addBinaryRule(Rule.Binary("B", "a", "A"))
        grammar.addBinaryRule(Rule.Binary("C", "B", "d"))
        cflReachability(graph, grammar)
        assert(graph.containsEdge(Edge(1, 3, "A")))
        assert(graph.containsEdge(Edge(0, 3, "B")))
        assert(graph.containsEdge(Edge(0, 0, "C")))
    }

    @Test
    fun `grammar with binary rules complex labels`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, Label.Complex("a", "100")))
        graph.addEdge(Edge(1, 2, Label.Complex("b", "100")))
        graph.addEdge(Edge(2, 3, Label.Complex("a", "120")))
        graph.addEdge(Edge(3, 0, Label.Complex("b", "140")))
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary(
            Label.Complex("A", null),
            Label.Complex("a", null),
            Label.Complex("b", null)
        ))
        grammar.addBinaryRule(Rule.Binary(
            Label.Simple("B"),
            Label.Complex("a", null),
            Label.Complex("b", null)
        ))
        cflReachability(graph, grammar)
        assert(graph.getAllEdges().count { it.label.main == "A"} == 1)
        assert(graph.containsEdge(Edge(0, 2, Label.Complex("A", "100"))))
        assert(graph.containsEdge(Edge(0, 2, "B")))
    }

    @Test
    fun `grammar with binary rules complex labels #2`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, Label.Complex("d", "120")))
        graph.addEdge(Edge(1, 2, Label.Complex("c", "100")))
        graph.addEdge(Edge(2, 3, Label.Complex("a", "100")))
        graph.addEdge(Edge(3, 0, Label.Complex("b", "100")))
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary(
            Label.Complex("A", null),
            Label.Complex("a", null),
            Label.Complex("b", null)
        ))
        grammar.addBinaryRule(Rule.Binary(
            Label.Simple("B"),
            Label.Complex("c", null),
            Label.Complex("A", null)
        ))
        grammar.addBinaryRule(Rule.Binary(
            Label.Complex("C", null),
            Label.Complex("d", null),
            Label.Complex("B", null)
        ))
        cflReachability(graph, grammar)
        assert(graph.containsEdge(Edge(2, 0, Label.Complex("A", "100"))))
        assert(graph.containsEdge(Edge(1, 0, "B")))
        assert(graph.containsEdge(Edge(0, 0, Label.Complex("C", "120"))))
    }

    @Test
    fun `grammar with binary rules complex labels #3`() {
        val graph = Graph()
        graph.addEdge(Edge(0, 1, Label.Simple("a")))
        graph.addEdge(Edge(1, 2, Label.Complex("b", "100")))
        graph.addEdge(Edge(2, 3, Label.Simple("a")))
        graph.addEdge(Edge(3, 0, Label.Complex("b", "140")))
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary(
            Label.Complex("A", null),
            Label.Complex("a", null),
            Label.Complex("b", null)
        ))
        grammar.addBinaryRule(Rule.Binary(
            Label.Simple("B"),
            Label.Complex("A", null),
            Label.Complex("A", null)
        ))
        cflReachability(graph, grammar)
        assert(graph.containsEdge(Edge(0, 2, Label.Complex("A", "100"))))
        assert(graph.containsEdge(Edge(2, 0, Label.Complex("A", "140"))))
    }

    @Test
    fun `java grammar`() {
        val grammar = buildJavaGrammar()
        val graph = Graph()
        graph.addEdge(Edge(0, 1, "alloc"))
        graph.addEdge(Edge(1, 0, "alloc'"))
        graph.addEdge(Edge(1, 2, "assign"))
        graph.addEdge(Edge(2, 1, "assign'"))

        graph.addEdge(Edge(3, 1, Label.Complex("store", "1")))
        graph.addEdge(Edge(1, 3, Label.Complex("store'", "1")))

        graph.addEdge(Edge(2, 4, Label.Complex("load", "1")))
        graph.addEdge(Edge(4, 2, Label.Complex("load'", "1")))

        graph.addEdge(Edge(5, 3, "alloc"))
        graph.addEdge(Edge(3, 5, "alloc'"))

        cflReachability(graph, grammar)
        assert(graph.containsEdge(Edge(3, 4, "A")))
        assert(graph.containsEdge(Edge(4, 3, "D")))
        assert(graph.containsEdge(Edge(3, 4, "alias")))
        assert(graph.containsEdge(Edge(4, 3, "alias")))
    }
}