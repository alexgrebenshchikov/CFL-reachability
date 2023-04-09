import java.io.FileReader

fun parseLabel(labelStr: String, isReverse: Boolean): Label {
    return if (labelStr.contains("_")) {
        val (main, extra) = labelStr.split("_")
        val main2 = if (isReverse) "$main'" else main
        Label.Complex(main2, extra)
    } else {
        val main = if (isReverse) "$labelStr'" else labelStr
        Label.Simple(main)
    }
}


fun readJavaGraph(path: String): Graph {
    val graph = Graph()
    FileReader(path).forEachLine {
        val row = it.split(" ")
        val label = parseLabel(row[2], false)
        val labelRev = parseLabel(row[2], true)
        val e = Edge(row[0].toInt(), row[1].toInt(), label)
        val eRev = Edge(row[1].toInt(), row[0].toInt(), labelRev)
        graph.addEdge(e)
        graph.addEdge(eRev)
    }
    return graph
}

fun readCppGraph(path: String): Graph {
    val graph = Graph()
    FileReader(path).forEachLine {
        val row = it.split(" ")
        val e = Edge(row[0].toInt(), row[1].toInt(), row[2])
        val eRev = Edge(row[1].toInt(), row[0].toInt(), "${row[2]}'")
        graph.addEdge(e)
        graph.addEdge(eRev)
    }
    return graph
}

fun buildJavaGrammar(): Grammar {
    return Grammar().apply {
        addEmptyRule(Rule.Empty("A"))
        addBinaryRule(Rule.Binary("A", "assign", "A"))
        addBinaryRule(Rule.Binary(Label.Complex("C", null), Label.Simple("alias"),
            Label.Complex("load", null)))
        addBinaryRule(Rule.Binary(Label.Simple("B"), Label.Complex("store", null),
            Label.Complex("C", null)))
        addBinaryRule(Rule.Binary("A", "B", "A"))

        addEmptyRule(Rule.Empty("D"))
        addBinaryRule(Rule.Binary("D", "assign'", "D"))
        addBinaryRule(Rule.Binary(Label.Complex("E", null), Label.Simple("alias"),
            Label.Complex("store'", null)))
        addBinaryRule(Rule.Binary(Label.Simple("F"), Label.Complex("load'", null),
            Label.Complex("E", null)))
        addBinaryRule(Rule.Binary("D", "F", "D"))

        addBinaryRule(Rule.Binary("flowsTo", "alloc", "A"))
        addBinaryRule(Rule.Binary("flowsTo'", "D", "alloc'"))

        addBinaryRule(Rule.Binary("alias", "flowsTo'", "flowsTo"))
    }
}

fun buildCppGrammar(): Grammar {
    return Grammar().apply {
        addUnaryRule(Rule.Unary("A", "a'"))
        addBinaryRule(Rule.Binary("A", "M", "a'"))
        addUnaryRule(Rule.Unary("B", "a"))
        addBinaryRule(Rule.Binary("B", "a", "M"))
        addUnaryRule(Rule.Unary("V", "C"))
        addBinaryRule(Rule.Binary("V", "A", "V"))
        addUnaryRule(Rule.Unary("C", "D"))
        addBinaryRule(Rule.Binary("C", "M", "D"))
        addEmptyRule(Rule.Empty("D"))
        addBinaryRule(Rule.Binary("D", "B", "D"))
        addBinaryRule(Rule.Binary("M", "d'", "E"))
        addBinaryRule(Rule.Binary("E", "V", "D"))
    }
}


fun main(args: Array<String>) {
    val graph1 = readJavaGraph("src/main/resources/Graphs_points_to/Java_points_to/lusearch.csv")
    val grammar1 = buildJavaGrammar()

    cflReachability(graph1, grammar1)
    println(graph1.getAllEdges().size)

    val graph2 = readCppGraph("src/main/resources/Graphs_points_to/С_points_to/ls.csv")
    val grammar2 = buildCppGrammar()

    cflReachability(graph2, grammar2)
    println(graph2.getAllEdges().size)
}