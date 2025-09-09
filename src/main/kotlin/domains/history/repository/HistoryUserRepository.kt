package org.bank.domains.history.repository

import org.bank.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface HistoryUserRepository : JpaRepository<User, String> {
    fun findByUlid(ulid: String): User
}