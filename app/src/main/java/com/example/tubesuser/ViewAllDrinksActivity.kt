package com.example.tubesuser

import android.content.Intent
import android.os.Bundle
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

class ViewAllDrinksActivity : AppCompatActivity() {
    private lateinit var dbRefFoods: DatabaseReference
    private lateinit var recyclerViewAllDrinks: RecyclerView
    private lateinit var foodAdapter: MenuAdapter
    private lateinit var allFoods: MutableList<FoodModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_drinks)
        dbRefFoods = FirebaseDatabase.getInstance().getReference("minuman")

        recyclerViewAllDrinks = findViewById(R.id.recyclerViewAllDrinks)
        recyclerViewAllDrinks.layoutManager = LinearLayoutManager(this)

        allFoods = mutableListOf()
        foodAdapter = MenuAdapter(this,allFoods)
        foodAdapter.setOnItemClickCallback(object : MenuAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FoodModel) {
                val bundle = Bundle().apply {
                    putString("id", data.id)
                    putString("name", data.name)
                    putString("description", data.description)
                    putString("price", data.price.toString())
                }
                Intent(this@ViewAllDrinksActivity, FoodDetailActivity::class.java).also {
                    it.putExtra(FoodDetailActivity.DATA,bundle)
                    startActivity(it)
                }
            }

        })

        recyclerViewAllDrinks.adapter = foodAdapter

        loadAllFoods()
    }

    private fun loadAllFoods() {
        dbRefFoods.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allFoods.clear()
                for (foodSnapshot in snapshot.children) {
                    val food = foodSnapshot.getValue(FoodModel::class.java)
                    food?.let { allFoods.add(it) }
                }
                foodAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewAllDrinksActivity, "Failed to load foods: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}