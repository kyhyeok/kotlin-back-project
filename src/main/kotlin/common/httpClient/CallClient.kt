package org.bank.common.httpClient

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class CallClient(
    private val httpClient: OkHttpClient,
) {

    fun GET(uri: String, headers: Map<String, String> = emptyMap()): String {
        val requestBuilder = Request.Builder().url(uri)
        headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    fun POST(uri: String, headers: Map<String, String> = emptyMap(), body: RequestBody): String {
        val requestBuilder = Request.Builder().url(uri).post(body)
        headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    private fun resultHandler(response: Response): String {
        response.use {
            if (!it.isSuccessful) {
                val message = "Http ${it.code}: ${it.body?.string() ?: "Unknown error"}"
                throw CustomException(ErrorCode.FAILED_TO_CALL_CLIENT, message)
            }

            return it.body?.string() ?: throw CustomException(ErrorCode.CALL_REQUEST_BODY_NULL)
        }

    }

}