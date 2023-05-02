package base

sealed interface Type {
    object Int32: Type
    object Bool: Type
}
