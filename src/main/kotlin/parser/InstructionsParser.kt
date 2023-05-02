package parser

import runner.CppInstruction
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object InstructionsParser {

    fun parseInstruction(instructionDump: String): CppInstruction {
        val parts = instructionDump.split(',')
        val nameRaw = parts[0]
        if (!nameRaw.startsWith("Function called: ")) {
            throw RuntimeException()
        }
        val numberRaw = parts[1]
        if (!numberRaw.startsWith(" instruction: ")) {
            throw RuntimeException()
        }
        val name = nameRaw.removePrefix("Function called: ")
        val number = numberRaw.removePrefix(" instruction: ").toInt()
        return CppInstruction(name, number)
    }
}