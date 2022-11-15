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

    @GET("orders/findByClientAndStatus/{id_client}/{status}")
    fun getOrdersByClientAndStatus(
        @Path("id_client") idClient:String,
        @Path("status") status:String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Order>> //Esto es lo que estamos esperando en recibir en este call

    @GET("orders/findByDeliveryAndStatus/{id_delivery}/{status}")
    fun getOrdersByDeliveryAndStatus(
        @Path("id_delivery") idDelivery:String,
        @Path("status") status:String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Order>> //Esto es lo que estamos esperando en recibir en este call

    @POST("orders/create")
    fun create(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("orders/updateToDispatched")
    fun updateToDispatched(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("orders/updateToOnTheWay")
    fun updateToOnTheWay(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}