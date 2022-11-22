package com.alex.deliveryapp.api

import com.alex.deliveryapp.routes.*
import retrofit2.create

class ApiRoutes {

    val BASE_URL = "http://192.168.0.249:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes{
        return retrofit.getClient(BASE_URL).create(UsersRoutes::class.java)
    }

    fun getUsersRoutesWithToken(token:String): UsersRoutes{
        return retrofit.getClientWithToken(BASE_URL, token).create(UsersRoutes::class.java)
    }

    fun getCategoriesRoutes(token:String): CategoriesRoutes{
        return retrofit.getClientWithToken(BASE_URL, token).create(CategoriesRoutes::class.java)
    }

    fun getProductsRoutes(token:String): ProductsRoutes {
        return retrofit.getClientWithToken(BASE_URL, token).create(ProductsRoutes::class.java)
    }

    fun getAddressRoutes(token:String): AddressRoutes{
        return retrofit.getClientWithToken(BASE_URL, token).create(AddressRoutes::class.java)
    }

    fun getOrdersRoutes(token:String): OrdersRoutes{
        return retrofit.getClientWithToken(BASE_URL, token).create(OrdersRoutes::class.java)
    }

    fun getPaymentsRoutes(token:String): PaymentsRoutes{
        return retrofit.getClientWithToken(BASE_URL, token).create(PaymentsRoutes::class.java)
    }
}