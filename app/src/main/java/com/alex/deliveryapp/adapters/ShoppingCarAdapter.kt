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

class ShoppingCarAdapter(val context: Activity, val products: ArrayList<Product>):RecyclerView.Adapter<ShoppingCarAdapter.ShoppingCarViewHolder>() {

    val sharedPref = SharedPref(context)

    init {
        (context as ClientShoppingCarActivity).setTotal(getTotal())
    }

    //Metodo para inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_car, parent, false)
        return  ShoppingCarViewHolder(view)
    }

    //Metodo para especificar los valores que tendrá cada elemento de la vista
    override fun onBindViewHolder(holder: ShoppingCarViewHolder, position: Int) {
        val product = products[position] //Obtenemos cada una de las categorias

        holder.tvNameProduct.text = product.name
        holder.tvCounterProduct.text = "${product.quantity}"
        holder.tvPriceProduct.text = "$${product.price * product.quantity!!}"
        Glide.with(context).load(product.image1).into(holder.ivProduct)

        holder.ivAdd.setOnClickListener { addItem(product, holder) }
        holder.ivRemove.setOnClickListener { removeItem(product, holder) }
        holder.ivDelete.setOnClickListener { deleteItem(position) }

        //holder.itemView.setOnClickListener { goToProductDetail(product) }
    }

    private fun getTotal():Double{
        var total = 0.0
        for (p in products){
            total += p.quantity!! * p.price
        }

        return total
    }

    private fun getIndexOf(idProduct:String):Int{
        var position = 0

        for (p in products){
            if (p.id == idProduct){
                return position
            }

            position++
        }
        return -1
    }

    private fun deleteItem(position: Int){
        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, products.size)

        sharedPref.save(Constants.ORDER, products)
        (context as ClientShoppingCarActivity).setTotal(getTotal())
    }

    private fun addItem(product: Product, holder: ShoppingCarViewHolder){

        val index = getIndexOf(product.id!!)
        product.quantity = product.quantity!! + 1
        products[index].quantity = product.quantity

        holder.tvCounterProduct.text = "${product.quantity}"
        holder.tvPriceProduct.text = "$${product.quantity!! * product.price}"

        sharedPref.save(Constants.ORDER, products)
        (context as ClientShoppingCarActivity).setTotal(getTotal())
    }

    private fun removeItem(product: Product, holder: ShoppingCarViewHolder){
        if (product.quantity!! > 1){
            val index = getIndexOf(product.id!!)
            product.quantity = product.quantity!! - 1
            products[index].quantity = product.quantity

            holder.tvCounterProduct.text = "${product.quantity}"
            holder.tvPriceProduct.text = "$${product.quantity!! * product.price}"

            sharedPref.save(Constants.ORDER, products)
            (context as ClientShoppingCarActivity).setTotal(getTotal())
        }



    }

//    private fun goToProductDetail(product: Product) {
//        val i = Intent(context, ClientProductsDetailActivity::class.java)
//        i.putExtra(Constants.PRODUCT, product.toJson())
//        context.startActivity(i)
//    }

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