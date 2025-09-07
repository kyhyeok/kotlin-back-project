package org.bank.catch

import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode.FAILED_TO_GET_LOCK
import org.bank.common.exception.ErrorCode.FAILED_TO_MUTEX_INVOKE
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit.SECONDS

@Component
class RedisClient(
    private val template: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {

    fun get(key: String): String? {
        return template.opsForValue().get(key)
    }

    fun <T> get(key: String, kSerializer: (Any) -> T?): T? {
        val value = get(key)

        value?.let { return kSerializer(it) } ?: return null
    }

    fun setIfNotExist(key: String, value: String): Boolean {
        return template.opsForValue().setIfAbsent(key, value) ?: false
    }

    fun <T> invokeWithMutex(key: String, function: () -> T?): T? {
        val lock = redissonClient.getLock(key)
        var lockAcquired = false
        try {
            lockAcquired = lock.tryLock(10, 15, SECONDS)

            if (!lockAcquired) {
                throw CustomException(FAILED_TO_GET_LOCK, key)
            }

            return function.invoke()
        } catch (e: Exception) {
            throw CustomException(FAILED_TO_MUTEX_INVOKE, e.message)
        } finally {
            if (lockAcquired && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}