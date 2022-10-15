package com.alex.deliveryapp.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.alex.deliveryapp.R
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File


class RestaurantProductFragment : Fragment() {

    var myView: View? = null
    var etNameProduct: EditText? = null
    var etDescriptionProduct: EditText? = null
    var etPriceProduct: EditText? = null
    var ivImageProduct1: ImageView? = null
    var ivImageProduct2: ImageView? = null
    var ivImageProduct3: ImageView? = null
    var btnCreateProduct: Button? = null

    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_product, container, false)

        etNameProduct = myView?.findViewById(R.id.et_product_name)
        etDescriptionProduct = myView?.findViewById(R.id.et_product_description)
        etPriceProduct = myView?.findViewById(R.id.et_product_price)
        ivImageProduct1 = myView?.findViewById(R.id.iv_image1)
        ivImageProduct2 = myView?.findViewById(R.id.iv_image2)
        ivImageProduct3 = myView?.findViewById(R.id.iv_image3)
        btnCreateProduct = myView?.findViewById(R.id.btn_create_product)

        btnCreateProduct?.setOnClickListener { createProduct() }
        ivImageProduct1?.setOnClickListener { selectImage(101) }
        ivImageProduct2?.setOnClickListener { selectImage(102) }
        ivImageProduct3?.setOnClickListener { selectImage(103) }

        return myView
    }

    private fun createProduct() {
        val name = etNameProduct?.text.toString()
        val description = etDescriptionProduct?.text.toString()
        val priceText = etPriceProduct?.text.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val fileUri = data?.data

            if (requestCode == 101){
                imageFile1 = File(fileUri?.path)
                ivImageProduct1?.setImageURI(fileUri)
            }
            else if (requestCode == 102){
                imageFile2 = File(fileUri?.path)
                ivImageProduct2?.setImageURI(fileUri)
            }
            else if (requestCode == 103){
                imageFile3 = File(fileUri?.path)
                ivImageProduct3?.setImageURI(fileUri)
            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    //Esta funcion hace que el ImagePicker permita seleccionar una imagen con la ayuda de la variable startImageForResult
    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop() //Permitir recortar la imagen al gusto del usuario
            .compress(1024) //comprimir la imagen de forma adecuada al servidor
            .maxResultSize(1080, 1080) //tamaño maximo permitido para imagenes en pixeles
            .start(requestCode)
    }


}