package com.alex.deliveryapp.providers

import com.alex.deliveryapp.api.ApiRoutes
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriesProvider(val token: String) {

    private var categoriesRoutes: CategoriesRoutes? = null

    init {
        val api = ApiRoutes()
        categoriesRoutes = api.getCategoriesRoutes(token)
    }

    fun getAll():Call<ArrayList<Category>>?{
        return categoriesRoutes?.getAll(token)
    }

    fun create(file: File, category: Category): Call<ResponseHttp>?{
        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file) //Este es el archivo que vamos a enviar
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile) // Este es el nombre del archivo
        val requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), category.toJson())

        return categoriesRoutes?.create(image, requestBody,token)
    }

}