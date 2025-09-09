package org.bank.common.message

import org.bank.common.exception.CustomException
import org.bank.common.exception.ErrorCode
import org.bank.common.logging.Logging
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class KafkaProducer(
    private val template: KafkaTemplate<String, Any>,
    private val log: Logger = Logging.getLogger(KafkaProducer::class.java)
) {
    fun sendMessage(topic: String, message: Any) {
        val future = template.send(topic, message)

        future.whenComplete { result, ex ->
            if (ex == null) {
                // 메시지 전송 성공
                log.info("메시지 발행 성공 - topic: ${topic}  - time: ${LocalDateTime.now()}")
            } else {
                throw CustomException(ErrorCode.FAILED_TO_SEND_MESSAGE, topic)
            }
        }
    }
}