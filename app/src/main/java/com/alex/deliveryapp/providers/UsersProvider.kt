package com.alex.deliveryapp.providers

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.alex.deliveryapp.api.ApiRoutes
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.routes.UsersRoutes
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UsersProvider(val token:String? = null) {
    private val TAG = "UsersProvider"

    private var userRoutes: UsersRoutes? = null
    private var userRoutesToken: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        userRoutes = api.getUsersRoutes()

        //Si el token es diferente de nulo se inicializa este metodo
        if(token != null){
            userRoutesToken = api.getUsersRoutesWithToken(token!!)
        }
    }

    fun createToken(user: User, context: Activity){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result

            val sharedPref = SharedPref(context)

            user.notificationToken = token

            sharedPref.save(Constants.USER, user)

            updateNotificationToken(user)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    if (response.body() == null){
                        Log.d(TAG, "Hubo un error al crear el token")
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

            // Get new FCM registration token

            Log.d(TAG, "TOKEN DE NOTIFICACIONES: $token")
        })

    }

    fun getDeliveryMen():Call<ArrayList<User>>?{
        return userRoutesToken?.getDeliveryMen(token!!)
    }

    fun register(user:User): Call<ResponseHttp>?{
        return userRoutes?.register(user)
    }

    fun login(email:String, password:String): Call<ResponseHttp>?{
        return userRoutes?.login(email,password)
    }

    fun updateWithoutImage(user:User): Call<ResponseHttp>?{
        return userRoutesToken?.updateWithoutImage(user, token!!)
    }

    fun updateNotificationToken(user:User): Call<ResponseHttp>?{
        return userRoutesToken?.updateNotificationToken(user, token!!)
    }

    fun update(file: File, user: User): Call<ResponseHttp>?{
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file) //Este es el archivo que vamos a enviar
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile) // Este es el nombre del archivo
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())

        return userRoutesToken?.update(image, requestBody,token!!)
    }
}