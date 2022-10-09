package com.alex.deliveryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.home.ClientHomeActivity
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.UsersProvider
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    var btnGoToLogin: ImageView? = null
    var etNameRegister: EditText? = null
    var etLastNameRegister: EditText? = null
    var etEmailRegister: EditText? = null
    var etPhoneRegister: EditText? = null
    var etPasswordRegister: EditText? = null
    var etConfirmPasswordRegister: EditText? = null
    var btnRegister:Button? = null

    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnGoToLogin = findViewById(R.id.btn_go_to_login)
        etNameRegister = findViewById(R.id.et_name_register)
        etLastNameRegister = findViewById(R.id.et_last_name_register)
        etEmailRegister = findViewById(R.id.et_email_register)
        etPhoneRegister = findViewById(R.id.et_phone_register)
        etPasswordRegister = findViewById(R.id.et_password_register)
        etConfirmPasswordRegister = findViewById(R.id.et_confirm_password_register)
        btnRegister = findViewById(R.id.btn_register)

        btnGoToLogin?.setOnClickListener {
            goToLogin()
        }

        btnRegister?.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = etNameRegister?.text.toString()
        val lastName = etLastNameRegister?.text.toString()
        val email = etEmailRegister?.text.toString()
        val phone = etPhoneRegister?.text.toString()
        val password = etPasswordRegister?.text.toString()
        val confirmPassword = etConfirmPasswordRegister?.text.toString()

        if (isValidForm(name, lastName,email,phone,password,confirmPassword)){
            val user = User(
                name = name,
                lastName = lastName,
                email = email,
                phone = phone,
                password = password
            )
            usersProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {

                    if (response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "onResponse: $response")
                    Log.d(TAG, "onBody: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "onFailure: Se produjo un error ${t.message}")
                    //se coloca el this de esta forma por que al estar en el enqueue estamos en otro contexto
                    Toast.makeText(this@RegisterActivity, "onFailure: Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        startActivity(i)
    }

    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        //En esta variable tenemos toda la información del usuario de tipo Json
        val user = gson.fromJson(data,User::class.java)

        sharedPref.save("user", user)

    }

    private fun String.isEmailValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(
        name:String,
        lastName:String,
        email:String,
        phone:String,
        password:String,
        confirmPassword:String
    ):Boolean{

        if (name.isBlank()){
            Toast.makeText(this, "Debes ingresar el nombre", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastName.isBlank()){
            Toast.makeText(this, "Debes ingresar el apellido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isBlank()){
            Toast.makeText(this, "Debes ingresar el email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.isBlank()){
            Toast.makeText(this, "Debes ingresar el telefono", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isBlank()){
            Toast.makeText(this, "Debes ingresar el contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmPassword.isBlank()){
            Toast.makeText(this, "Debes confirmar la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }


        if (!email.isEmailValid()){
            Toast.makeText(this, "El email no es valido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}