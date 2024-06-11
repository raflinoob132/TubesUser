package com.example.tubesuser

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodDetailActivity : AppCompatActivity() {

    private lateinit var dbRefCart: DatabaseReference
    private lateinit var food:FoodModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)
        food= FoodModel()

        dbRefCart = FirebaseDatabase.getInstance().getReference("cart")

        // Get the FoodModel object from the intent extra safely using key "food"
        val bundle = intent.getBundleExtra(DATA)
        val foodId = bundle?.getString("id").toString()
        val foodName = bundle?.getString("name").toString()
        val foodDescription = bundle?.getString("description").toString()
        val foodPrice = bundle?.getString("price")?.toDouble()


        // Check if food is not null before accessing its properties
        if (bundle != null) {
            val foodNameTextView: TextView = findViewById(R.id.foodName)
            val foodDescriptionTextView: TextView = findViewById(R.id.foodDescription)
            val foodPriceTextView: TextView = findViewById(R.id.foodPrice)
            val addToCartButton: Button = findViewById(R.id.cartButton)

            foodNameTextView.text = foodName
            foodDescriptionTextView.text = foodDescription
            foodPriceTextView.text = foodPrice.toString()

            food.id = foodId
            food.name = foodName
            food.description = foodDescription
            food.price = foodPrice

            addToCartButton.setOnClickListener {
                addToCart(food)
            }
        } else {
            // Handle case where food is null
            Toast.makeText(this, "Invalid food item", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        }
    }

    private fun addToCart(food: FoodModel) {
        val cartItem = CartItemModel(
            id = food.id,
            name = food.name,
            description = food.description,
            price = food.price,
            quantity = 1
        )

        dbRefCart.child(food.id!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val existingCartItem = snapshot.getValue(CartItemModel::class.java)

                if (existingCartItem != null) {
                    // Jika item sudah ada di keranjang, tambahkan kuantitasnya
                    existingCartItem.quantity = existingCartItem.quantity + 1
                    dbRefCart.child(food.id!!).setValue(existingCartItem).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@FoodDetailActivity, "Added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@FoodDetailActivity, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Jika item belum ada di keranjang, tambahkan sebagai item baru
                    dbRefCart.child(food.id!!).setValue(cartItem).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@FoodDetailActivity, "Added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@FoodDetailActivity, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FoodDetailActivity, "Failed to add to cart: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object{
        const val FOOD_ID = "food_id"
        const val DATA = "data"
    }
}