package com.example.tubesuser

data class CartItemModel(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    var quantity: Int = 1

)
