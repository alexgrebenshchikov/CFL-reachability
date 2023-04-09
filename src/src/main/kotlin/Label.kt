sealed interface Label {
    val main: String
    fun getExtra(): String?
    fun setExtra(extra: String?)
    fun withExtra(extra: String?): Label


    data class Simple(override val main: String) : Label {
        override fun getExtra(): String? {
            return null
        }

        override fun setExtra(extra: String?) = Unit

        override fun withExtra(extra: String?): Label {
            return this
        }
    }


    data class Complex(override val main: String, private var extra: String?) : Label {
        override fun getExtra(): String? {
            return extra
        }

        override fun setExtra(extra: String?) {
            this.extra = extra
        }

        override fun withExtra(extra: String?): Label {
            return this.copy().apply { setExtra(extra) }
        }

        override fun equals(other: Any?): Boolean {
            return when (other) {
                is Complex -> {
                    this.main == other.main && (this.extra == null || other.extra == null || this.extra == other.extra)
                }
                else -> {
                    false
                }
            }
        }

        override fun hashCode(): Int {
            return main.hashCode()
        }
    }
}

fun combineExtras(extra1: String?, extra2: String?): String? {
    return extra1 ?: extra2
}