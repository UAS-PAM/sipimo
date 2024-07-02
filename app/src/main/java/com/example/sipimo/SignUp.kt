package com.example.sipimo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUp : AppCompatActivity() {
    private lateinit var backBtn : ImageButton
    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var fullnameEditText : EditText
    private lateinit var usernameEditText : EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var registerBtn: Button
    private lateinit var terms: CheckBox
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        init var
        usernameEditText = findViewById(R.id.etUsername)
        fullnameEditText = findViewById(R.id.etFullname)
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        repeatPasswordEditText = findViewById(R.id.etRepeatPassword)
        registerBtn = findViewById(R.id.registerBtn)
        terms = findViewById(R.id.cbTerms)
        database = FirebaseDatabase.getInstance("https://sipimo-pam-default-rtdb.asia-southeast1.firebasedatabase.app/")
        myRef = database.getReference("users")
        auth = FirebaseAuth.getInstance()

        registerBtn.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val fullname = fullnameEditText.text.toString().trim()
            val email= emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val repeatPassword = repeatPasswordEditText.text.toString().trim()

            if (
                username.isEmpty() ||
                fullname.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                repeatPassword.isEmpty()
                ) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                Toast.makeText(this, "Password and RepeatPassword is not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!terms.isChecked){
                Toast.makeText(this, "Please checked our Agreement", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                if (it.isSuccessful){
                    val userId = auth.currentUser?.uid
                    val user = userId?.let {
                        User(userId, username, email, fullname)
                    }
                    if (userId != null) {
                        myRef.child(userId).setValue(user).addOnCompleteListener{
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }



        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            startActivity(Intent(this, LandingPage::class.java))
            finish()
        }
    }
}

