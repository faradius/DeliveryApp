package com.alex.deliveryapp.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.address.create.ClientAddressCreateActivity
import com.alex.deliveryapp.adapters.AddressAdapter
import com.alex.deliveryapp.models.Address
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.AddressProvider
import com.alex.deliveryapp.utils.SharedPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {

    var fabCreateAddress: FloatingActionButton? = null
    var toolbar:Toolbar? = null

    var rvAddress:RecyclerView? = null
    var adapter:AddressAdapter? = null
    var addressProvider:AddressProvider? = null
    var sharedPref:SharedPref? = null
    var user: User? = null

    var address = ArrayList<Address>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        sharedPref = SharedPref(this)

        fabCreateAddress = findViewById(R.id.fab_address_create)
        rvAddress = findViewById(R.id.rv_address)
        rvAddress?.layoutManager = LinearLayoutManager(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Mis direcciones"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUserFromSession()

        addressProvider = AddressProvider(user?.sessionToken!!)

        fabCreateAddress?.setOnClickListener{ goToAddressCreate() }

        getAddress()
    }

    private fun getAddress(){
        addressProvider?.getAddress(user?.id!!)?.enqueue(object : Callback<ArrayList<Address>>{
            override fun onResponse(
                call: Call<ArrayList<Address>>,
                response: Response<ArrayList<Address>>
            ) {
                if (response.body() != null){
                    address = response.body()!!
                    adapter = AddressAdapter( this@ClientAddressListActivity, address)
                    rvAddress?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun goToAddressCreate() {
        val i = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}