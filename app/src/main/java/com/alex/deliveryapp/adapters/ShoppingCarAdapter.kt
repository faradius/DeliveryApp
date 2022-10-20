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
import com.alex.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.alex.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.models.Rol
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson

class ShoppingCarAdapter(val context: Activity, val products: ArrayList<Product>):RecyclerView.Adapter<ShoppingCarAdapter.ShoppingCarViewHolder>() {

    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_car, parent, false)
        return  ShoppingCarViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: ShoppingCarViewHolder, position: Int) {
        val product = products[position] //Obtenemos cada una de las categorias

        holder.tvNameProduct.text = product.name
        holder.tvPriceProduct.text = "$${product.price}"
        Glide.with(context).load(product.image1).into(holder.ivProduct)

        //holder.itemView.setOnClickListener { goToProductDetail(product) }
    }

    private fun goToProductDetail(product: Product) {
        val i = Intent(context, ClientProductsDetailActivity::class.java)
        i.putExtra(Constants.PRODUCT, product.toJson())
        context.startActivity(i)
    }

    //Metodo que define el tamaño de elementos que tiene la vista
    override fun getItemCount(): Int {
        return products.size
    }

    //Aqui se definen las instancias de la vista
    class ShoppingCarViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvNameProduct: TextView
        val tvPriceProduct: TextView
        val tvCounterProduct: TextView
        val ivProduct: ImageView
        val ivAdd: ImageView
        val ivRemove: ImageView
        val ivDelete: ImageView

        init {
            tvNameProduct = view.findViewById(R.id.tv_name_product_list_car)
            tvPriceProduct = view.findViewById(R.id.tv_price_product_car)
            tvCounterProduct = view.findViewById(R.id.tv_amount_product_car)
            ivProduct = view.findViewById(R.id.iv_product_car)
            ivAdd = view.findViewById(R.id.iv_add_product_car)
            ivRemove = view.findViewById(R.id.iv_remove_product_car)
            ivDelete = view.findViewById(R.id.iv_delete_product_car)
        }

    }
}