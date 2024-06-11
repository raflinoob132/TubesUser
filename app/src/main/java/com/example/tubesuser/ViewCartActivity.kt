    // ViewCartActivity.kt
    package com.example.tubesuser

    import android.os.Bundle
    import android.widget.Button
    import android.widget.EditText
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.firebase.database.*

    class ViewCartActivity : AppCompatActivity() {

        private lateinit var dbRefCart: DatabaseReference
        private lateinit var dbRefOrders: DatabaseReference
        private lateinit var recyclerViewCart: RecyclerView
        private lateinit var cartAdapter: CartAdapter
        private lateinit var cartItems: MutableList<CartItemModel>
        private lateinit var buttonPlaceOrder: Button
        private lateinit var editTextCustomerName: EditText
        private lateinit var textViewTotalPrice: TextView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_view_cart)

            dbRefCart = FirebaseDatabase.getInstance().getReference("cart")
            dbRefOrders = FirebaseDatabase.getInstance().getReference("pending_orders")

            recyclerViewCart = findViewById(R.id.recyclerViewCart)
            buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder)
            editTextCustomerName = findViewById(R.id.editTextCustomerName)
            textViewTotalPrice = findViewById(R.id.textViewTotalPrice)

            recyclerViewCart.layoutManager = LinearLayoutManager(this)

            cartItems = mutableListOf()

            cartAdapter = CartAdapter(cartItems) { itemId ->
                removeFromCart(itemId)
            }

            recyclerViewCart.adapter = cartAdapter

            loadCartItems()

            buttonPlaceOrder.setOnClickListener {
                placeOrder()
            }
        }

        private fun loadCartItems() {
            dbRefCart.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartItems.clear()
                    var totalPrice = 0.0
                    for (cartSnapshot in snapshot.children) {
                        val cartItem = cartSnapshot.getValue(CartItemModel::class.java)
                        cartItem?.let {
                            cartItems.add(it)
                            totalPrice += (it.price ?: 0.0) * it.quantity
                        }
                    }
                    cartAdapter.notifyDataSetChanged()
                    textViewTotalPrice.text = "Total Price: $$totalPrice"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ViewCartActivity, "Failed to load cart items: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun removeFromCart(itemId: String) {
            dbRefCart.child(itemId).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun placeOrder() {
            val customerName = editTextCustomerName.text.toString().trim()
            if (customerName.isEmpty()) {
                editTextCustomerName.error = "Please enter your name"
                return
            }

            val orderId = dbRefOrders.push().key ?: return

            val orderItems = cartItems.filter { it.id != null }.associateBy { it.id!! }
            val totalPrice = cartItems.sumOf { (it.price ?: 0.0) * it.quantity }

            val order = OrderModel(orderId, customerName, orderItems, totalPrice)

            dbRefOrders.child(orderId).setValue(order).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dbRefCart.removeValue().addOnCompleteListener {
                        Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


