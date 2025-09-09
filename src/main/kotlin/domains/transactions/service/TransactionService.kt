package org.bank.domains.transactions.service

import org.bank.common.catch.RedisClient
import org.bank.common.catch.RedisKeyProvider
import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.bank.common.json.JsonUtil
import org.bank.common.logging.Logging
import org.bank.common.message.KafkaProducer
import org.bank.common.transaction.Transactional
import org.bank.domains.transactions.model.DepositResponse
import org.bank.domains.transactions.model.TransferResponse
import org.bank.domains.transactions.repository.TransactionsAccount
import org.bank.domains.transactions.repository.TransactionsUser
import org.bank.types.dto.Response
import org.bank.types.dto.ResponseProvider
import org.bank.types.message.TransactionMessage
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionService(
    private val transactionsUser: TransactionsUser,
    private val transactionsAccount: TransactionsAccount,
    private val producer: KafkaProducer,
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

                    val message = JsonUtil.encodeToJson(
                        TransactionMessage(
                            fromUlid = userUlid,
                            fromName = "0x0",
                            fromAccountUlid = "0x0",
                            toUlid = userUlid,
                            toName = user.username,
                            toAccountUlid = accountUlid,
                            value = value,
                        ), TransactionMessage.serializer()
                    )

                    producer.sendMessage("", message)

                    ResponseProvider.success(DepositResponse(afterBalance = account.balance))
                }
            }
        }

    fun transfer(
        fromUlid: String,
        fromAccountUlid: String,
        toAccountUlid: String,
        value: BigDecimal
    ): Response<TransferResponse> =
        Logging.logFor(logger) { log ->
            log["fromUlid"] = fromUlid
            log["fromAccountUlid"] = fromAccountUlid
            log["toAccountUlid"] = toAccountUlid
            log["value"] = value

            val key = RedisKeyProvider.bankMutexKey(fromUlid, fromAccountUlid)

            return@logFor redisClient.invokeWithMutex(key) {
                return@invokeWithMutex transactional.run {
                    val fromAccount = transactionsAccount.findByUlid(fromAccountUlid)
                        ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                    if (fromAccount.user.ulid != fromUlid) {
                        throw CustomException(ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID)
                    } else if (fromAccount.balance < value) {
                        throw CustomException(ErrorCode.NOT_ENOUGH_VALUE)
                    } else if (value <= BigDecimal.ZERO) {
                        throw CustomException(ErrorCode.VALUE_MUST_BE_OVER_ZERO)
                    }

                    val toAccount = transactionsAccount.findByUlid(toAccountUlid)
                        ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                    fromAccount.balance = fromAccount.balance.subtract(value)
                    toAccount.balance = toAccount.balance.add(value)

                    transactionsAccount.save(toAccount)
                    transactionsAccount.save(fromAccount)

                    ResponseProvider.success(
                        TransferResponse(
                            afterFromBalance = fromAccount.balance,
                            afterToBalance = toAccount.balance
                        )
                    )
                }
            }
        }
}