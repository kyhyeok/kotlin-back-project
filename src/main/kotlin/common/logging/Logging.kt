package org.bank.common.logging

import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.slf4j.*

object Logging {
    fun <T: Any> getLogger(clazz: Class<T>): Logger  = LoggerFactory.getLogger(clazz)

    fun <T> logFor(log: Logger, function: (MutableMap<String, Any>) -> T?): T {
        val logInfo = mutableMapOf<String, Any>()
        logInfo["start_at"] = now()

        val result = function.invoke(logInfo)

        logInfo["end_at"] = now()

        log.info(logInfo.toString())

        return result ?: throw CustomException(ErrorCode.FAILED_TO_INVOKE_IN_LOGGER)
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }
}