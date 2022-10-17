package com.alex.deliveryapp.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.adapters.ProductsAdapter
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.ProductsProvider
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {

    val TAG = "ProductsListActivity"

    var rvProducts: RecyclerView? = null
    var adapter:ProductsAdapter? = null
    var user: User? = null
    var productsProvider: ProductsProvider? = null
    var products:ArrayList<Product> = ArrayList()

    var idCategory:String? = null
    var nameCategory:String? = null
    var sharedPref: SharedPref? = null

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        sharedPref = SharedPref(this)

        idCategory = intent.getStringExtra(Constants.ID_CATEGORY)
        nameCategory = intent.getStringExtra(Constants.NAME_CATEGORY)

        toolbar?.title = nameCategory
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUserFromSession()

        productsProvider = ProductsProvider(user?.sessionToken!!)

        rvProducts = findViewById(R.id.rv_products)
        rvProducts?.layoutManager = GridLayoutManager(this,2)

        getProducts()
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun getProducts(){
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object: Callback<ArrayList<Product>>{
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if (response.body() != null){
                    products = response.body()!!
                    adapter = ProductsAdapter(this@ClientProductsListActivity, products)
                    rvProducts?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error: ${t.message}")
            }

        })
    }
}