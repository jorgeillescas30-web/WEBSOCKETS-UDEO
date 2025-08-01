package com.example.websockets1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button


class MainActivity : AppCompatActivity() {


    private lateinit var btnUp: Button
    private lateinit var btnDown: Button
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var btnStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WebSocketManager.connect("ws://192.168.1.2:81/")

        btnUp = findViewById(R.id.btnUp)
        btnDown = findViewById(R.id.btnDown)
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        btnStop = findViewById(R.id.btn_stop)

        setupButtonWithTouch(btnUp, "forward")
        setupButtonWithTouch(btnDown, "backward")
        setupButtonWithTouch(btnLeft, "left")
        setupButtonWithTouch(btnRight, "right")

        btnStop.setOnClickListener {
            WebSocketManager.sendMessage("stop")
            Log.d("Websockets", "Botón STOP presionado → Enviando: stop")
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