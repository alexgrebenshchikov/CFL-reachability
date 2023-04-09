import java.util.LinkedList

fun cflReachability(graph: Graph, grammar: Grammar) {
    // 2
    val workList = LinkedList<Edge>()
    graph.getAllEdges().forEach { e ->
        workList.addLast(e)
    }
    // 3
    grammar.getEmptyRules().forEach { rule ->
        graph.getAllVertex().forEach { i ->
            val e = Edge(i, i, rule.name)
            if (!graph.containsEdge(e)) {
                graph.addEdge(e)
                workList.addLast(e)
            }
        }
    }
    //4
    while (workList.isNotEmpty()) {
        val (i, j, curLabel) = workList.removeFirst()
        //4.1
        grammar.getAssociatedUnaryRules(curLabel.main).forEach { rule ->
            val e = Edge(i, j, rule.name.withExtra(curLabel.getExtra()))
            if (!graph.containsEdge(e)) {
                graph.addEdge(e)
                workList.addLast(e)
            }
        }
        //4.2
        grammar.getAssociatedLeftBinaryRules(curLabel.main).forEach { rule ->
            val reqLabel = when (curLabel) {
                is Label.Simple -> {
                    rule.right
                }
                is Label.Complex -> {
                    rule.right.withExtra(curLabel.getExtra())
                }
            }
            graph.getOutEdges(j).filter { it.label == reqLabel }.forEach { (j2, k, actLabel) ->
                val e = Edge(i, k, rule.name.withExtra(combineExtras(curLabel.getExtra(), actLabel.getExtra())))
                if (!graph.containsEdge(e)) {
                    graph.addEdge(e)
                    workList.addLast(e)
                }
            }
        }
        //4.3
        grammar.getAssociatedRightBinaryRules(curLabel.main).forEach { rule ->
            val reqLabel = when (curLabel) {
                is Label.Simple -> {
                    rule.left
                }
                is Label.Complex -> {
                    rule.left.withExtra(curLabel.getExtra())
                }
            }
            graph.getInEdges(i).filter { it.label == reqLabel }.forEach { (k, _, actLabel) ->
                val e = Edge(k, j, rule.name.withExtra(combineExtras(curLabel.getExtra(), actLabel.getExtra())))
                if (!graph.containsEdge(e)) {
                    graph.addEdge(e)
                    workList.addLast(e)
                }
            }
        }
    }
}