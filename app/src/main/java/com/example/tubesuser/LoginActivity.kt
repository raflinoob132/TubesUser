// com/example/tubesuser/LoginActivity.kt
package com.example.tubesuser


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbRef = FirebaseDatabase.getInstance().getReference("users")

        val idEditText: EditText = findViewById(R.id.idEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val userId = idEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                dbRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserModel::class.java)
                        if (user != null && user.password == password) {
                            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, UserActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid ID or password", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter ID and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
