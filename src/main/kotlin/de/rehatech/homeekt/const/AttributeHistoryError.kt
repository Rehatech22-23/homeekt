package de.rehatech.homeekt.const

enum class AttributeHistoryError(val id: Float) {
    TIMEOUT(1F),
    NO_CONNECTION(2F),
    WRONG_HOSTNAME(3F),
    DISABLED(4F),
    UNKNOWN_CURL_CODE(5F),
    EMPTY(6F)
}