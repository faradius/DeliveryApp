package com.alex.deliveryapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.models.Rol
import com.bumptech.glide.Glide

class RolesAdapter(val context: Activity, val roles: ArrayList<Rol>):RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

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


}