package de.rehatech.homeekt.const

enum class HomeeUpdate(val id: Float) {
    STARTING(0F),
    DOWNLOADING(1F),
    ERROR_DOWNLOADING(2F),
    PREPAIRING(3F),
    ERROR_PREPAIRING(4F),
    INSTALLING(5F),
    ERROR_INSTALLING(6F),
    SUCCESSFUL(7F)
}