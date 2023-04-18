package de.rehatech.homeekt

import com.google.gson.Gson
import de.rehatech.homeekt.model.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class implies the communication between it and the Homee device.
 */
class Homee(private val host:String, private val user: String, private val password: String, private val device: String = "homeekt"): WebSocketListener() {
    private var token: String? = null
    private var expiredTime: Long? = null

    private var webSocket: WebSocket? = null

    private val gson = Gson()

    var nodeslist: ArrayList<nodes> = arrayListOf()



    //https://stackoverflow.com/questions/46510338/sha-512-hashing-with-android
    /**
     *  get a SHA512 Hash from a String
     *  @param input A String
     *  @return A String with a Hash from input String
     */
    private fun getSHA512(input:String):String{
        return MessageDigest
            .getInstance("SHA-512")
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Encoded the User and Passwort with Base64
     * @return A Base64 encoded String
     */
     fun getBasicString():String{

        val userstring = "${user}:${getSHA512(password)}"
        val encodedString: String = Base64.getEncoder().encodeToString(userstring.toByteArray())
        return "Basic $encodedString"
    }

    /**
     * The methode create a http reqest to get a access Token from Homee
     */
    fun getAccessToken(){


        val boy = "device_name=${device}&device_hardware_id=homeekt-api&device_os=5&device_type=0&device_app=1"

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
        val time = cookie1[1].split("=")[1]
        expiredTime = System.currentTimeMillis() + time.toLong()
        response.close()


    }

    /**
     * Start a websocket
     */
    fun run()
    {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${getWsUrl()}/connection?access_token=${token}")
            .addHeader("Sec-WebSocket-Protocol", "v2")
            .build()
        webSocket = client.newWebSocket(request,this)

    }

    /**
     * get the Url from Homee
     * @return return the url with Port
     */
    private fun getUrl(): String {
        return "http://$host:7681"
    }

    /**
     * get the url for websocket
     * @return
     */
    private fun getWsUrl(): String {
        return "ws://$host:7681"
    }


    /**
     * The Methode proccess the JSONObject from Homee
     * @param
     */
    fun handleMessage(jsonObject: JSONObject)
    {
        // Check the Key
        if (jsonObject.has("all"))
        {
            val all = jsonObject.getJSONObject("all")
            val nodejson = all.get("nodes")
            val nodelist = gson.fromJson(nodejson.toString(), Array<nodes>::class.java)
            for (node in nodelist)
            {
                updateOrCreateNode(node)
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
                updateOrCreateNode(node)
            }
        }


    }

    /**
     * Update or Create a Node
     */
    private fun updateOrCreateNode(node: nodes)
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


    /**
     *  get a Node by Id
     *  @param id Homee Id
     *  @return nodes
     */
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

    /**
     * The Methode update the Attribute List of a Node
     */
    private  fun updateAttribute(attlist:attributes) {
        val exnode = getNodeById(attlist.node_id)


        if (exnode != null) {
            val exatt = getAttributeById(exnode, attlist.id)
            if (exatt != null)
            {
              exnode.attributes.remove(exatt)
              exnode.attributes.add(attlist)
            }

        }
    }



    /**
     *  get a Attribute by Id
     *
     *  @param node Homee Node
     *  @param id Attribute Id
     *  @return
     */
    private  fun getAttributeById(node: nodes, id: Int): attributes?
    {
        for(att in node.attributes)
        {
            if(att.id == id)
            {
                return att
            }
        }
        return  null
    }

    /**
     * Check the connect and reconnect
     */
    fun connect()
    {
        if(token == null) {
            getAccessToken()
            run()
        }

        else if (expiredTime!! <= System.currentTimeMillis())
        {
            getAccessToken()
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
        try {
            responsSend =
                webSocket!!.send("PUT:/nodes/${nodeId}/attributes/${attributesId}?target_value=${targetValue}")

        }
        catch (ex:NullPointerException)
        {
            connect()
            sendNodeBefehl(nodeId,attributesId,targetValue)
        }

        Thread.sleep(2000L)
        return responsSend



    }
    override fun onOpen(webSocket: WebSocket, response: Response) {
        getallNodes()


    }


    override fun onMessage(webSocket: WebSocket, text: String) {
        val jsonObject = JSONObject(text)
        handleMessage(jsonObject)

    }

    override fun onFailure(webSocket2: WebSocket, t: Throwable, response: Response?) {
        println("Fehler: " + response?.code)
        response?.close()
        webSocket2.close(1000,null)
        webSocket = null
        expiredTime = null
        token = null
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        println("Closed Websocket")
        webSocket.close(code,null)
    }



}