import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GrammarTests {
    @Test
    fun `list of empty rules`() {
        val grammar = Grammar()
        grammar.addEmptyRule(Rule.Empty("A"))
        grammar.addEmptyRule(Rule.Empty("A"))
        grammar.addEmptyRule(Rule.Empty("B"))
        assertEquals(setOf(Rule.Empty("A"), Rule.Empty("B")), grammar.getEmptyRules())
    }

    @Test
    fun `associated unary rules`() {
        val grammar = Grammar()
        grammar.addUnaryRule(Rule.Unary("A", "a"))
        grammar.addUnaryRule(Rule.Unary("A", "a"))
        grammar.addUnaryRule(Rule.Unary("B", "a"))
        assertEquals(setOf(Rule.Unary("A", "a"), Rule.Unary("B", "a")),
            grammar.getAssociatedUnaryRules("a"))
    }

    @Test
    fun `associated binary rules left`() {
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary("A", "B", "C"))
        grammar.addBinaryRule(Rule.Binary("A'", "B", "c"))
        grammar.addBinaryRule(Rule.Binary("A''", "C", "B"))
        assertEquals(setOf(Rule.Binary("A", "B", "C"), Rule.Binary("A'", "B", "c")),
            grammar.getAssociatedLeftBinaryRules("B"))
    }

    @Test
    fun `associated binary rules right`() {
        val grammar = Grammar()
        grammar.addBinaryRule(Rule.Binary("A", "B", "C"))
        grammar.addBinaryRule(Rule.Binary("A'", "C", "c"))
        grammar.addBinaryRule(Rule.Binary("A''", "D", "C"))
        assertEquals(setOf(Rule.Binary("A", "B", "C"), Rule.Binary("A''", "D", "C")),
            grammar.getAssociatedRightBinaryRules("C"))
    }
}