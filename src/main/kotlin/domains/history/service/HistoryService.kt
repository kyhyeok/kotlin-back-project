package org.bank.domains.history.service

import kotlinx.serialization.builtins.ListSerializer
import org.bank.common.catch.RedisClient
import org.bank.common.catch.RedisKeyProvider
import org.bank.common.json.JsonUtil
import org.bank.common.logging.Logging
import org.bank.domains.history.repository.HistoryMongoRepository
import org.bank.types.dto.History
import org.bank.types.dto.Response
import org.bank.types.dto.ResponseProvider
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class HistoryService(
    private val historyMongoRepository: HistoryMongoRepository,
    private val redisClient: RedisClient,
    private val logger: Logger = Logging.getLogger(HistoryService::class.java)
) {

    fun history(ulid: String): Response<List<History>> = Logging.logFor(logger) { log ->
        log["ulid"] = ulid

        val key = RedisKeyProvider.historyCatchKey(ulid)
        val cacheValue = redisClient.get(key)

        return@logFor when {
            cacheValue == null -> {
                val result = historyMongoRepository.findLatestTransactionHistory(ulid)
                redisClient.setIfNotExist(key, JsonUtil.encodeToJson(result, ListSerializer(History.serializer())))
                return@logFor ResponseProvider.success(result)
            }

            else -> {
                val cachedData = JsonUtil.decodeFromJson(cacheValue, ListSerializer(History.serializer()))
                return@logFor ResponseProvider.success(cachedData)
            }
        }
    }
}