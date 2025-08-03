package com.example.websockets1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.content.Intent



class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val btnConnect = findViewById<Button>(R.id.btnConnect)

        btnConnect.setOnClickListener {
            // Ir directamente al gamepad (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

class MainActivity : AppCompatActivity() {


    private lateinit var btnUp: Button
    private lateinit var btnDown: Button
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var btnStop: Button
    private lateinit var btnright90 : Button
    private  lateinit var btnright180 : Button
    private  lateinit var  btnleft90 : Button
    private lateinit var btnleft180 : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WebSocketManager.connect("ws://192.168.1.2:81/")

        btnUp = findViewById(R.id.btnUp)
        btnDown = findViewById(R.id.btnDown)
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        btnStop = findViewById(R.id.btn_stop)
        btnright90 = findViewById(R.id.right90)
        btnright180 = findViewById(R.id.right180)
        btnleft90 = findViewById(R.id.left90)
        btnleft180 = findViewById(R.id.left180)

        setupButtonWithTouch(btnUp, "forward")
        setupButtonWithTouch(btnDown, "backward")
        setupButtonWithTouch(btnLeft, "left")
        setupButtonWithTouch(btnRight, "right")

        btnStop.setOnClickListener {
            WebSocketManager.sendMessage("stop")
            Log.d("Websockets", "Botón STOP presionado → Enviando: stop")
        }

        btnright90.setOnClickListener {
            WebSocketManager.sendMessage("right90")
            Log.d("Websockets", "Funcion Especial Giro Derecha a 90 grados")
        }

        btnright180.setOnClickListener {
            WebSocketManager.sendMessage("right180")
            Log.d("Websockets", "Funcion Especial Giro Derecha a 180 grados")
        }


        btnleft90.setOnClickListener {
            WebSocketManager.sendMessage("left90")
            Log.d("Websockets", "Funcion Especial Giro Izquierda a 90 grados")
        }


        btnleft180.setOnClickListener {
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
                    Log.d("Websockets", "Botón $direction liberado -> stop")
                    WebSocketManager.sendMessage("stop")
                    v.performClick()
                    true
                }

                else -> false
            }
        }
    }
}