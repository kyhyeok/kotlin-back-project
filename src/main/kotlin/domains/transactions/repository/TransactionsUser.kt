package org.bank.domains.transactions.repository

import org.bank.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionsUser: JpaRepository<User, String> {
    fun findByUlid(ulid: String): User
}