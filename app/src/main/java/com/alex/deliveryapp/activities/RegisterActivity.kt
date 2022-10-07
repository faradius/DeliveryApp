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
            Toast.makeText(this, "El formulario es valido", Toast.LENGTH_SHORT).show()
        }

        Log.d(TAG, "El nombre es: $name")
        Log.d(TAG, "El apellido es: $lastName")
        Log.d(TAG, "El email es: $email")
        Log.d(TAG, "El telefono es: $phone")
        Log.d(TAG, "La contraseña es: $password")
        Log.d(TAG, "La confirmación de la contraseña es: $confirmPassword")
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