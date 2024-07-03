package com.example.sipimo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class menu_setting : AppCompatActivity() {
    private lateinit var userButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_setting)

        userButton = findViewById(R.id.userButton)

        userButton.setOnClickListener {
            val intent = Intent(this, user_setting::class.java)
            startActivity(intent)
        }
    }
}
