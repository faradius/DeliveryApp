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
import com.alex.deliveryapp.activities.client.products.list.ClientProductsListActivity
import com.alex.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.alex.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Rol
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide

class CategoriesAdapter(val context: Activity, val categories: ArrayList<Category>):RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    //val sharedPref = SharedPref(context)

    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
        return  CategoriesViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position] //Obtenemos cada una de las categorias

        holder.tvNameCategory.text = category.name
        Glide.with(context).load(category.image).into(holder.ivCategory)

        holder.itemView.setOnClickListener{ goToProducts(category) }
    }

    //Metodo que define el tamaño de elementos que tiene la vista
    override fun getItemCount(): Int {
        return categories.size
    }

    //Aqui se definen las instancias de la vista
    class CategoriesViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvNameCategory: TextView
        val ivCategory: ImageView

        init {
            tvNameCategory = view.findViewById(R.id.tv_item_category_name)
            ivCategory = view.findViewById(R.id.iv_item_category)
        }

    }

   private fun goToProducts(category: Category){
        val i = Intent(context, ClientProductsListActivity::class.java)
        i.putExtra(Constants.ID_CATEGORY, category.id)
        i.putExtra(Constants.NAME_CATEGORY, category.name)
        context.startActivity(i)
    }


}