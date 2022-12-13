package de.rehatech.homeekt.const

enum class HomeegramState(val id: Float){
    NONE(0F),
    NORMAL(1F),
    EXECUTING(2F),
    NO_TRIGGERS(3F),
    NO_ACTIONS(4F)
}