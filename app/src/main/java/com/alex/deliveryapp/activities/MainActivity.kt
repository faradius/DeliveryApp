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
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var btnGoToRegister: ImageView? = null
    var etEmailLogin:EditText? = null
    var etPasswordLogin:EditText? = null
    var btnLogin: Button? = null
    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGoToRegister = findViewById(R.id.btn_go_to_register)
        etEmailLogin = findViewById(R.id.et_email_login)
        etPasswordLogin = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login)

        //si esto viene nulo no se va ejecutar este metodo para eso sirve el null safety
        btnGoToRegister?.setOnClickListener {
            goToRegister()
        }

        btnLogin?.setOnClickListener {
            login()
        }
    }
    
    private fun login(){
        val email = etEmailLogin?.text.toString()
        val password = etPasswordLogin?.text.toString()

        if (isValidForm(email,password)){
            usersProvider.login(email, password)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d("MainActivity", "Response: ${response.body()}")

                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@MainActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("MainActivity", "Hubo un error ${t.message}")
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }else{
            Toast.makeText(this, "No es valido el formulario", Toast.LENGTH_SHORT).show()
        }

        //Toast.makeText(this, "El email es $email", Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "El pasword es $password", Toast.LENGTH_SHORT).show()

    }

    //Es un metodo que valida una variable que tiene un formato de email correcto, por eso el String al principio
    private fun String.isEmailValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(email:String, password:String):Boolean{
        if (email.isBlank()){
            return false
        }

        if (password.isBlank()){
            return false
        }

        if (!email.isEmailValid()){
            return false
        }

        return true
    }

    private fun goToRegister() {
        //no usar intent como nombre de variable por que existe una que hereda de AppCompatActivity (tener cuidado)
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}