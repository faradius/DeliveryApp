package com.alex.deliveryapp.providers

import com.alex.deliveryapp.api.ApiRoutes
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.routes.CategoriesRoutes
import com.alex.deliveryapp.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider(val token: String) {

    private var productsRoutes: ProductsRoutes? = null

    init {
        val api = ApiRoutes()
        productsRoutes = api.getProductsRoutes(token)
    }

//    fun getAll():Call<ArrayList<Category>>?{
//        return categoriesRoutes?.getAll(token)
//    }

    fun create(files: List<File>, product: Product): Call<ResponseHttp>?{

        val images = arrayOfNulls<MultipartBody.Part>(files.size)

        for (i in 0 until files.size){
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i]) //Este es el archivo que vamos a enviar
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile) // Este es el nombre del archivo
        }


        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())

        return productsRoutes?.create(images, requestBody,token)
    }

}