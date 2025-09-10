package org.bank.common.catch

object RedisKeyProvider {
    private const val BANK_MUTEX_KEY = "bankMutex"
    private const val HISTORY_CATCH_KEY = "history"

    fun bankMutexKey(ulid: String, accountUlid: String): String {
        return "$BANK_MUTEX_KEY:$ulid:$accountUlid"
    }

    fun historyCatchKey(ulid: String): String {
        return "$HISTORY_CATCH_KEY:$ulid"
    }
}