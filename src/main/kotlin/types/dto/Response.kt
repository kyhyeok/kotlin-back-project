package org.bank.types.dto

import org.springframework.http.HttpStatus

object ResponseProvider {
    fun <T> success(result: T): Response<T> {
        return Response(HttpStatus.OK.value(), "SUCCESS", result)
    }

    fun <T> failed(code: HttpStatus, message: String, result: T? = null): Response<T> {
        return Response(code.value(), message, result)
    }


}

data class Response<T>(
    val code: Int,
    val message: String,
    val result: T?,
)