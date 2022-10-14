package com.alex.deliveryapp.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.CategoriesProvider
import com.alex.deliveryapp.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantCategoryFragment : Fragment() {

    private val TAG = "CategoryFragment"

    var myView: View? = null

    var ivCategory: ImageView? = null
    var etNameCategory: EditText? = null
    var btnCreateCategory: Button? = null

    private var imageFile: File? = null

    var categoriesProvider: CategoriesProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_category, container, false)

        sharedPref = SharedPref(requireActivity())

        ivCategory = myView?.findViewById(R.id.iv_category)
        etNameCategory = myView?.findViewById(R.id.et_name_category)
        btnCreateCategory = myView?.findViewById(R.id.btn_create_category)

        ivCategory?.setOnClickListener { selectImage() }
        btnCreateCategory?.setOnClickListener { createCategory() }

        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)

        return myView
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun createCategory(){
        val nameCategory = etNameCategory?.text.toString()

        if (imageFile != null){

            val category = Category(name = nameCategory)

            categoriesProvider?.create(imageFile!!, category)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_LONG).show()

                    if(response.body()?.isSuccess == true){
                        clearForm()
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }else{
            Toast.makeText(requireContext(), "Seleccione una imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm(){
        etNameCategory?.setText("")
        imageFile = null
        ivCategory?.setImageResource(R.drawable.ic_image)
    }

    //Esta variable captura lo que el usuario seleccionó
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
            val resultCode = result.resultCode
            //aqui capturamos los datos que nos devuelve el creatIntent del ImagePicker
            val data = result.data

            //Si el usuario seleccionó una imagen correctamente
            if (resultCode == Activity.RESULT_OK){
                //Transformamos esa imagen en un archivo que almacenamos en el imageFile
                //Vamos a crear un archivo
                val fileUri = data?.data
                //El archivo que vamos a guardar como imagen en el servidor, fileUri.path viene siendo la ruta de la imagen en forma de URL
                imageFile = File(fileUri?.path)
                //aqui lo que hacemos es colocarle al ivCircleUser la imagen seleccionada en forma de url
                ivCategory?.setImageURI(fileUri)

            }else if(resultCode == ImagePicker.RESULT_ERROR){

                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()

            }else{

                Toast.makeText(requireContext(), "La tarea se cancelo", Toast.LENGTH_LONG).show()
            }
        }

    //Esta funcion hace que el ImagePicker permita seleccionar una imagen con la ayuda de la variable startImageForResult
    private fun selectImage() {
        ImagePicker.with(this)
            .crop() //Permitir recortar la imagen al gusto del usuario
            .compress(1024) //comprimir la imagen de forma adecuada al servidor
            .maxResultSize(1080, 1080) //tamaño maximo permitido para imagenes en pixeles
            .createIntent { intent: Intent ->
                startImageForResult.launch(intent)
            }
    }

}