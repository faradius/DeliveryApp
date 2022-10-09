package com.alex.deliveryapp.models

import com.google.gson.annotations.SerializedName

class Rol(
    @SerializedName("id") val id:String,
    @SerializedName("name") val rol:String,
    @SerializedName("image") val image:String,
    @SerializedName("route") val route:String
) {
    override fun toString(): String {
        return "Rol(id='$id', rol='$rol', image='$image', route='$route')"
    }
}