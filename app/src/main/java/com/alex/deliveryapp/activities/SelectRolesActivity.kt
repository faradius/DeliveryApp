package com.alex.deliveryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.adapters.RolesAdapter
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson

class SelectRolesActivity : AppCompatActivity() {

    var rvRoles: RecyclerView? = null
    var user:User? = null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        rvRoles = findViewById(R.id.rv_roles)
        //Con esto definimos que se mostrará la vista de forma vertical
        rvRoles?.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapter(this,user?.roles!!)
        rvRoles?.adapter = adapter
    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}