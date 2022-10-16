package com.alex.deliveryapp.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Product(
    val id: String? = null ,
    val name: String,
    val description: String,
    val price: Double,
    val image1: String? = null,
    val image2: String? = null,
    val image3: String? = null,
    @SerializedName("id_category") val idCategory: String,
    val quantity: Int? = null
) {
    fun toJson():String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Product(id='$id', name='$name', description='$description', price=$price, image1='$image1', image2='$image2', image3='$image3', idCategory='$idCategory', quantity=$quantity)"
    }


}