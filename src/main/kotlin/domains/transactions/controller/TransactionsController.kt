package org.bank.domains.transactions.controller

import org.bank.domains.transactions.model.DepositRequest
import org.bank.domains.transactions.model.DepositResponse
import org.bank.domains.transactions.model.TransferRequest
import org.bank.domains.transactions.model.TransferResponse
import org.bank.domains.transactions.service.TransactionService
import org.bank.types.dto.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionsController(
    private val transactionService: TransactionService,
) {

    @PostMapping("/deposit")
    fun deposit(@RequestBody(required = true) request: DepositRequest): Response<DepositResponse> {
        return transactionService.deposit(request.toUlid, request.toAccountUlid, request.value)
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody(required = true) request: TransferRequest): Response<TransferResponse> {
        return transactionService.transfer(
            request.fromUlid,
            request.fromAccountUlid,
            request.toAccountUlid,
            request.value
        )
    }
}