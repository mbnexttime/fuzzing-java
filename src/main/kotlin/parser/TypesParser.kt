package parser

import base.Type
import base.Value

object TypesParser {
    fun getValue(valueString: String): Value {
        return when {
            valueString.startsWith("int32: ") -> {
                Value.Int32(valueString.removePrefix("int32: ").toInt())
            }
            valueString.startsWith("bool: ") -> {
                Value.Bool(valueString.removePrefix("bool: ").toBoolean())
            }
            else -> throw RuntimeException()
        }
    }

    fun getType(typeString: String) : Type {
        return when (typeString) {
            "int32" -> Type.Int32
            "bool" -> Type.Bool
            else -> throw RuntimeException()
        }
    }

    fun getString(value: Value) : String {
        return when (value) {
            is Value.Int32 -> "int32: ${value.value}"
            is Value.Bool -> "int32: ${value.value}"
        }
    }
}