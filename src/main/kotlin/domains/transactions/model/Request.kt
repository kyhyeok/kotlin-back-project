package org.bank.domains.transactions.model

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class DepositRequest(
    @field:NotBlank(message = "enter to account ulid")
    val toAccountUlid: String,

    @field:NotBlank(message = "enter to ulid")
    val toUlid: String,

    @field:NotBlank(message = "enter value")
    val value: BigDecimal,
)

data class TransferRequest(
    @field:NotBlank(message = "enter from account ulid")
    val fromAccountUlid: String,

    @field:NotBlank(message = "enter from account ulid")
    val toAccountUlid: String,

    @field:NotBlank(message = "enter from ulid")
    val fromUlid: String,

    @field:NotBlank(message = "enter value")
    val value: BigDecimal,
)
