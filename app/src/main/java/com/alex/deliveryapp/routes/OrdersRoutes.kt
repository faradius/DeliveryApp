package com.alex.deliveryapp.routes

import com.alex.deliveryapp.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface OrdersRoutes {

    @GET("orders/findByStatus/{status}")
    fun getOrdersByStatus(
        @Path("status") status:String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Order>> //Esto es lo que estamos esperando en recibir en este call


    @POST("orders/create")
    fun create(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}