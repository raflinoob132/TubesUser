package com.example.tubesuser

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {

    private lateinit var dbRefFoods: DatabaseReference
    private lateinit var dbRefDrinks: DatabaseReference
    private lateinit var recyclerViewRecommendedFoods: RecyclerView
    private lateinit var recyclerViewRecommendedDrinks: RecyclerView
    private lateinit var foodAdapter: MenuAdapter
    private lateinit var drinkAdapter: MenuAdapter
    private lateinit var recommendedFoods: MutableList<FoodModel>
    private lateinit var recommendedDrinks: MutableList<FoodModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        dbRefFoods = FirebaseDatabase.getInstance().getReference("makanan")
        dbRefDrinks = FirebaseDatabase.getInstance().getReference("minuman")

        recyclerViewRecommendedFoods = findViewById(R.id.recyclerViewRecommendedFoods)
        recyclerViewRecommendedDrinks = findViewById(R.id.recyclerViewRecommendedDrinks)
        val buttonViewAllFoods: Button = findViewById(R.id.buttonViewAllFoods)
        val buttonViewAllDrinks: Button = findViewById(R.id.buttonViewAllDrinks)

        recyclerViewRecommendedFoods.layoutManager = LinearLayoutManager(this)
        recyclerViewRecommendedDrinks.layoutManager = LinearLayoutManager(this)

        recommendedFoods = mutableListOf()
        recommendedDrinks = mutableListOf()

        foodAdapter = MenuAdapter(recommendedFoods)
        drinkAdapter = MenuAdapter(recommendedDrinks)

        recyclerViewRecommendedFoods.adapter = foodAdapter
        recyclerViewRecommendedDrinks.adapter = drinkAdapter

        loadRecommendedFoods()
        loadRecommendedDrinks()

        buttonViewAllFoods.setOnClickListener {
            // Implement view all foods functionality
        }

        buttonViewAllDrinks.setOnClickListener {
            // Implement view all drinks functionality
        }
    }

    private fun loadRecommendedFoods() {
        dbRefFoods.orderByChild("recommended").equalTo(true).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recommendedFoods.clear()
                for (foodSnapshot in snapshot.children) {
                    val food = foodSnapshot.getValue(FoodModel::class.java)
                    food?.let { recommendedFoods.add(it) }
                }
                foodAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserActivity, "Failed to load recommended foods: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadRecommendedDrinks() {
        dbRefDrinks.orderByChild("recommended").equalTo(true).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recommendedDrinks.clear()
                for (drinkSnapshot in snapshot.children) {
                    val drink = drinkSnapshot.getValue(FoodModel::class.java)
                    drink?.let { recommendedDrinks.add(it) }
                }
                drinkAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserActivity, "Failed to load recommended drinks: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}