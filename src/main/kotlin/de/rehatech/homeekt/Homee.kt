package de.rehatech.homeekt

class Homee(val host:String, val user: String, val password: String) {
    var device: String = "pymee";
    var pingInterval: Int = 30;
    var reconnectInterval: Int = 5;
    var reconnect: Boolean = true;
    var maxRetries: Int = 5;


    fun get_access_token(){

    }

}