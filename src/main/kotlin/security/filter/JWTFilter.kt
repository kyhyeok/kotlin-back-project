package org.bank.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.bank.common.jwt.JwtProvider
import org.bank.types.dto.ResponseProvider
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@ComponentScan
class JWTFilter(
    private val jwtProvider: JwtProvider,
    private val pathMatcher: AntPathMatcher,
) : OncePerRequestFilter() {
    private val JWT_AUTH_ENDPOINT = arrayOf(
        "/api/v1/bank/**",
        "/api/v1/history/**"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI

        if (shouldPerformAuthentication(requestURI)) {
            val authHeader = request.getHeader("Authorization")

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)

                try {
                    jwtProvider.verifyToken(token)
                } catch (e: CustomException) {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.contentType = "application/json"

                    val message = e.getCodeInterface()
                    val errorResponse = ResponseProvider.failed(
                        HttpStatus.UNAUTHORIZED,
                        message.message,
                        null
                    )

                    response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
                    response.writer.flush()
                    return
                }

            } else {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"

                val errorResponse = ResponseProvider.failed(
                    code = HttpStatus.UNAUTHORIZED,
                    message = ErrorCode.ACCESS_TOKEN_NEED.message,
                    null
                )

                response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
                response.writer.flush()
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun shouldPerformAuthentication(uri: String): Boolean {
        for (endPoint in JWT_AUTH_ENDPOINT) {
            if (pathMatcher.match(endPoint, uri)) {
                return true
            }
        }

        return false;
    }

}