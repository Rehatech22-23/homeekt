package de.rehatech.homeekt

import com.google.gson.Gson
import de.rehatech.homeekt.model.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.w3c.dom.Node
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import kotlin.time.Duration

class Homee(val host:String, val user: String, val password: String): WebSocketListener() {
    var device: String = "homeekt";
    var pingInterval: Int = 30;
    var reconnectInterval: Int = 5;
    var reconnect: Boolean = true;
    var maxRetries: Int = 5;
    var token: String? = null;
    var expiredTime: Long? = null;

    var webSocket: WebSocket? = null;

    var homeeSettings: settings? = null;
    val gson = Gson()

    var Nodes: ArrayList<nodes> = arrayListOf()
    var Groups: ArrayList<HomeeGroup> = arrayListOf()





    fun get_access_token(){
        val auth: String;


        var boy = "device_name=homeeApi&device_hardware_id=homee-api&device_os=5&device_type=0&device_app=1";

        var dat = data("homeeApi", "homee-api", 5 ,0 ,1)
        var boy2 = boy.toRequestBody()

        val jsonObject = JSONObject()
        jsonObject.put("device", "homeeApi")
        jsonObject.put("device_hardware_id", "homee-api")
        jsonObject.put("device_os", "5")
        jsonObject.put("device_type", "0")
        jsonObject.put("device_app", "1")



        var request = Request.Builder()
            .url("${getUrl()}/access_token")
            .addHeader("Authorization","Basic cmVoYXRlY2g6ZWFjOWMwN2Y2MzMwNTI5N2Q5MzM5MTQyZGZkOGYyNDg1NjJmOGYyMjUyNDcyNTVmYjM5Nzc5Y2M0Y2EzYWRiZDFhYmM0MGQ3ZDgzMTAxN2ViZjQyMGQ0MzBlMDQ1YzkwNDU0OWIwZjRkYzhjYjRmNmM3ZjVlMzA1YzcxNzY5MmQ=")
            .addHeader("'Content-Type'", "'application/x-www-form-urlencoded'")
            .post(boy2)

            .build();


        val client = OkHttpClient.Builder()
            .connectTimeout(2500,TimeUnit.MILLISECONDS)
            .build();
        val response: Response = client.newCall(request).execute();
        val cookie = response.headers("Set-Cookie")
        if (cookie.size != 1)
        {
            println("Fehler")
        }
        val cookie1 = cookie[0].split(";");
        val listcookie = cookie1[0].split("=")
        token = listcookie[1];
        var time = cookie1[1].split("=")[1]
        expiredTime = System.currentTimeMillis() + time.toLong();


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
        val url = "http://$host:7681";
        return url;
    }

    private  fun getWsUrl():String
    {
        val url = "ws://$host:7681";
        return url;
    }






    fun handleMessage(jsonObject: JSONObject)
    {
        println( jsonObject)
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

            val groupjson = all.get("groups")
            val grouplist = gson.fromJson(groupjson.toString(), Array<HomeeGroup>::class.java)

            val reljson = all.get("relationships")
            val rellist = gson.fromJson(reljson.toString(), Array<HomeeRelationship>::class.java)


        }
        else if (jsonObject.has("attribute"))
        {
            val attjson = jsonObject.get("attribute")
            val attlist = gson.fromJson(attjson.toString(), Array<attributes>::class.java)
            for (att in attlist){
                updateAttribute(att)
            }
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
        else if (jsonObject.has("relationships"))
        {
            val reljson = jsonObject.get("relationships")
            val rellist = gson.fromJson(reljson.toString(), Array<HomeeRelationship>::class.java)
        }
        else if (jsonObject.has("relationships"))
        {
            val groupjson = jsonObject.get("groups")
            val grouplist = gson.fromJson(groupjson.toString(), Array<HomeeGroup>::class.java)
        }

    }

    private fun update_or_create_node(node: nodes)
    {
        val exnode = getNodeById(node.id)
        if (exnode==null)
        {
            Nodes.add(node)
        }
        else
        {
            Nodes.remove(exnode)
            Nodes.add(node)
        }
    }

    private fun getNodeById(id: String):nodes?
    {
        for(node in Nodes)
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
            Nodes.remove(exnode)

            exnode.attributes.clear()
            exnode.attributes.addAll(listOf(attlist))
            Nodes.add(exnode)

        }
    }







    fun getallNodes()
    {
        webSocket?.send("GET:all")
    }
    fun sendNodeBefehl(node:Int, attributes:Int, targetValue:Double)
    {
        webSocket?.send("PUT:/nodes/${node}/attributes/${attributes}?target_value=${targetValue}")

    }
    override fun onOpen(webSocket: WebSocket, response: Response) {
        println(response)
        webSocket.send("GET:all")

    }


    override fun onMessage(webSocket: WebSocket, text: String) {
        val jsonObject = JSONObject(text)
        handleMessage(jsonObject)

    }

}