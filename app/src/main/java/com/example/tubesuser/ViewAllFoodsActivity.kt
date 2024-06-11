package com.example.tubesuser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ViewAllFoodsActivity : AppCompatActivity() {

    private lateinit var dbRefFoods: DatabaseReference
    private lateinit var recyclerViewAllFoods: RecyclerView
    private lateinit var foodAdapter: MenuAdapter
    private lateinit var allFoods: MutableList<FoodModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_foods)

        dbRefFoods = FirebaseDatabase.getInstance().getReference("makanan")

        recyclerViewAllFoods = findViewById(R.id.recyclerViewAllFoods)
        recyclerViewAllFoods.layoutManager = LinearLayoutManager(this)

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
                Intent(this@ViewAllFoodsActivity, FoodDetailActivity::class.java).also {
                    it.putExtra(FoodDetailActivity.DATA,bundle)
                    startActivity(it)
                }
            }

        })

        recyclerViewAllFoods.adapter = foodAdapter

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
                Toast.makeText(this@ViewAllFoodsActivity, "Failed to load foods: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
