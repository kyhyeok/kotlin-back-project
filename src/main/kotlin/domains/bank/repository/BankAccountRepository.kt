package org.bank.domains.bank.repository

import org.bank.types.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountRepository: JpaRepository<Account, String> {
    fun findByUlid(ulid: String): Account?
}