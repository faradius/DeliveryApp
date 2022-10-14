package com.alex.deliveryapp.routes

import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface CategoriesRoutes {

    @Multipart //Esto se pone por que se va enviar una imagen
    //Aqui se utiliza POST por que es una creación de datos y no una actualización por lo tanto no es PUT,
    //Ademas esto tambien se define en el backend web el tipo de petición que va hacer
    @POST("categories/create")
    fun create(
        @Part image: MultipartBody.Part,
        //el Part category hace refencia a la linea de codigo en el backend web (const category = JSON.parse(req.body.category);) que esta en el categoryController
        @Part("category") category: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}