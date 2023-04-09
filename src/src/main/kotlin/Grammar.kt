sealed interface Rule {
    data class Empty(val name: Label) : Rule {
        constructor(nameStr: String) : this(Label.Simple(nameStr))
    }

    data class Unary(val name: Label, val value: Label) : Rule {
        constructor(nameStr: String, valueStr: String) : this(Label.Simple(nameStr), Label.Simple(valueStr))
    }

    data class Binary(val name: Label, val left: Label, val right: Label) : Rule {
        constructor(nameStr: String, leftStr: String, rightStr: String) : this(
            Label.Simple(nameStr),
            Label.Simple(leftStr),
            Label.Simple(rightStr)
        )
    }
}

class Grammar {
    private val emptyRules = mutableSetOf<Rule.Empty>()

    private val labelToUnaryRules = mutableMapOf<String, MutableSet<Rule.Unary>>()
    private val labelToBinaryRulesLeft = mutableMapOf<String, MutableSet<Rule.Binary>>()
    private val labelToBinaryRulesRight = mutableMapOf<String, MutableSet<Rule.Binary>>()

    fun addEmptyRule(rule: Rule.Empty) {
        emptyRules.add(rule)
    }

    fun addUnaryRule(rule: Rule.Unary) {
        if (!labelToUnaryRules.contains(rule.value.main)) {
            labelToUnaryRules[rule.value.main] = mutableSetOf()
        }
        labelToUnaryRules[rule.value.main]?.add(rule)
    }

    fun addBinaryRule(rule: Rule.Binary) {
        if (!labelToBinaryRulesLeft.contains(rule.left.main)) {
            labelToBinaryRulesLeft[rule.left.main] = mutableSetOf()
        }
        labelToBinaryRulesLeft[rule.left.main]?.add(rule)

        if (!labelToBinaryRulesRight.contains(rule.right.main)) {
            labelToBinaryRulesRight[rule.right.main] = mutableSetOf()
        }
        labelToBinaryRulesRight[rule.right.main]?.add(rule)
    }

    fun getEmptyRules(): Set<Rule.Empty> {
        return emptyRules
    }

    fun getAssociatedUnaryRules(labelMain: String): Set<Rule.Unary> {
        return labelToUnaryRules[labelMain] ?: setOf()
    }

    fun getAssociatedLeftBinaryRules(labelMain: String): Set<Rule.Binary> {
        return labelToBinaryRulesLeft[labelMain] ?: setOf()
    }

    fun getAssociatedRightBinaryRules(labelMain: String): Set<Rule.Binary> {
        return labelToBinaryRulesRight[labelMain] ?: setOf()
    }
}