import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LabelTests {
    @Test
    fun `labels equals`() {
        val a = Label.Simple("A")
        val b = Label.Simple("A")
        val c = Label.Complex("B", null)
        val d = Label.Complex("B", null)
        val e = Label.Complex("B", "100")
        val f = Label.Complex("C", null)
        val g = Label.Complex("B", "100")
        val a1: Label = Label.Simple("A")
        val a2: Label = Label.Complex("A", null)
        assert(a == b)
        assert(c == d)
        assert(d == e)
        assert(e != f)
        assert(e == g)
        assert(a1 != a2)
    }

    @Test
    fun `combine extras`() {
        assertEquals(null, combineExtras(null, null))
        assertEquals("100", combineExtras(null, "100"))
        assertEquals("100", combineExtras("100", null))
    }
}