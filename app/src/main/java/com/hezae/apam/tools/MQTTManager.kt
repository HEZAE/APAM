package com.hezae.apam.tools

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttActionListener
import org.eclipse.paho.mqttv5.client.MqttAsyncClient
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.MqttConnectionOptionsBuilder
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties
import java.util.UUID

object MQTTManager : MqttCallback {
    private var mqttClient: MqttAsyncClient? = null // MQTT 客户端实例
    private lateinit var mqttConnectionOptions: MqttConnectionOptions
    private var isInitialized = false
    private val SERVER_URI = "tcp://192.168.20.27:1883"
    private val CLIENT_ID = UserInfo.username
    private val USERNAME = "admin"
    private val PASSWORD = "public"

    var pictureByteArray = mutableStateOf<ByteArray?>(null)

    fun init(context: Context){
        mqttConnectionOptions = MqttConnectionOptionsBuilder()
            .serverURI(SERVER_URI)
            .username(USERNAME)
            .automaticReconnect(true)
            .connectionTimeout(3)// 连接超时时间3s
            .password(PASSWORD.toByteArray())
            .automaticReconnectDelay(1, 3)// 最小1秒，最大3秒
            .cleanStart(false)
            .keepAliveInterval(60)// 心跳包发送时间60s
            .build()
        try {
            mqttClient = MqttAsyncClient(
                SERVER_URI,
                CLIENT_ID + UUID.randomUUID().toString().substring(0, 6),
                MqttDefaultFilePersistence(context.getExternalFilesDir(null)?.absolutePath)).apply {
                setCallback(this@MQTTManager)
            }
            isInitialized = true
        }catch (e: MqttException) {
            isInitialized = false
            Log.e("MQTT", "初始化失败:${e.message}")
            return
        }
    }
    fun connect() {
        if (isInitialized) {
            mqttClient?.connect(mqttConnectionOptions, null, object : MqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    subscribeTopic("UserEventIn")
                    Log.d("MQTT", "连接成功")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MQTT", "连接失败:${exception?.message}")
                    reconnect()
                }
            })
        }else{
            Log.e("MQTT", "MQTT未初始化")
        }
    }
    private var reconnectJob: Job? = null // 用于管理重连协程的Job
    // 重连方法,解决首次连接失败问题
    private fun reconnect() {
        // 取消之前的重连任务
        reconnectJob?.cancel()
        // 启动新的协程进行重连
        reconnectJob = CoroutineScope(Dispatchers.IO).launch {
            val maxRetries = 100
            var attempt = 0
            while (attempt < maxRetries) {
                delay(3000) // 等待 2 秒
                try {
                    connect() // 尝试重新连接
                    return@launch // 如果成功连接，则退出
                } catch (e: Exception) {
                    Log.e("MQTT", "重连失败: ${e.message}")
                    attempt++
                }
            }
            Log.e("MQTT", "重连失败，已达到最大重连次数")
        }
    }

    //订阅主题
    fun subscribeTopic(topic: String) {
        if (isInitialized&& mqttClient!!.isConnected) {
            mqttClient!!.subscribe(topic, 0, null, object : MqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "订阅成功: $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MQTT", "订阅失败: $topic,${exception?.message}")
                }
            })
        }
    }

    //发送消息
    fun publishMessage(topic: String, message:ByteArray) {
        if (isInitialized) {
           if(mqttClient!=null){
               if (mqttClient!!.isConnected){
                   val mqttMessage = MqttMessage(message)
                   mqttClient!!.publish(topic, mqttMessage, null, object : MqttActionListener {
                       override fun onSuccess(asyncActionToken: IMqttToken?) {
                           Log.d("MQTT", "消息发送成功: $topic")
                       }

                       override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                           Log.e("MQTT", "消息发送失败: $topic, ${exception?.message}")
                       }
                   })
               }
           }
        }
    }

    // 消息到达
    override fun messageArrived(topic: String?, message: MqttMessage?) {
       if (topic == "UserEventIn"){
           pictureByteArray.value = message?.payload
       }
    }

    override fun disconnected(disconnectResponse: MqttDisconnectResponse?) {
        Log.d("MQTT", "断开连接")
    }

    override fun mqttErrorOccurred(exception: MqttException?) {
    }



    override fun deliveryComplete(token: IMqttToken?) {
    }

    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
    }

    override fun authPacketArrived(reasonCode: Int, properties: MqttProperties?) {
    }

}