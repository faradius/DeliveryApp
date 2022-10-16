package com.alex.deliveryapp.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.alex.deliveryapp.R
import com.alex.deliveryapp.adapters.CategoriesAdapter
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.CategoriesProvider
import com.alex.deliveryapp.providers.ProductsProvider
import com.alex.deliveryapp.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantProductFragment : Fragment() {

    val TAG ="ProductFragment"

    var myView: View? = null
    var etNameProduct: EditText? = null
    var etDescriptionProduct: EditText? = null
    var etPriceProduct: EditText? = null
    var ivImageProduct1: ImageView? = null
    var ivImageProduct2: ImageView? = null
    var ivImageProduct3: ImageView? = null
    var btnCreateProduct: Button? = null
    var spinnerCategories: Spinner? = null

    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null

    var user: User? = null
    var categoriesProvider: CategoriesProvider? = null
    var productsProvider: ProductsProvider? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var idCategory = ""

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
        spinnerCategories = myView?.findViewById(R.id.spinner_categories)

        btnCreateProduct?.setOnClickListener { createProduct() }
        ivImageProduct1?.setOnClickListener { selectImage(101) }
        ivImageProduct2?.setOnClickListener { selectImage(102) }
        ivImageProduct3?.setOnClickListener { selectImage(103) }

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        productsProvider = ProductsProvider(user?.sessionToken!!)

        getCategories()

        return myView
    }

    private fun getCategories(){
        //enqueue es para traer los datos
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null){
                    categories = response.body()!!

                    val arrayAdapter = ArrayAdapter<Category>(requireActivity(), android.R.layout.simple_dropdown_item_1line, categories)
                    spinnerCategories?.adapter = arrayAdapter
                    spinnerCategories?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                            idCategory = categories[position].id!!
                            Log.d(TAG, "id category: $idCategory")
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        } )
    }

    private fun createProduct() {
        val name = etNameProduct?.text.toString()
        val description = etDescriptionProduct?.text.toString()
        val priceText = etPriceProduct?.text.toString()
        val files = java.util.ArrayList<File>()

        if (isValidForm(name,description,priceText)){

            val product = Product(
                name= name,
                description = description,
                price = priceText.toDouble(),
                idCategory = idCategory
            )

            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)

            productsProvider?.create(files, product)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG, "Response: $response")
                    Log.d(TAG, "Body: ${response.body()}")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun isValidForm(name:String, description: String, price:String):Boolean{

        if (name.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa el nombre del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if (description.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa la descripción del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if (price.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa el precio del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if (imageFile1 == null){
            Toast.makeText(requireContext(), "Selecciona la imagen 1", Toast.LENGTH_SHORT).show()
            return false
        }

        if (imageFile2 == null){
            Toast.makeText(requireContext(), "Selecciona la imagen 2", Toast.LENGTH_SHORT).show()
            return false
        }

        if (imageFile3 == null){
            Toast.makeText(requireContext(), "Selecciona la imagen 3", Toast.LENGTH_SHORT).show()
            return false
        }

        if (idCategory.isNullOrBlank()){
            Toast.makeText(requireContext(), "Selecciona la categoria del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
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

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }


}