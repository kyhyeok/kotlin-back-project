package org.bank.domains.bank.service

import com.github.f4b6a3.ulid.UlidCreator
import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode.*
import org.bank.common.logging.Logging
import org.bank.common.transaction.Transactional
import org.bank.domains.bank.repository.BankAccountRepository
import org.bank.domains.bank.repository.BankUserRepository
import org.bank.types.dto.Response
import org.bank.types.dto.ResponseProvider
import org.bank.types.entity.Account
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class BankService(
    private val transactional: Transactional,
    private val bankUserRepository: BankUserRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java),
) {
    fun createAccount(userUlid: String): Response<String> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid

        transactional.run {
            val user = bankUserRepository.findByUlid(userUlid)

            val ulid = UlidCreator.getUlid().toString()
            val accountNumber = generateRandomAccountNumber()

            val account = Account(
                ulid = ulid,
                user = user,
                accountNumber = accountNumber,
            )

            try {
                bankAccountRepository.save(account)
            } catch (e: Exception) {
                throw CustomException(FAILED_TO_SAVE_DATA, e.message)
            }
        }

        return@logFor ResponseProvider.success("SUCCESS")
    }

    fun balance(userUlid: String, accountUlid: String): Response<BigDecimal> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transactional.run {
            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(FAILED_TO_FIND_ACCOUNT)

            if (account.user.ulid != userUlid) throw CustomException(MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID, accountUlid)
            ResponseProvider.success(account.balance)
        }
    }

    fun removeAccount(userUlid: String, accountUlid: String): Response<String> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transactional.run {
            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(FAILED_TO_FIND_ACCOUNT)

            if (account.user.ulid != userUlid) throw CustomException(MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID, accountUlid)
            if (account.balance.compareTo(BigDecimal.ZERO) != 0) throw CustomException(
                ACCOUNT_BALANCE_IS_NOT_ZERO,
                account.balance.toString()
            )

            val updatedAccount = account.copy(
                isDeleted = true,
                updatedAt = LocalDateTime.now(),
                deletedAt = LocalDateTime.now()
            )

            bankAccountRepository.save(updatedAccount)

            ResponseProvider.success("SUCCESS")
        }
    }

    private fun generateRandomAccountNumber(): String {
        val bankCode = "003"
        val section = "12"

        val number = Math.random().toString()

        return "$bankCode-$section-$number"
    }
}