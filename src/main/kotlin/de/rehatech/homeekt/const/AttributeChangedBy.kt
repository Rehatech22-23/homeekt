package de.rehatech.homeekt.const

enum class AttributeChangedBy(val id: Float) {
    NONE(0F),
    ITSELF(1F),
    USER(2F),
    HOMEEGRAM(3F),
    AI(6F)
}