package com.example.tubesuser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbRef = FirebaseDatabase.getInstance().getReference("users")

        val idEditText: EditText = findViewById(R.id.idEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val userId = idEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val user = UserModel(userId, password)
                dbRef.child(userId).setValue(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                            // Navigate to login screen
                        } else {
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter ID and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

