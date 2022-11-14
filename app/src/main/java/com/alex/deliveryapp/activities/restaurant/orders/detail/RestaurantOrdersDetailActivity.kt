package com.alex.deliveryapp.activities.restaurant.orders.detail

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

class RestaurantOrdersDetailActivity : AppCompatActivity() {

    val TAG = "RestaurantOrderDetail"
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

    var tvDeliveryAvailable: TextView? = null

    var adapter: OrderProductsAdapter? = null

    var usersProvider:UsersProvider? = null
    var ordersProvider: OrdersProvider? = null
    var user:User? = null
    var sharedPref:SharedPref? = null

    var spinnerDeliveryMen: Spinner? = null
    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_orders_detail)

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
        tvDeliveryAvailable = findViewById(R.id.tv_delivery_available)

        spinnerDeliveryMen = findViewById(R.id.spinner_delivery_men)

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
        getDeliveryMen()

        if (order?.status == "PAGADO"){
            btnUpdateDeliveryOrderDetail?.visibility = View.VISIBLE
            tvDeliveryAvailable?.visibility = View.VISIBLE
            spinnerDeliveryMen?.visibility = View.VISIBLE
        }

        btnUpdateDeliveryOrderDetail?.setOnClickListener { updateOrder() }
    }

    private fun updateOrder(){
        order?.idDelivery = idDelivery
        ordersProvider?.updateToDispatched(order!!)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body() != null){
                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@RestaurantOrdersDetailActivity, "Repartidor asignado correctamente", Toast.LENGTH_SHORT).show()
                        goToOrders()
                    }else{
                        Toast.makeText(this@RestaurantOrdersDetailActivity, "No se pudo asignar el repartidor", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@RestaurantOrdersDetailActivity, "No hubo respuesta del servidor", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@RestaurantOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goToOrders(){
        val i = Intent(this, RestaurantHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    private fun getDeliveryMen(){
        usersProvider?.getDeliveryMen()?.enqueue(object: Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.body() != null){
                    val deliveryMen = response.body() //Trae la lista de repartidores
                    val arrayAdapter = ArrayAdapter<User>(this@RestaurantOrdersDetailActivity, android.R.layout.simple_dropdown_item_1line, deliveryMen!!)
                    spinnerDeliveryMen?.adapter = arrayAdapter
                    spinnerDeliveryMen?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                            idDelivery = deliveryMen[position].id!! //Seleccionando del spinner el id del delivery, es decir que repartidor va entregar cierta orden
                            Log.d(TAG, "id Delivery: $idDelivery")
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(this@RestaurantOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
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