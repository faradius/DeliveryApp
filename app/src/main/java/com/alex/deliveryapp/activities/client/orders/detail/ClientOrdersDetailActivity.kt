package com.alex.deliveryapp.activities.client.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.orders.map.ClientOrdersMapActivity
import com.alex.deliveryapp.activities.delivery.orders.map.DeliveryOrdersMapActivity
import com.alex.deliveryapp.adapters.OrderProductsAdapter
import com.alex.deliveryapp.models.Order
import com.alex.deliveryapp.utils.Constants
import com.google.gson.Gson

class ClientOrdersDetailActivity : AppCompatActivity() {

    val TAG = "ClientOrderDetail"
    var order: Order? = null
    val gson = Gson()

    var toolbar: Toolbar? = null

    var tvClientOrderDetail:TextView? = null
    var tvAddressOrderDetail:TextView? = null
    var tvDateOrderDetail:TextView? = null
    var tvTotalOrderDetail:TextView? = null
    var tvStatusOrderDetail:TextView? = null
    var rvProductsOrderDetail:RecyclerView? = null
    var btnGoToMap: Button? = null

    var adapter: OrderProductsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_orders_detail)

        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)

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
        btnGoToMap = findViewById(R.id.btn_go_to_map_client)

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

        if(order?.status == "EN CAMINO"){
            btnGoToMap?.visibility = View.VISIBLE
        }

        btnGoToMap?.setOnClickListener { goToMap() }
    }

    private fun goToMap(){
        val i = Intent(this, ClientOrdersMapActivity::class.java)
        i.putExtra(Constants.ORDER, order?.toJson())
        startActivity(i)
    }

    private fun getTotal(){
        var total = 0.0

        for (p in order?.products!!){
            total += (p.price * p.quantity!!)
        }

        tvTotalOrderDetail?.text = "$$total"
    }
}