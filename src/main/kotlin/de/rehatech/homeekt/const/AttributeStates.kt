package de.rehatech.homeekt.const

enum class AttributeStates(val id: Float) {
    NONE(0F),
    NORMAL(1F),
    WAITING_FOR_WAKE_UP(2F),
    WAITING_FOR_VALUE(3F),
    WAITING_FOR_ACKNOWLEDGE(4F),
    INACTIVE(5F),
    IGNORED(6F),
    UNAVAILABLE(7F)
}