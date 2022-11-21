package com.alex.deliveryapp.api

import com.alex.deliveryapp.routes.*
import retrofit2.create

class MercadoPagoApiRoutes {

    val BASE_URL = "https://api.mercadopago.com"
    val retrofit = RetrofitClient()

    fun getMercadoPagoRoutes(): MercadoPagoRoutes{
        return retrofit.getClient(BASE_URL).create(MercadoPagoRoutes::class.java)
    }
}