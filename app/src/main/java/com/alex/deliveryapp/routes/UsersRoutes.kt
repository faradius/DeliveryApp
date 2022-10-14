package com.alex.deliveryapp.routes

import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UsersRoutes {

    //El Body es la información que queremos enviar para registrar en la base de datos mientras el query es utilizado para hacer consultas
    @POST("users/create")
    //Se define la respuesta como ResponseHttp por que al insertar nos manda un modelo de datos diferente cuando se registra un usuario a la base de datos
    fun register(@Body user:User): Call<ResponseHttp>

    @FormUrlEncoded
    @POST("users/login")
    fun login(@Field("email") email:String, @Field("password") password:String):Call<ResponseHttp>

    @Multipart //Esto se pone por que se esta subiendo una imagen
    @PUT("users/update") //Se utiliza put por que es una actualización de datos
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("users/updateWithoutImage")
    fun updateWithoutImage(@Body user:User, @Header("Authorization") token: String): Call<ResponseHttp>
}