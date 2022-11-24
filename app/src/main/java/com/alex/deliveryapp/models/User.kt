package com.alex.deliveryapp.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User(
    //Como se esta trabajando con BIGSERIAL en la base de datos puede ser String el id
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    var name: String,
    @SerializedName("lastname")
    var lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("session_token")
    val sessionToken: String? = null,
    @SerializedName("notification_token")
    var notificationToken: String? = null,
    @SerializedName("is_available")
    val isAvailable: Boolean? = null,
    @SerializedName("roles")
    val roles: ArrayList<Rol>? = null
){
    override fun toString(): String {
        return "$name $lastName"
    }

    //Transformamos este tipo de modelo a un tipo JSON
    fun toJson(): String{
        return Gson().toJson(this)
    }
}