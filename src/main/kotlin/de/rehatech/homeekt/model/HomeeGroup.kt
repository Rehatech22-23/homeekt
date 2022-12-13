package de.rehatech.homeekt.model

data class HomeeGroup(
    var nodes: ArrayList<HomeeNode>,
    var id: Int,
    var name: String,
    var image: String,
    var order: Int,
    var added: Int,
    var state: Int,
    var category: Int,
    var phonetic_name: String,
    var services: Int,
    var owner: Int

)

