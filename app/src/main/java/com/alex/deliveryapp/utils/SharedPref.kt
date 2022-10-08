package com.alex.deliveryapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

//Va recibir como parametros un activity que hace mención de donde va a funcionar el sharedPreferences
class SharedPref(activity: Activity) {
    private var prefs: SharedPreferences? = null

    init {
        prefs = activity.getSharedPreferences("com.alex.deliveryapp", Context.MODE_PRIVATE)
    }

    //Es un metodo para guardar la sesión del usuario en sharedPreferences
    fun save(key: String, objeto: Any){
        try {
            val gson = Gson()
            //se convierte el objeto de tipo Json
            val json = gson.toJson(objeto)
            with(prefs?.edit()){
                this?.putString(key, json)
                this?.commit()
            }

        }catch (e: Exception){
            Log.d("ERROR", "Err: ${e.message} ")
        }
    }

    fun getData(key:String): String?{
        val data = prefs?.getString(key, "")
        return data
    }

    fun remove(key:String){
        prefs?.edit()?.remove(key)?.apply()
    }

}