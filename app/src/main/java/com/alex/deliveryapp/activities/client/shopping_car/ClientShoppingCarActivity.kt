package com.alex.deliveryapp.activities.client.shopping_car

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.address.create.ClientAddressCreateActivity
import com.alex.deliveryapp.activities.client.address.list.ClientAddressListActivity
import com.alex.deliveryapp.adapters.ShoppingCarAdapter
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientShoppingCarActivity : AppCompatActivity() {

    var rvShoppingCar: RecyclerView? = null
    var tvTotal: TextView? = null
    var btnNext: Button? = null
    var toolbar: Toolbar? = null

    var adapter: ShoppingCarAdapter? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    private var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shopping_car)

        sharedPref = SharedPref(this)

        rvShoppingCar = findViewById(R.id.rv_shopping_car)
        tvTotal = findViewById(R.id.tv_total_product_list_car)
        btnNext = findViewById(R.id.btn_next_product_list_car)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Tu orden"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvShoppingCar?.layoutManager = LinearLayoutManager(this)

        getProductsFromSharedPref()

        btnNext?.setOnClickListener { goToAddressList() }
    }

    private fun goToAddressList() {
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    fun setTotal(total:Double){
        tvTotal?.text = "$${total}"
    }

    private fun getProductsFromSharedPref(){
        //Si existe una orden en sharedPreferences
        if (!sharedPref?.getData(Constants.ORDER).isNullOrBlank()){
            //Aqui indicamos que vamos a transformar una lista de tipo Gson a una lista de tipo product
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            //Aqui le indicamos que los datos que se obtienen del sharePref se convierte a un tipo de objeto product
            selectedProducts = gson.fromJson(sharedPref?.getData(Constants.ORDER), type)

            adapter = ShoppingCarAdapter(this, selectedProducts)
            rvShoppingCar?.adapter = adapter


        }
    }
}