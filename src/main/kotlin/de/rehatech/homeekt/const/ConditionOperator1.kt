package de.rehatech.homeekt.const

enum class ConditionOperator1(val id: Float){
    NONE(0F),
    RISE_ABOVE(1F),
    FALL_BELOW(2F),
    BECOME_EQUAL(3F),
    ANY_CHANGE_GREATER_THAN(4F)
}