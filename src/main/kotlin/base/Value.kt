package base

sealed interface Value {
    data class Int32(val value: Int) : Value
    data class Bool(val value: Boolean) : Value
}