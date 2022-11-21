package com.alex.deliveryapp.routes

import com.alex.deliveryapp.models.MercadoPagoCardTokenBody
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MercadoPagoRoutes {

    @POST("v1/card_tokens?public_key=TEST-dab6d675-c0a6-4439-ad0f-686dba382f2a")
    fun createCardToken(@Body body: MercadoPagoCardTokenBody): Call<JsonObject>
}