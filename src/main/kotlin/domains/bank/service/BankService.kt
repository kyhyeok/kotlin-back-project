package org.bank.domains.bank.service

import org.bank.common.logging.Logging
import org.bank.common.transaction.Transactional
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class BankService(
    private val transactional: Transactional,
    private val logger: Logger = Logging.getLogger(BankService::class.java),
) {
    fun createAccount(userUlid: String) = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        transactional.run {

        }
    }

    fun balance(userUlid: String, accountUlid: String) = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transactional.run {

        }
    }

    fun removeAccount(userUlid: String, accountUlid: String) = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transactional.run {

        }
    }
}