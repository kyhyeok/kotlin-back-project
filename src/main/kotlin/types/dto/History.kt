package org.bank.types.dto

import kotlinx.serialization.Serializable
import org.bank.common.json.BigDecimalSerializer
import org.bank.common.json.LocalDateTimeSerializer
import java.math.BigDecimal
import java.time.LocalDateTime


@Serializable
data class History(
    val fromUlid: String,
    val fromUser: String,

    val toUlid: String,
    val toUser: String,

    @Serializable(with = BigDecimalSerializer::class)
    val value: BigDecimal,

    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime
)
