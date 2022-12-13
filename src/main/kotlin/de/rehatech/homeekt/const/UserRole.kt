package de.rehatech.homeekt.const

enum class UserRole(val id: Float) {
    SERVICE(1F),
    ADMIN(2F),
    STANDARD(3F),
    LIMITED(4F),
    CAUSEROLE_EXTERNAL_SERVICE(5F)
}