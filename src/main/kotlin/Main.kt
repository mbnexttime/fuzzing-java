import base.Function
import base.Type
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import parser.TypesParser
import runner.FuzzingConfig
import runner.FuzzingController
import java.io.BufferedReader
import java.io.FileReader


fun main(args: Array<String>) {
    val parser = ArgParser("command line parser")
    val portCpp by parser.option(
        ArgType.Int,
        shortName = "port_cpp",
        description = "port to use to communicate with c++ runner"
    ).required()
    val functionDescriptionPath by parser.option(
        ArgType.String,
        shortName = "function_params_path",
        description = "path to function params description"
    ).required()
    val thresholdBeforeStop by parser.option(
        ArgType.Int,
        shortName = "threshold_before_stop",
        description = "number of executions before stop in case of not founding new ways",
    ).default(1000)
    parser.parse(args)
    val functionParamsReader = BufferedReader(FileReader(functionDescriptionPath))
    val functionParams = functionParamsReader.readLine().split(',')
    val config = FuzzingConfig(
        function = Function(
            TypesParser.getType(functionParams[0]),
            functionParams.subList(1, functionParams.size).map { TypesParser.getType(it) }),
        portCpp = portCpp,
        thresholdBeforeStop = thresholdBeforeStop,
    )
    val controller = FuzzingController(config)
    controller.startFuzzing()
}