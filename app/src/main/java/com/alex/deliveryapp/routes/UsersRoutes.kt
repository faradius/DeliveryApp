package com.alex.deliveryapp.routes

import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersRoutes {

    //El Body es la información que queremos enviar para registrar en la base de datos mientras el query es utilizado para hacer consultas
    @POST("users/create")
    //Se define la respuesta como ResponseHttp por que al insertar nos manda un modelo de datos diferente cuando se registra un usuario a la base de datos
    fun register(@Body user:User): Call<ResponseHttp>
}