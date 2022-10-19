package com.alex.deliveryapp.activities.client.products.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.utils.Constants
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson

class ClientProductsDetailActivity : AppCompatActivity() {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)

        //La información que se reciba que se convierta en un objeto de tipo Product
        product = gson.fromJson(intent.getStringExtra(Constants.PRODUCT), Product::class.java)

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