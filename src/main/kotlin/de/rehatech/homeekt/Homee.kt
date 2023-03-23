package de.rehatech.homeekt

import com.google.gson.Gson
import de.rehatech.homeekt.model.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import okhttp3.internal.wait
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

class Homee(private val host:String, private val user: String, private val password: String): WebSocketListener() {
    private var device: String = "homeekt"
    private var pingInterval: Int = 30
    private var reconnectInterval: Int = 5
    private var reconnect: Boolean = true
    private var maxRetries: Int = 5
    private var token: String? = null
    private var expiredTime: Long? = null

    private var webSocket: WebSocket? = null

    private var homeeSettings: settings? = null
    private val gson = Gson()

    var nodeslist: ArrayList<nodes> = arrayListOf()
    var groupslist: ArrayList<HomeeGroup> = arrayListOf()


    //https://stackoverflow.com/questions/46510338/sha-512-hashing-with-android
    private fun getSHA512(input:String):String{
        return MessageDigest
            .getInstance("SHA-512")
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

     fun getBasicString():String{

        val userstring = "${user}:${getSHA512(password)}"
        val encodedString: String = Base64.getEncoder().encodeToString(userstring.toByteArray())
        println(encodedString)
        return "Basic $encodedString"
    }

    fun get_access_token(){


        val boy = "device_name=homeeApi&device_hardware_id=homee-api&device_os=5&device_type=0&device_app=1"

        val boy2 = boy.toRequestBody()


        val request = Request.Builder()
            .url("${getUrl()}/access_token")
            .addHeader("Authorization", getBasicString())
            .addHeader("'Content-Type'", "'application/x-www-form-urlencoded'")
            .post(boy2)

            .build()


        val client = OkHttpClient.Builder()
            .connectTimeout(4500,TimeUnit.MILLISECONDS)
            .build()
        val response: Response = client.newCall(request).execute()
        val cookie = response.headers("Set-Cookie")
        if (cookie.size != 1)
        {
            println("Fehler")
        }
        val cookie1 = cookie[0].split(";")
        val listcookie = cookie1[0].split("=")
        token = listcookie[1]
        var time = cookie1[1].split("=")[1]
        expiredTime = System.currentTimeMillis() + time.toLong()


    }

    fun run()
    {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${getWsUrl()}/connection?access_token=${token}")
            .addHeader("Sec-WebSocket-Protocol", "v2")
            .build()
        webSocket = client.newWebSocket(request,this)

    }

    private fun getUrl():String
    {
        val url = "http://$host:7681"
        return url
    }

    private  fun getWsUrl():String
    {
        val url = "ws://$host:7681"
        return url
    }






    fun handleMessage(jsonObject: JSONObject)
    {
        if (jsonObject.has("all"))
        {
            val all = jsonObject.getJSONObject("all")
            println(all.toString())
            val nodejson = all.get("nodes")
            val nodelist = gson.fromJson(nodejson.toString(), Array<nodes>::class.java)
            for (node in nodelist)
            {
                update_or_create_node(node)
            }


        }
        else if (jsonObject.has("attribute"))
        {
            val attjson = jsonObject.get("attribute")
            val attlist = gson.fromJson(attjson.toString(), attributes::class.java)

            updateAttribute(attlist)

        }
        else if (jsonObject.has("nodes"))
        {
            val nodejson = jsonObject.get("nodes")
            val nodelist = gson.fromJson(nodejson.toString(), Array<nodes>::class.java)
            for (node in nodelist)
            {
                update_or_create_node(node)
            }
        }


    }

    private fun update_or_create_node(node: nodes)
    {
        val exnode = getNodeById(node.id)
        if (exnode==null)
        {
            nodeslist.add(node)
        }
        else
        {
            nodeslist.remove(exnode)
            nodeslist.add(node)
        }
    }

    private fun getNodeById(id: Int):nodes?
    {
        for(node in nodeslist)
        {
            if (node.id == id)
            {
                return node
            }
        }
        return null
    }

    private  fun updateAttribute(attlist:attributes) {
        val exnode = getNodeById(attlist.node_id)

        if (exnode != null) {
            nodeslist.remove(exnode)

            exnode.attributes.clear()
            exnode.attributes.addAll(listOf(attlist))
            nodeslist.add(exnode)

        }
    }

    fun connect()
    {
        if(token == null) {
            get_access_token()
            run()
        }
        else if (expiredTime!! <= System.currentTimeMillis())
        {
            get_access_token()
            run()
        }

    }


    /**
     * Methode to update all Nodes
     * @return Boolean?
     *
     */
    fun getallNodes():Boolean?
    {

        connect()
        var responsSend:Boolean? = null
        webSocket.runCatching {
             responsSend= this?.send("GET:all")


        }
        Thread.sleep(2000L)
        return responsSend
    }

    /**
     * Methode to send a command to Homee
     * @param nodeId
     * @param attributesId
     * @param targetValue
     * @return Boolean?
     */
    fun sendNodeBefehl(nodeId:Int, attributesId:Int, targetValue:Float):Boolean?
    {
        connect()
        var responsSend:Boolean? = null
        webSocket.runCatching {
            responsSend= this?.send("PUT:/nodes/${nodeId}/attributes/${attributesId}?target_value=${targetValue}")


        }
        Thread.sleep(2000L)
        return responsSend



    }
    override fun onOpen(webSocket: WebSocket, response: Response) {
        println(response)
        getallNodes()


    }


    override fun onMessage(webSocket: WebSocket, text: String) {
        val jsonObject = JSONObject(text)
        handleMessage(jsonObject)

    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println(t)
        println(response)
        super.onFailure(webSocket, t, response)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        println(code)
        webSocket.close(code,null)
    }



}