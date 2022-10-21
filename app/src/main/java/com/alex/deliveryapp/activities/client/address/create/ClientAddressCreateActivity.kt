package com.alex.deliveryapp.activities.client.address.create

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.address.map.ClientAddressMapActivity
import com.alex.deliveryapp.utils.Constants

class ClientAddressCreateActivity : AppCompatActivity() {
    val TAG = "ClientAddressCreate"

    var toolbar: Toolbar? = null
    var etRefPoint: EditText? = null
    var etAddress: EditText? = null
    var etNeighborhood: EditText? = null
    var btnCreateAddress: Button? = null

    var addressLat = 0.0
    var addressLng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_create)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Nueva dirección"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etRefPoint = findViewById(R.id.et_ref_point)
        etAddress = findViewById(R.id.et_address)
        etNeighborhood = findViewById(R.id.et_neighborhood)
        btnCreateAddress = findViewById(R.id.btn_create_address)

        etRefPoint?.setOnClickListener { goToAddressMap() }

        btnCreateAddress?.setOnClickListener {  }
    }

    private fun createAddress(){
        val address = etAddress?.text.toString()
        val neighborhood = etNeighborhood?.text.toString()

        if (isValidForm(address, neighborhood)){
            //Lanzar petición a nodejs
        }
    }

    private fun isValidForm(address: String, neighborhood: String ):Boolean{
        if (address.isNullOrBlank()){
            Toast.makeText(this, "Ingresa la dirección", Toast.LENGTH_SHORT).show()
            return false
        }
        if (neighborhood.isNullOrBlank()){
            Toast.makeText(this, "Ingresa la colonia", Toast.LENGTH_SHORT).show()
            return false
        }

        if (addressLat == 0.0){
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }

        if (addressLng == 0.0){
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    //Creamos esto para poder esperar unos datos que nos tiene que pasar el activity hijo
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val city = data?.getStringExtra(Constants.CITY)
            val address = data?.getStringExtra(Constants.ADDRESS)
            val country = data?.getStringExtra(Constants.COUNTRY)
            addressLat = data?.getDoubleExtra(Constants.LAT, 0.0)!!
            addressLng = data?.getDoubleExtra(Constants.LNG, 0.0)!!

            etRefPoint?.setText("$address $city")

            Log.d(TAG, "City: $city")
            Log.d(TAG, "Address: $address")
            Log.d(TAG, "Country: $country")
            Log.d(TAG, "Lat: $addressLat")
            Log.d(TAG, "Lng: $addressLng")
        }

    }

    private fun goToAddressMap(){
        val i = Intent(this, ClientAddressMapActivity::class.java)
        //Aqui ejecutamos la variable y se queda en escucha esta activity de un resultado que le enviará el activity hijo
        resultLauncher.launch(i)
    }
}