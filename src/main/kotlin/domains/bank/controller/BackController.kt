package org.bank.domains.bank.controller

import org.bank.domains.bank.service.BankService
import org.bank.types.dto.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/bank")
class BackController(
    private val backService: BankService
) {

    @PostMapping("/create/{userUlid}")
    fun createAccount(
        @PathVariable("userUlid", required = true) userUlid: String,
    ): Response<String> {
        return backService.createAccount(userUlid)
    }

    @GetMapping("/balance/{userUlid}/{accountUlid}")
    fun balance(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String,
    ): Response<BigDecimal> {
        return backService.balance(userUlid, accountUlid)
    }

    @PostMapping("/balance/{userUlid}/{accountUlid}")
    fun removeAccount(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String,
    ): Response<String> {
        return backService.removeAccount(userUlid, accountUlid)
    }

}