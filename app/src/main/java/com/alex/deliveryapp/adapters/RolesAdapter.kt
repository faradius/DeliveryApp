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
import com.alex.deliveryapp.models.Rol
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide

class RolesAdapter(val context: Activity, val roles: ArrayList<Rol>):RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharedPref = SharedPref(context)

    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return  RolesViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]

        holder.tvRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.ivRol)

        holder.itemView.setOnClickListener{
            goToRol(rol)
        }
    }

    //Metodo que define el tamaño de elementos que tiene la vista
    override fun getItemCount(): Int {
        return roles.size
    }

    //Aqui se definen las instancias de la vista
    class RolesViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvRol: TextView
        val ivRol: ImageView

        init {
            tvRol = view.findViewById(R.id.tv_rol)
            ivRol = view.findViewById(R.id.iv_rol)
        }

    }

    private fun goToRol(rol: Rol){
        if (rol.name == Constants.ROL_RESTAURANTE){

            sharedPref.save(Constants.ROL, Constants.ROL_RESTAURANTE)

            val i = Intent(context, RestaurantHomeActivity::class.java)
            context.startActivity(i)
        }else if(rol.name == Constants.ROL_CLIENTE){

            sharedPref.save(Constants.ROL, Constants.ROL_CLIENTE)

            val i = Intent(context, ClientHomeActivity::class.java)
            context.startActivity(i)
        }else if(rol.name == Constants.ROL_REPARTIDOR){

            sharedPref.save(Constants.ROL, Constants.ROL_REPARTIDOR)

            val i = Intent(context, DeliveryHomeActivity::class.java)
            context.startActivity(i)
        }
    }


}