package com.alex.deliveryapp.providers

import com.alex.deliveryapp.api.ApiRoutes
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.routes.UsersRoutes
import retrofit2.Call

class UsersProvider {
    private var userRoutes: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        userRoutes = api.getUsersRoutes()
    }

    fun register(user:User): Call<ResponseHttp>?{
        return userRoutes?.register(user)
    }

    fun login(email:String, password:String): Call<ResponseHttp>?{
        return userRoutes?.login(email,password)
    }
}