package de.rehatech.homeekt.const

enum class ErrorCode2(val id: Float){
    MISSING_PARAMETER(1F),
    INVALID_FORMAT(2F),
    INVALID_PATH(3F),
    INVALID_VERSION(4F),
    OBJECT_NOT_FOUND(5F),
    INVALID_RESOURCE_ID(6F),
    INTERNAL_SERVER_EXCEPTION(7F),
    TOO_MANY_PARAMETERS(8F),
    INVALID_PARAMETERS(9F),
    CONFLICTING_PARAMETERS(10F)
}