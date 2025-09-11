package org.bank.common.transaction

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

interface Runner {
    fun <T> run(function: () -> T?): T?
    fun <T> readOnly(function: () -> T?): T?
}

@Component
class TransactionalAdvice : Runner {
    @Transactional
    override fun <T> run(function: () -> T?): T? = function()

    @Transactional(readOnly = true)
    override fun <T> readOnly(function: () -> T?): T? = function()
}

@Component
class Transactional(
    private val advice: Runner
) {
    fun <T> run(function: () -> T?): T? = advice.run(function)

    fun <T> readOnly(function: () -> T?): T? = advice.readOnly(function)
}
