package org.bank.types.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bank.common.json.BigDecimalSerializer
import org.bank.common.json.LocalDateTimeDecimalSerializer
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class TransactionMessage(
    @SerialName("fromUlid")
    val fromUlid: String,

    @SerialName("fromName")
    val fromName: String,

    @SerialName("fromAccountUlid")
    val fromAccountUlid: String,

    @SerialName("toUlid")
    val toUlid: String,

    @SerialName("toName")
    val toName: String,

    @SerialName("toAccountUlid")
    val toAccountUlid: String,

    @SerialName("value")
    @Serializable(with = BigDecimalSerializer::class)
    val value: BigDecimal,

    @SerialName("time")
    @Serializable(with = LocalDateTimeDecimalSerializer::class)
    var time: LocalDateTime = LocalDateTime.now(),
)