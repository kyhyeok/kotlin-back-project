package org.bank.domains.transactions.service

import org.bank.catch.RedisClient
import org.bank.catch.RedisKeyProvider
import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.bank.common.logging.Logging
import org.bank.common.transaction.Transactional
import org.bank.domains.transactions.model.DepositResponse
import org.bank.domains.transactions.repository.TransactionsAccount
import org.bank.domains.transactions.repository.TransactionsUser
import org.bank.types.dto.Response
import org.bank.types.dto.ResponseProvider
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionService(
    private val transactionsUser: TransactionsUser,
    private val transactionsAccount: TransactionsAccount,
    private val redisClient: RedisClient,
    private val transactional: Transactional,
    private val logger: Logger = Logging.getLogger(TransactionsUser::class.java)
) {
    fun deposit(userUlid: String, accountUlid: String, value: BigDecimal): Response<DepositResponse> =
        Logging.logFor(logger) { log ->
            log["userUlid"] = userUlid
            log["accountUlid"] = accountUlid
            log["value"] = value

            val key = RedisKeyProvider.bankMutexKey(userUlid, accountUlid)

            return@logFor redisClient.invokeWithMutex(key) {
                return@invokeWithMutex transactional.run {
                    val user = transactionsUser.findByUlid(userUlid)

                    val account = transactionsAccount.findByUlidAndUser(accountUlid, user)
                        ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                    account.balance = account.balance.add(value)
                    account.updatedAt = LocalDateTime.now()

                    transactionsAccount.save(account)

                    ResponseProvider.success(DepositResponse(afterBalance = account.balance))
                }
            }
        }

}