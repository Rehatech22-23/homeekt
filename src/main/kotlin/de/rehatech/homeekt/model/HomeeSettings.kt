package de.rehatech.homeekt.model

data class HomeeSettings(
    var local_ssl_enabled: Boolean,
    var wlan_enabled: Int,
    var wlan_ssid: String,
    var wlan_mode: Int,
    var internet_access: Boolean,
    var lan_enabled: Int,
    var lan_ip_address:Int,
    var available_ssids: ArrayList<String>,
    var time: Int,
    var civil_time: String,
    var version: String,
    var uid: String,
    var cubes: ArrayList<String>,
    var extensions: ArrayList<String>

)
