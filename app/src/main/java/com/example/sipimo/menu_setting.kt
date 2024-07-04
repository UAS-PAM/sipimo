package com.example.sipimo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class menu_setting : AppCompatActivity() {

    private lateinit var userButton: Button
    private lateinit var logoutButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_setting)

        mAuth = FirebaseAuth.getInstance()
        userButton = findViewById(R.id.userButton) // Pastikan ID ini sesuai dengan yang di XML
        logoutButton = findViewById(R.id.logout)   // Pastikan ID ini sesuai dengan yang di XML
        database = FirebaseDatabase.getInstance("https://sipimo-pam-default-rtdb.asia-southeast1.firebasedatabase.app/")

        userButton.setOnClickListener {
            val intent = Intent(this, user_setting::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            Log.d("menu_setting", "Logout button clicked")
            mAuth.signOut() // Gunakan mAuth untuk sign out
            Log.d("menu_setting", "Firebase sign out")
            sendToLandingPage()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        Log.d("menu_setting", "onStart - Current user: $currentUser")
        // Check if user has already signed in, if not send user to Landing Page
        if (currentUser == null) {
            sendToLandingPage()
        }
    }

    private fun sendToLandingPage() {
        Log.d("menu_setting", "Sending to Landing Page")
        // To send user to Landing Page
        val landingIntent = Intent(this@menu_setting, LandingPage::class.java)
        landingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(landingIntent)
        finish()
    }
}
