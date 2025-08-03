package com.example.websockets1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.Toast



class StartActivity : AppCompatActivity() {

    private lateinit var ipAddress: EditText
    private lateinit var port: EditText
    private lateinit var btnConnect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        ipAddress = findViewById(R.id.ipAddress)
        port = findViewById(R.id.port)
        btnConnect = findViewById(R.id.btnConnect)

        btnConnect.setOnClickListener {
            val ip = ipAddress.text.toString().trim()
            val portValue = port.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (ip.isNotEmpty() && portValue.isNotEmpty()) {
                // Pasar la IP y el Puerto a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("IP_ADDRESS", ip)
                intent.putExtra("PORT", portValue)
                startActivity(intent)
                finish()  // Finalizamos la actividad StartActivity para que no quede en el stack
            } else {
                // Si los campos están vacíos, mostrar un mensaje
                Toast.makeText(this, "Por favor ingresa la dirección IP y el puerto", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var btnUp: Button
    private lateinit var btnDown: Button
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var btnStop: Button
    private lateinit var btnRight90: Button
    private lateinit var btnRight180: Button
    private lateinit var btnLeft90: Button
    private lateinit var btnLeft180: Button

    private lateinit var ipAddress: String
    private lateinit var port: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener los valores de IP y Puerto desde el Intent
        ipAddress = intent.getStringExtra("IP_ADDRESS") ?: ""
        port = intent.getStringExtra("PORT") ?: ""

        // Verificar si los valores de IP y puerto no están vacíos
        if (ipAddress.isNotEmpty() && port.isNotEmpty()) {
            val url = "ws://$ipAddress:$port/" // Construir la URL del WebSocket
            WebSocketManager.connect(url)

            // Si necesitas mostrar un mensaje de conexión
            Toast.makeText(this, "Conectando a $url...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "IP y Puerto inválidos", Toast.LENGTH_SHORT).show()
        }

        // Inicializar los botones
        btnUp = findViewById(R.id.btnUp)
        btnDown = findViewById(R.id.btnDown)
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        btnStop = findViewById(R.id.btn_stop)
        btnRight90 = findViewById(R.id.right90)
        btnRight180 = findViewById(R.id.right180)
        btnLeft90 = findViewById(R.id.left90)
        btnLeft180 = findViewById(R.id.left180)

        setupButtonWithTouch(btnUp, "forward")
        setupButtonWithTouch(btnDown, "backward")
        setupButtonWithTouch(btnLeft, "left")
        setupButtonWithTouch(btnRight, "right")

        btnStop.setOnClickListener {
            WebSocketManager.sendMessage("stop")
            Log.d("Websockets", "Botón STOP presionado → Enviando: stop")
        }

        btnRight90.setOnClickListener {
            WebSocketManager.sendMessage("right90")
            Log.d("Websockets", "Funcion Especial Giro Derecha a 90 grados")
        }

        btnRight180.setOnClickListener {
            WebSocketManager.sendMessage("right180")
            Log.d("Websockets", "Funcion Especial Giro Derecha a 180 grados")
        }

        btnLeft90.setOnClickListener {
            WebSocketManager.sendMessage("left90")
            Log.d("Websockets", "Funcion Especial Giro Izquierda a 90 grados")
        }

        btnLeft180.setOnClickListener {
            WebSocketManager.sendMessage("left180")
            Log.d("Websockets", "Funcion Especial Giro Izquierda a 180 grados")
        }
    }

    private fun setupButtonWithTouch(button: Button, direction: String) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("Websockets", "Botón $direction presionado")
                    WebSocketManager.sendMessage(direction)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    Log.d("Websockets", "Botón $direction liberado → stop")
                    WebSocketManager.sendMessage("stop")
                    v.performClick()
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.close()
    }
}