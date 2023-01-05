package com.alex.deliveryapp.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.address.list.ClientAddressListActivity
import com.alex.deliveryapp.activities.client.home.ClientHomeActivity
import com.alex.deliveryapp.activities.client.orders.detail.ClientOrdersDetailActivity
import com.alex.deliveryapp.activities.client.payments.mercado_pago.form.ClientPaymentFormActivity
import com.alex.deliveryapp.activities.client.products.list.ClientProductsListActivity
import com.alex.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.alex.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.alex.deliveryapp.models.Address
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Order
import com.alex.deliveryapp.models.Rol
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson

class OrdersClientAdapter(val context: Activity, val orders: ArrayList<Order>):RecyclerView.Adapter<OrdersClientAdapter.OrdersViewHolder>() {


    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders, parent, false)
        return  OrdersViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        //Obtenemos cada una de las ordenes
        val order = orders[position]

        holder.tvOrderId.text = "Order #${order.id}"
        holder.tvDate.text = "${order.timestamp}"
        holder.tvAddress.text = "${order.address?.address}"

        holder.itemView.setOnClickListener {goToOrderDetail(order)}

    }

    private fun goToOrderDetail(order:Order){
        val i = Intent(context, ClientOrdersDetailActivity::class.java)
        i.putExtra(Constants.ORDER, order.toJson())
        context.startActivity(i)
    }

    //Metodo que define el tamaño de elementos que tiene la vista
    override fun getItemCount(): Int {
        return orders.size
    }

    //Aqui se definen las instancias de la vista
    class OrdersViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvOrderId: TextView
        val tvDate: TextView
        val tvAddress: TextView

        init {
            tvOrderId = view.findViewById(R.id.tv_order_id)
            tvDate = view.findViewById(R.id.tv_date_order)
            tvAddress = view.findViewById(R.id.tv_address_order)
        }

    }
}