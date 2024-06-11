package com.example.tubesuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val cartItems: List<CartItemModel>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.cartItemName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.cartItemDescription)
        val priceTextView: TextView = itemView.findViewById(R.id.cartItemPrice)
        val deleteButton: Button = itemView.findViewById(R.id.deleteCartItemButton)
        val quantityTextView:TextView= itemView.findViewById(R.id.textViewQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]
        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description
        holder.priceTextView.text = currentItem.price.toString()
        holder.quantityTextView.text = "Quantity: ${currentItem.quantity}"

        holder.deleteButton.setOnClickListener {
            currentItem.id?.let { id -> onDeleteClick(id) }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
