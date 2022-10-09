package com.alex.deliveryapp.activities


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.home.ClientHomeActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    var circleimageUser: CircleImageView? = null
    var btnNext: Button? = null
    var btnConfirm: Button? = null

    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        circleimageUser = findViewById(R.id.iv_circle_user)
        btnNext = findViewById(R.id.btn_next)
        btnConfirm = findViewById(R.id.btn_confirm)

        circleimageUser?.setOnClickListener { selectImage() }

        btnNext?.setOnClickListener { goToClientHome() }

        btnConfirm?.setOnClickListener {  }
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
                circleimageUser?.setImageURI(fileUri)

            }else if(resultCode == ImagePicker.RESULT_ERROR){

                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()

            }else{

                Toast.makeText(this, "La tarea se cancelo", Toast.LENGTH_LONG).show()
            }
        }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop() //Permitir recortar la imagen al gusto del usuario
            .compress(1024) //comprimir la imagen de forma adecuada al servidor
            .maxResultSize(1080, 1080) //tamaño maximo permitido para imagenes en pixeles
            .createIntent { intent: Intent ->
                startImageForResult.launch(intent)
            }
    }

    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        //Esto es para eliminar el historial de pantallas
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}