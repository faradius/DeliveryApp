package com.alex.deliveryapp.providers

import com.alex.deliveryapp.api.ApiRoutes
import com.alex.deliveryapp.models.Address
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.routes.AddressRoutes
import com.alex.deliveryapp.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class AddressProvider(val token: String) {

    private var addressRoutes: AddressRoutes? = null

    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
    }

    fun getAddress(idUser: String):Call<ArrayList<Address>>?{
        return addressRoutes?.getAddress(idUser,token)
    }

    fun create(address: Address): Call<ResponseHttp>?{
        return addressRoutes?.create(address, token)
    }

}