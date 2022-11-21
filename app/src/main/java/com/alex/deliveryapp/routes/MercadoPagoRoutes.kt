package com.alex.deliveryapp.routes

import com.alex.deliveryapp.models.MercadoPagoCardTokenBody
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MercadoPagoRoutes {

    @GET("v1/payment_methods/installments?access_token=TEST-1713543570431424-112019-d4b9db7d20cb9b9536cd0528d671b1e4-233084861")
    fun getInstallments(@Query("bin") bin: String, @Query("amount") amount:String): Call<JsonArray> //Es un arreglo que contiene objetos

    @POST("v1/card_tokens?public_key=TEST-dab6d675-c0a6-4439-ad0f-686dba382f2a")
    fun createCardToken(@Body body: MercadoPagoCardTokenBody): Call<JsonObject> //Este seria un objeto con datos
}