package com.alex.deliveryapp.providers

import com.alex.deliveryapp.api.MercadoPagoApiRoutes
import com.alex.deliveryapp.models.MercadoPagoCardTokenBody
import com.alex.deliveryapp.routes.MercadoPagoRoutes
import com.google.gson.JsonObject
import retrofit2.Call

class MercadoPagoProvider {

    var mercadoPagoRoutes: MercadoPagoRoutes? = null

    init {
        val api = MercadoPagoApiRoutes()
        mercadoPagoRoutes = api.getMercadoPagoRoutes()
    }

    fun createCardToken(mercadoPagoCardTokenBody: MercadoPagoCardTokenBody): Call<JsonObject>? {
        return mercadoPagoRoutes?.createCardToken(mercadoPagoCardTokenBody)
    }
}