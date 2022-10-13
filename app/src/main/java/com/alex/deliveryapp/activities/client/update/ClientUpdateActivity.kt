package com.alex.deliveryapp.activities.client.update

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.alex.deliveryapp.R
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.UsersProvider
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {

    private val TAG = "ClientUpdateActivity"

    var circleImageUserUpdate: CircleImageView?= null
    var etNameUserUpdate: EditText? = null
    var etLastNameUserUpdate: EditText? = null
    var etPhoneUserUpdate: EditText? = null
    var btnUserUpdate: Button? = null

    var sharedPref: SharedPref? = null
    var user: User? = null

    private var imageFile: File? = null
    private var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)

        sharedPref = SharedPref(this)

        circleImageUserUpdate = findViewById(R.id.iv_circle_user_profile_update)
        etNameUserUpdate = findViewById(R.id.et_name_user_update)
        etLastNameUserUpdate = findViewById(R.id.et_last_name_user_update)
        etPhoneUserUpdate = findViewById(R.id.et_phone_user_update)
        btnUserUpdate = findViewById(R.id.btn_user_update)

        getUserFromSession()

        etNameUserUpdate?.setText(user?.name)
        etLastNameUserUpdate?.setText(user?.lastName)
        etPhoneUserUpdate?.setText(user?.phone)

        if (!user?.image.isNullOrBlank()){
            Glide.with(this).load(user?.image).into(circleImageUserUpdate!!)
        }

        circleImageUserUpdate?.setOnClickListener { selectImage() }
        btnUserUpdate?.setOnClickListener { updateData() }
    }

    //Guarda los nuevos datos de la sesión del usuario
    private fun saveUserInSession(data: String){
        val gson = Gson()
        //En esta variable tenemos toda la información del usuario de tipo Json
        val user = gson.fromJson(data,User::class.java)
        sharedPref?.save("user", user)
    }

    private fun updateData(){

        val name = etNameUserUpdate?.text.toString()
        val lastName = etLastNameUserUpdate?.text.toString()
        val phone = etPhoneUserUpdate?.text.toString()

        user?.name = name
        user?.lastName = lastName
        user?.phone = phone

        if (imageFile != null){
            usersProvider.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())
                    Toast.makeText(this@ClientUpdateActivity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }else{
            usersProvider.updateWithoutImage(user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())
                    Toast.makeText(this@ClientUpdateActivity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }


    }

    //Esta variable captura lo que el usuario seleccionó
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
            val resultCode = result.resultCode
            //aqui capturamos los datos que nos devuelve el creatIntent del ImagePicker
            val data = result.data

            //Si el usuario seleccionó una imagen correctamente
            if (resultCode == Activity.RESULT_OK){
                //Transformamos esa imagen en un archivo que almacenamos en el imageFile
                //Vamos a crear un archivo
                val fileUri = data?.data
                //El archivo que vamos a guardar como imagen en el servidor, fileUri.path viene siendo la ruta de la imagen en forma de URL
                imageFile = File(fileUri?.path)
                //aqui lo que hacemos es colocarle al ivCircleUser la imagen seleccionada en forma de url
                circleImageUserUpdate?.setImageURI(fileUri)

            }else if(resultCode == ImagePicker.RESULT_ERROR){

                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()

            }else{

                Toast.makeText(this, "La tarea se cancelo", Toast.LENGTH_LONG).show()
            }
        }

    //Esta funcion hace que el ImagePicker permita seleccionar una imagen con la ayuda de la variable startImageForResult
    private fun selectImage() {
        ImagePicker.with(this)
            .crop() //Permitir recortar la imagen al gusto del usuario
            .compress(1024) //comprimir la imagen de forma adecuada al servidor
            .maxResultSize(1080, 1080) //tamaño maximo permitido para imagenes en pixeles
            .createIntent { intent: Intent ->
                startImageForResult.launch(intent)
            }
    }

    //Obtiene la sesión del usuario
    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}