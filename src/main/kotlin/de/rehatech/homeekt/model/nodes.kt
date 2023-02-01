package de.rehatech.homeekt.model

import kotlinx.serialization.Serializable

@Serializable
data class nodes(
    var attributes: ArrayList<attributes>,
    var id: String,
    var name: String,
    var profile:Int,
    var image: String,
    var favorite:Int,
    var order:Int,
    var protocol:Int,
    var routing:Int,
    var state:Int,
    var state_change:Int,
    var added:Int,
    var history:Int,
    var cube_type:Int,
    var note: String,
    var services:Int,
    var phonetic_name: String,
    var owner:Int,
    var security: Int,


    )
