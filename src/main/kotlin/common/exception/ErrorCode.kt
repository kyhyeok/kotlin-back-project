package org.bank.common.exception

interface CodeInterface {
    val code: Int
    var message: String
}

enum class ErrorCode(
    override val code: Int,
    override var message: String
) : CodeInterface {
    AUTH_CONFIG_NOT_FOUND(-100, "auth config not found"),
    FAILED_TO_CALL_CLIENT(-101, "failed to call client"),
    CALL_REQUEST_BODY_NULL(-102, "body is null"),
    PROVIDER_NOT_FOUND(-103, "provider not found"),
    TOKEN_IS_INVALID(-104, "token invalid"),
    TOKEN_IS_EXPIRED(-105, "token expired"),
    FAILED_TO_INVOKE_IN_LOGGER(-106, "failed to invoke in logger"),
}