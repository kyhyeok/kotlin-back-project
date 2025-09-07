package org.bank.catch

object RedisKeyProvider {
    private const val BACK_MUTEX_KEY = "bankMutex"
    private const val HISTORY_CATCH_KEY = "history"

    fun backMutexKey(ulid: String, accountUlid: String): String {
        return "$BACK_MUTEX_KEY:$ulid:$accountUlid"
    }

    fun historyCatchKey(ulid: String, accountUlid: String): String {
        return "$HISTORY_CATCH_KEY:$ulid:$accountUlid"
    }
}