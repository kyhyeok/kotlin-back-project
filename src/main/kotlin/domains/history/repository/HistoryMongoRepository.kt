package org.bank.domains.history.repository

import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.bank.config.MongoTableCollector
import org.bank.types.dto.History
import org.bank.types.entity.TransactionHistoryDocument
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class HistoryMongoRepository(
    private val mongoTemplate: HashMap<String, MongoTemplate>,
    private val historyUserRepository: HistoryUserRepository,
    private val userNameMapper: ConcurrentHashMap<String, String> = ConcurrentHashMap(),
) {
    fun findLatestTransactionHistory(ulid: String, limit: Int = 30): List<History> {
        val criteria = Criteria().orOperator(
            Criteria.where("fromUlid").`is`(ulid),
            Criteria.where("toUlid").`is`(ulid),
        )

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, "time"))
            .limit(limit)

        query.fields().exclude("_id")

        val result: List<TransactionHistoryDocument> =
            getTemplate(MongoTableCollector.BANK).find(query, TransactionHistoryDocument::class.java)

        return result.map { doc ->
            val fromUser = getUserName(doc.fromUlid)
            val toUser = getUserName(doc.toUlid)

            doc.toHistory(fromUser, toUser)
        }
    }

    private fun getTemplate(c: MongoTableCollector): MongoTemplate {
        return mongoTemplate[c.table] ?: throw CustomException(ErrorCode.FAILED_TO_FIND_MONGO_TEMPLATE, c.table)
    }

    private fun getUserName(ulid: String): String {
        val value = userNameMapper[ulid] ?: ""

        if (value.isEmpty()) {
            val user = historyUserRepository.findByUlid(ulid)
            userNameMapper[ulid] = user.username

            return user.username
        } else {
            return value
        }
    }
}