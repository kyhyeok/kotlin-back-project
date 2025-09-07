package org.bank.domains.transactions.repository

import org.bank.types.entity.Account
import org.bank.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionsAccount : JpaRepository<Account, String> {
    fun findByUlid(accountUlid: String): Account?

    fun findByUlidAndUser(ulid: String, user: User): Account?
}