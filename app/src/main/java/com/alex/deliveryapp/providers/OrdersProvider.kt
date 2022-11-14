package com.alex.deliveryapp.providers

import com.alex.deliveryapp.api.ApiRoutes
import com.alex.deliveryapp.models.*
import com.alex.deliveryapp.routes.AddressRoutes
import com.alex.deliveryapp.routes.CategoriesRoutes
import com.alex.deliveryapp.routes.OrdersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class OrdersProvider(val token: String) {

    private var ordersRoutes: OrdersRoutes? = null

    init {
        val api = ApiRoutes()
        ordersRoutes = api.getOrdersRoutes(token)
    }

    fun getOrdersByStatus(status: String):Call<ArrayList<Order>>?{
        return ordersRoutes?.getOrdersByStatus(status,token)
    }

    fun getOrdersByClientAndStatus(idClient:String, status: String):Call<ArrayList<Order>>?{
        return ordersRoutes?.getOrdersByClientAndStatus(idClient,status,token)
    }

    fun create(order: Order): Call<ResponseHttp>?{
        return ordersRoutes?.create(order, token)
    }

    fun updateToDispatched(order: Order): Call<ResponseHttp>?{
        return ordersRoutes?.updateToDispatched(order, token)
    }

}