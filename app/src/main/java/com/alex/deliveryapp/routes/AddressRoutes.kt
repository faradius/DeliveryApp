package com.alex.deliveryapp.routes

import com.alex.deliveryapp.models.Address
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
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

interface AddressRoutes {

    @GET("address/findByUser/{id_user}")
    fun getAddress(
        @Path("id_user") idUser:String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Address>> //Esto es lo que estamos esperando en recibir en este call


    @POST("address/create")
    fun create(
        @Body address: Address,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}