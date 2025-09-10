package org.bank.common.exception

class CustomException(
    private val codeInterface: CodeInterface,
    private val additionMessage: String? = null
) : RuntimeException(
    if (additionMessage == null) {
        codeInterface.message
    } else {
        "${codeInterface.message} - $additionMessage"
    }
) {
    fun getCodeInterface(): CodeInterface {
        var codeInterface = codeInterface;

        if (additionMessage != null) {
            codeInterface.message += additionMessage.toString()
        }

        return codeInterface
    }
}