package org.bank.domains.auth.repository

import org.bank.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuthUserRepository : JpaRepository<User, String> {
    fun existsByUsername(username: String): Boolean

    @Modifying
    @Query("UPDATE User SET accessToken = :token WHERE username = :username")
    fun updateAccessTokenByUsername(
        @Param("username") username: String,
        @Param("accessToken") token: String,
    )
}