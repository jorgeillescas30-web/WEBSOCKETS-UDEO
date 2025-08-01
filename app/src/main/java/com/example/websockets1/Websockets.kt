package com.example.websockets1

import android.util.Log
import okhttp3.*
import okio.ByteString

object WebSocketManager {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    var onMessageReceived: ((String) -> Unit)? = null

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d("Websockets", "Conectado al WebSocket")
            }

            override fun onMessage(ws: WebSocket, text: String) {
                Log.d("Websockets", "Mensaje recibido: $text")
                onMessageReceived?.invoke(text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("Websockets", "Error en WebSocket: ${t.message}")
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                Log.d("Websockets", "Cerrando WebSocket: $reason")
                ws.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("Websockets", "WebSocket cerrado: $reason")
            }
        })
    }

    fun sendMessage(message: String) {
        if (webSocket != null) {
            val sent = webSocket?.send(message) ?: false
            if (sent) {
                Log.d("Websockets", "Mensaje enviado: $message")
            } else {
                Log.e("Websockets", "Fallo al enviar: $message")
            }
        } else {
            Log.e("Websockets", "WebSocket no está conectado")
        }
    }

    fun close() {
        webSocket?.close(1000, "Cerrando desde app")
        webSocket = null
    }
}