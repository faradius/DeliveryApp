package com.alex.deliveryapp.activities.delivery.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.alex.deliveryapp.adapters.OrderProductsAdapter
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Order
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.OrdersProvider
import com.alex.deliveryapp.providers.UsersProvider
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryOrdersDetailActivity : AppCompatActivity() {

    val TAG = "DeliveryOrderDetail"
    var order: Order? = null
    val gson = Gson()

    var toolbar: Toolbar? = null

    var tvClientOrderDetail:TextView? = null
    var tvAddressOrderDetail:TextView? = null
    var tvDateOrderDetail:TextView? = null
    var tvTotalOrderDetail:TextView? = null
    var tvStatusOrderDetail:TextView? = null


    var rvProductsOrderDetail:RecyclerView? = null
    var btnUpdateDeliveryOrderDetail: Button? = null


    var adapter: OrderProductsAdapter? = null

    var usersProvider:UsersProvider? = null
    var ordersProvider: OrdersProvider? = null
    var user:User? = null
    var sharedPref:SharedPref? = null


    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_detail)

        sharedPref = SharedPref(this)

        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)

        getUserFromSession()

        usersProvider = UsersProvider(user?.sessionToken!!)
        ordersProvider = OrdersProvider(user?.sessionToken!!)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Orden #${order?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvClientOrderDetail = findViewById(R.id.tv_client_order_detail)
        tvAddressOrderDetail = findViewById(R.id.tv_address_order_detail)
        tvDateOrderDetail = findViewById(R.id.tv_date_order_detail)
        tvTotalOrderDetail = findViewById(R.id.tv_total_order_detail)
        tvStatusOrderDetail = findViewById(R.id.tv_status_order_detail)


        btnUpdateDeliveryOrderDetail = findViewById(R.id.btn_update_delivery_order_detail)

        rvProductsOrderDetail = findViewById(R.id.rv_products_order_detail)
        rvProductsOrderDetail?.layoutManager = LinearLayoutManager(this)

        adapter = OrderProductsAdapter(this, order?.products!!)
        rvProductsOrderDetail?.adapter = adapter

        tvClientOrderDetail?.text = "${order?.client?.name} ${order?.client?.lastName}"
        tvAddressOrderDetail?.text = order?.address?.address
        tvDateOrderDetail?.text = "${order?.timestamp.toString()}"
        tvStatusOrderDetail?.text = order?.status


        Log.d(TAG,"Orden: ${order.toString()}")

        getTotal()


        if (order?.status == "DESPACHADO"){
            btnUpdateDeliveryOrderDetail?.visibility = View.VISIBLE
        }


        btnUpdateDeliveryOrderDetail?.setOnClickListener { updateOrder() }
    }

    private fun updateOrder(){
        order?.idDelivery = idDelivery
        ordersProvider?.updateToDispatched(order!!)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body() != null){
                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "Repartidor asignado correctamente", Toast.LENGTH_SHORT).show()
                        goToOrders()
                    }else{
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "No se pudo asignar el repartidor", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@DeliveryOrdersDetailActivity, "No hubo respuesta del servidor", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DeliveryOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goToOrders(){
        val i = Intent(this, RestaurantHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun getTotal(){
        var total = 0.0

        for (p in order?.products!!){
            total += (p.price * p.quantity!!)
        }

        tvTotalOrderDetail?.text = "$$total"
    }
}