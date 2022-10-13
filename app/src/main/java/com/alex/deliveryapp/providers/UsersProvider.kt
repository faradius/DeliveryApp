package com.alex.deliveryapp.providers

import com.alex.deliveryapp.api.ApiRoutes
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

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

    fun updateWithoutImage(user:User): Call<ResponseHttp>?{
        return userRoutes?.updateWithoutImage(user)
    }

    fun update(file: File, user: User): Call<ResponseHttp>?{
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file) //Este es el archivo que vamos a enviar
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile) // Este es el nombre del archivo
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())

        return userRoutes?.update(image, requestBody)
    }
}