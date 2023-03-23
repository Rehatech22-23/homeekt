package de.rehatech.homeekt.model

import kotlinx.serialization.Serializable

@Serializable
data class options(
    var history: History,
    var can_observe: ArrayList<Int>,
    var automations: ArrayList<String>

)
