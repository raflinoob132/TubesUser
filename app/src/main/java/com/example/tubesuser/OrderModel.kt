package com.example.tubesuser


data class OrderModel(
    val id: String? = null,
    val customerName: String,
    val items: Map<String, CartItemModel>,
    val totalPrice: Double
)