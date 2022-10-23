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
import com.alex.deliveryapp.models.Address
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Rol
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide

class AddressAdapter(val context: Activity, val addressList: ArrayList<Address>):RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    //val sharedPref = SharedPref(context)

    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_address, parent, false)
        return  AddressViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressList[position] //Obtenemos cada una de las direcciones

        holder.tvAddress.text = address.address
        holder.tvneighborhood.text = address.neighborhood

    }

    //Metodo que define el tamaño de elementos que tiene la vista
    override fun getItemCount(): Int {
        return addressList.size
    }

    //Aqui se definen las instancias de la vista
    class AddressViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvAddress: TextView
        val tvneighborhood: TextView
        val ivCheck: ImageView

        init {
            tvAddress = view.findViewById(R.id.tv_address_list)
            tvneighborhood = view.findViewById(R.id.tv_neighborhood_list)
            ivCheck = view.findViewById(R.id.iv_check)
        }

    }
}