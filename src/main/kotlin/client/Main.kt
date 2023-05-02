package client

import base.Value
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.coroutines.runBlocking
import parser.TypesParser
import java.net.ServerSocket

fun main(args: Array<String>) {
    val parser = ArgParser("command line parser")
    val portCpp by parser.option(
        ArgType.Int,
        shortName = "port_cpp",
        description = "port to use to communicate with c++ runner"
    ).required()
    parser.parse(args)
    val serverSocket = ServerSocket(portCpp)
    while (true) {
        val clientSocket = serverSocket.accept()
        val reader = clientSocket.getInputStream().bufferedReader()
        val writer = clientSocket.getOutputStream().bufferedWriter()
        while (true) {
            val input = (reader.readLine() ?: break).split(';')
            val values =
                (TypesParser.getValue(input[0]) as Value.Int32).value + (TypesParser.getValue(input[1]) as Value.Int32).value
            writer.write(TypesParser.getString(Value.Int32(values)) + ";" + "Function called: _Z3sumii, instruction: 1;Function called: _Z3sumii, instruction: 2;\n")
            writer.flush()
        }
    }
}