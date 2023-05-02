package runner

import base.Function

data class FuzzingConfig(
    val function: Function,
    val portCpp: Int,
    val thresholdBeforeStop: Int,
)