package com.alex.deliveryapp.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Address(
    val id:String? = null,
    @SerializedName("id_user")
    val idUser:String,
    val address:String,
    val neighborhood:String,
    val lat:Double,
    val lng:Double

) {
    override fun toString(): String {
        return "Address(id=$id, idUser='$idUser', address='$address', neighborhood='$neighborhood', lat=$lat, lng=$lng)"
    }

    fun toJson(): String{
        return Gson().toJson(this)
    }
}