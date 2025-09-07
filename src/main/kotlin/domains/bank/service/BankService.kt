package org.bank.domains.bank.service

import org.bank.common.logging.Logging
import org.bank.common.transaction.Transactional
import org.bank.domains.bank.repository.BankAccountRepository
import org.bank.domains.bank.repository.BankUserRepository
import org.bank.types.dto.Response
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BankService(
    private val transactional: Transactional,
    private val backUserRepository: BankUserRepository,
    private val backAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java),
) {
    fun createAccount(userUlid: String): Response<String> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        transactional.run {

        }
    }

    fun balance(userUlid: String, accountUlid: String): Response<BigDecimal> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transactional.run {

        }
    }

    fun removeAccount(userUlid: String, accountUlid: String): Response<String> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transactional.run {

        }
    }
}