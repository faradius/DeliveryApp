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
import com.alex.deliveryapp.activities.client.home.ClientHomeActivity
import com.alex.deliveryapp.activities.client.products.detail.ClientProductsDetailActivity
import com.alex.deliveryapp.activities.client.shopping_car.ClientShoppingCarActivity
import com.alex.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.alex.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.models.Rol
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson

class OrderProductsAdapter(val context: Activity, val products: ArrayList<Product>):RecyclerView.Adapter<OrderProductsAdapter.OrderProductsViewHolder>() {

    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_order_products, parent, false)
        return  OrderProductsViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: OrderProductsViewHolder, position: Int) {

        val product = products[position] //Obtenemos cada una de las categorias

        holder.tvNameProduct.text = product.name
        if (product.quantity != null){
            holder.tvQuantityProduct.text = "${product.quantity}"
        }
        Glide.with(context).load(product.image1).into(holder.ivProduct)
        holder.tvPriceProduct.text = "$${product.price * product.quantity!!}"

    }

    //Metodo que define el tamaño de elementos que tiene la vista
    override fun getItemCount(): Int {
        return products.size
    }

    //Aqui se definen las instancias de la vista
    class OrderProductsViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivProduct: ImageView
        val tvNameProduct: TextView
        val tvQuantityProduct: TextView
        val tvPriceProduct: TextView

        init {
            ivProduct = view.findViewById(R.id.iv_product_list_order)
            tvNameProduct = view.findViewById(R.id.tv_name_product_list_order)
            tvQuantityProduct = view.findViewById(R.id.tv_quantity_product_list_order)
            tvPriceProduct = view.findViewById(R.id.tv_price_product_list_order)

        }

    }
}