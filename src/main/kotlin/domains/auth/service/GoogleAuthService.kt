package org.bank.domains.auth.service

import org.bank.config.OAuth2Config
import org.bank.interfaces.OAuth2TokenResponse
import org.bank.interfaces.OAuth2UserResponse
import org.bank.interfaces.OAuthServiceInterface
import org.springframework.stereotype.Service

private const val key = "google"

@Service(key)
class GoogleAuthService(
    private val config: OAuth2Config
) : OAuthServiceInterface {

    private val oAuthInfo = config.providers[key] ?: throw TODO("Custom Exception")

    override val providerName: String = key

    override fun getToken(code: String): OAuth2TokenResponse {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(accessToken: String): OAuth2UserResponse {
        TODO("Not yet implemented")
    }
}