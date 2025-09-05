package org.bank.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit.SECONDS

@Configuration
class OkHttpClientClient {

    @Bean
    fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, SECONDS)
            .readTimeout(30, SECONDS)
            .writeTimeout(30, SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }
}