package com.example.tubesuser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val context: Context,
    private val menuList: List<FoodModel>
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.menuName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.menuDescription)
        val priceTextView: TextView = itemView.findViewById(R.id.menuPrice)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedFood = menuList[position]
                    onItemClickCallback?.onItemClicked(selectedFood)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = menuList[position]
        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description
        holder.priceTextView.text = currentItem.price.toString()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
    interface OnItemClickCallback{
        fun onItemClicked(data:FoodModel)
    }
}
