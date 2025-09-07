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
    FAILED_TO_SAVE_DATA(-107, "failed to save data"),
    FAILED_TO_FIND_ACCOUNT(-108, "failed to find account"),
    MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID(-109, "failed to matching ulid"),
    ACCOUNT_BALANCE_IS_NOT_ZERO(-110, "account balance is not zero"),
    FAILED_TO_MUTEX_INVOKE(-111, "failed to mutex invoke"),
}