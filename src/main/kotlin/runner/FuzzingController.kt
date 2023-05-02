package runner

import base.Type
import base.Value
import kotlinx.coroutines.*
import org.utbot.fuzzing.*
import org.utbot.fuzzing.seeds.BitVectorValue
import org.utbot.fuzzing.seeds.Signed
import org.utbot.fuzzing.utils.Trie
import parser.InstructionsParser
import parser.TypesParser
import java.net.Socket

class FuzzingController(
    private val config: FuzzingConfig,
) {
    fun startFuzzing() {
        val socket = Socket("127.0.0.1", config.portCpp)

        val reader = socket.getInputStream().bufferedReader()
        val writer = socket.getOutputStream().bufferedWriter()
        var counterBeforeStop = 0
        val cache: HashSet<List<CppInstruction>> = hashSetOf()
        runBlocking {
            runFuzzing(
                provider = { _, _ ->
                    config.function.argumentsTypes.map<Type, Seed<Type, Value>> {
                        when (it) {
                            Type.Int32 -> Seed.Known(Signed.ZERO.invoke(32)) { obj: BitVectorValue -> Value.Int32(obj.toInt()) }
                            Type.Bool -> Seed.Known(Signed.ZERO.invoke(1)) { obj: BitVectorValue -> Value.Bool(obj.toBoolean()) }
                        }
                    }.asSequence()
                },
                description = Description(
                    parameters = config.function.argumentsTypes,
                ),
                handle = { _, l ->
                    val arguments = buildArgumentsString(*l.toTypedArray())
                    writer.write(arguments + "\n")
                    writer.flush()
                    val rawResult = reader.readLine().split(';').dropLast(1)

                    val result = try {
                        val returnValue = TypesParser.getValue(rawResult[0])
                        val traces = rawResult.drop(1).map { InstructionsParser.parseInstruction(it) }
                        Pair(returnValue, traces)
                    } catch (e: Exception) {
                        null
                    }
                    if (result != null) {
                        if (cache.contains(result.second)) {
                            if (counterBeforeStop + 1 >= config.thresholdBeforeStop) {
                                BaseFeedback(result, control = Control.STOP)
                            } else {
                                counterBeforeStop += 1
                                BaseFeedback(result, control = Control.CONTINUE)
                            }
                        } else {
                            println("case ${cache.size + 1}:")
                            println("arguments:")
                            println(arguments.split(";").joinToString("\n"))
                            println("result:")
                            println(result.first)
                            println()
                            counterBeforeStop = 0
                            cache.add(result.second)
                            BaseFeedback(result, control = Control.CONTINUE)
                        }
                    } else {
                        /**
                         * crash or something bad
                         */
                        println("$arguments caused a crash")
                        BaseFeedback(null, control = Control.STOP)
                    }
                }
            )
        }
    }

    private fun buildArgumentsString(vararg arguments: Value): String {
        val stringBuilder = StringBuilder()
        for ((i, argument) in arguments.withIndex()) {
            stringBuilder.append(TypesParser.getString(argument))
            if (i != arguments.size - 1) {
                stringBuilder.append(";")
            }
        }
        return stringBuilder.toString()
    }
}