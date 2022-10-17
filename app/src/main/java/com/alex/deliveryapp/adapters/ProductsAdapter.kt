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
import com.alex.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.alex.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.models.Rol
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide

class ProductsAdapter(val context: Activity, val products: ArrayList<Product>):RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
        return  ProductsViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position] //Obtenemos cada una de las categorias

        holder.tvNameProduct.text = product.name
        holder.tvPriceProduct.text = "$${product.price}"
        Glide.with(context).load(product.image1).into(holder.ivProduct)
    }

    //Metodo que define el tamaño de elementos que tiene la vista
    override fun getItemCount(): Int {
        return products.size
    }

    //Aqui se definen las instancias de la vista
    class ProductsViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvNameProduct: TextView
        val tvPriceProduct: TextView
        val ivProduct: ImageView

        init {
            tvNameProduct = view.findViewById(R.id.tv_name_product_item)
            tvPriceProduct = view.findViewById(R.id.tv_price_product_item)
            ivProduct = view.findViewById(R.id.iv_product_item)
        }

    }
}