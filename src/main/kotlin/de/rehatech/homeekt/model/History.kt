package de.rehatech.homeekt.model

import kotlinx.serialization.Serializable

@Serializable
data class History (
    var stepped: Boolean,
    var week:Int,
    var month:Int,
    var day:Int
)

