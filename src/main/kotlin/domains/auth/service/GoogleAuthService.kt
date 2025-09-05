package org.bank.domains.auth.service

import okhttp3.FormBody
import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.bank.common.httpClient.CallClient
import org.bank.config.OAuth2Config
import org.bank.interfaces.OAuth2TokenResponse
import org.bank.interfaces.OAuth2UserResponse
import org.bank.interfaces.OAuthServiceInterface
import org.springframework.stereotype.Service

private const val key = "google"

@Service(key)
class GoogleAuthService(
    private val config: OAuth2Config,
    private val httpClient: CallClient
) : OAuthServiceInterface {

    private val oAuthInfo = config.providers[key] ?: throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, key)

    private val tokenURL = "https://oauth2.googleapis.com/token"
    private val userInfoURL = "https://www.googleapis.com/oauth2/v2/userinfo"

    override val providerName: String = key

    override fun getToken(code: String): OAuth2TokenResponse {
        val body = FormBody.Builder()
            .add("code", code)
            .add("client_id", oAuthInfo.clientId)
            .add("client_secret", oAuthInfo.clientSecret)
            .add("redirect_uri", oAuthInfo.redirectUri)
            .add("grant_type", "authorization_code")
            .build()

        val headers = mapOf("Accept" to "application/json")

        httpClient.POST(tokenURL, headers, body)
        val jsonString = httpClient.POST(tokenURL, headers, body)

        TODO("Not yet implemented")
    }

    override fun getUserInfo(accessToken: String): OAuth2UserResponse {
        TODO("")
    }
}