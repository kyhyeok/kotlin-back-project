package org.bank.domains.auth.service

import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.bank.common.jwt.JwtProvider
import org.bank.interfaces.OAuthServiceInterface
import org.springframework.stereotype.Service
import kotlin.text.get

@Service
class AuthService(
    private val oAuth2Service: Map<String, OAuthServiceInterface>,
    private val jwtProvider: JwtProvider
) {
    fun handleAuth(state: String, code: String): String {
        val provider = state.lowercase()

        val callService = oAuth2Service[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND)

        val accessToken = callService.getToken(code)
        val userInfo = callService.getUserInfo(accessToken.accessToken)
        val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)

        // userInfo
    }
}
