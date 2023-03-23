package de.rehatech.homeekt.model

import kotlinx.serialization.Serializable
import org.json.JSONPropertyIgnore

@Serializable
data class attributes(
    var id: Int,
    var node_id: Int,
    var instance: Int,
    var minimum: Int,
    var maximum:Int,
    var current_value: Double,
    var target_value: Double,
    var last_value: Double,
    var unit: String,
    var step_value: Double,
    var editable: Int,
    var type: Int,
    var state: Int,
    var last_change:Int,
    var changed_by:Int,
    var changed_by_id: Int,
    var based_on: Int,
    var name: String,
    var data:String,
    //var options: options


)
