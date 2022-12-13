package de.rehatech.homeekt.model

data class HomeeAttribute(
    var id: Int,
    var node_id: Int,
    var instance: Int,
    var minimum: Int,
    var maximum:Int,
    var current_value: Int,
    var target_value: Int,
    var last_value: Int,
    var unit: String,
    var step_value: Int,
    var editable: Boolean,
    var type: Int,
    var state: Int,
    var last_change:Int,
    var changed_by:Int,
    var changed_by_id: Int,
    var based_on: Int,
    var name: String,
    var data:String

)
