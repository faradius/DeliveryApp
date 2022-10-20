package com.alex.deliveryapp.activities.client.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alex.deliveryapp.R
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientProductsDetailActivity : AppCompatActivity() {
    private val TAG = "ProductDetailActivity"

    var product: Product? = null
    val gson: Gson = Gson()

    var imageSliderProduct: ImageSlider? = null
    var tvNameProduct: TextView? = null
    var tvDescriptionProduct: TextView? = null
    var tvPriceProduct: TextView? = null
    var tvCounterProduct: TextView? = null
    var ivAddProduct: ImageView? = null
    var ivRemoveProduct: ImageView? = null
    var btnAddProduct: Button? = null

    var counter = 1
    var productPrice = 0.0

    var sharedPref: SharedPref? = null
    var selectedProducts = ArrayList<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)

        //La información que se reciba que se convierta en un objeto de tipo Product
        product = gson.fromJson(intent.getStringExtra(Constants.PRODUCT), Product::class.java)

        sharedPref = SharedPref(this)

        imageSliderProduct = findViewById(R.id.iv_slider_product_detatil)
        tvNameProduct = findViewById(R.id.tv_name_product_detail)
        tvDescriptionProduct = findViewById(R.id.tv_description_product_detail)
        tvPriceProduct = findViewById(R.id.tv_price_product_detail)
        tvCounterProduct = findViewById(R.id.tv_amount_product_detail)
        ivAddProduct = findViewById(R.id.iv_add_product_detail)
        ivRemoveProduct = findViewById(R.id.iv_remove_product_detail)
        btnAddProduct = findViewById(R.id.btn_add_product_detail)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))

        imageSliderProduct?.setImageList(imageList)
        tvNameProduct?.text = product?.name
        tvDescriptionProduct?.text = product?.description
        tvPriceProduct?.text = "$${product?.price}"

        ivAddProduct?.setOnClickListener { addItem() }
        ivRemoveProduct?.setOnClickListener { removeItem() }
        btnAddProduct?.setOnClickListener { addToShoppingCar() }

        getProductsFromSharedPref()

    }

    private fun addToShoppingCar(){
        val index = getIndexOf(product?.id!!) //El indice del producto si es que existe en sharedpref

        if (index == -1){ //Este producto no existe aun en shared pref
            if (product?.quantity == null){ //corregi este error estaba como cero
                //La cantidad seleccionada de ese producto como minimo es uno
                product?.quantity = 1
            }
            selectedProducts.add(product!!)
        }else{ //Ya existe el producto en shared pref debemos editar la cantidad
            selectedProducts[index].quantity = counter
        }

        sharedPref?.save(Constants.ORDER, selectedProducts)
        //btnAddProduct?.setText("Editar Producto")
        //btnAddProduct?.backgroundTintList = ColorStateList.valueOf(Color.RED)
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
    }

    private fun getProductsFromSharedPref(){
        //Si existe una orden en sharedPreferences
        if (!sharedPref?.getData(Constants.ORDER).isNullOrBlank()){
            //Aqui indicamos que vamos a transformar una lista de tipo Gson a una lista de tipo product
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            //Aqui le indicamos que los datos que se obtienen del sharePref se convierte a un tipo de objeto product
            selectedProducts = gson.fromJson(sharedPref?.getData(Constants.ORDER), type)

            val index = getIndexOf(product?.id!!)

            //Si el producto existe en sahredPref
            if(index != -1){
                product?.quantity = selectedProducts[index].quantity

                //Buscar la manera de arreglar esto ya que no funciona bien el incremento de productos con estas dos lineas
                counter = product?.quantity!!
                tvCounterProduct?.text = "$counter"

                //tvCounterProduct?.text = "${product?.quantity}"

                productPrice = product?.price!! * product?.quantity!!
                tvPriceProduct?.text = "$$productPrice"
                btnAddProduct?.setText("Editar Producto")
                btnAddProduct?.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }



            for (p in selectedProducts){
                Log.d(TAG, "Shared Pref: $p")
            }
        }
    }

    //Es para comparar si un producto ya existe en sharedpref y asi poder editar la cantidad del producto seleccionado
    private fun getIndexOf(idProduct:String):Int{
        var position = 0

        for (p in selectedProducts){
            if (p.id == idProduct){
                return position
            }

            position++
        }
        return -1
    }

    private fun addItem(){
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        tvCounterProduct?.text = "${product?.quantity}"
        tvPriceProduct?.text = "$${productPrice}"
    }

    private fun removeItem(){
        if (counter>1){
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            tvCounterProduct?.text = "${product?.quantity}"
            tvPriceProduct?.text = "$${productPrice}"
        }

    }
}