package com.alex.deliveryapp.activities.delivery.orders.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.alex.deliveryapp.R
import com.alex.deliveryapp.utils.Constants
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class DeliveryOrdersMapActivity : AppCompatActivity(), OnMapReadyCallback {
    var TAG = "DeliveryAddressMap"

    var googleMap: GoogleMap? = null

    val PERMISSION_ID = 42
    var fusedLocationClient: FusedLocationProviderClient? = null


    var btnAcceptLocationMap: Button? = null

    var city = ""
    var country = ""
    var address = ""
    var addressLatLng: LatLng? = null

    var markerDelivery: Marker? = null
    var myLocationLatLng: LatLng? = null

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            //Con esto podemos ver la localización en la que nos encontramos
            var lastLocation = locationResult.lastLocation
            Log.d("LOCALIZACION", "Callback: $lastLocation ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        //Esta variable nos va ayudar a iniciar el servicio para encontrar nuestra localización
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        btnAcceptLocationMap = findViewById(R.id.btn_accept_location)

        getLastLocation()

        btnAcceptLocationMap?.setOnClickListener { goToCreateAddress() }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    private fun addDeliveryMarker(){
        markerDelivery = googleMap?.addMarker(
            MarkerOptions()
                .position(myLocationLatLng)
                .title("Mi posición")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_man))
        )
    }

    private fun goToCreateAddress(){
        val i = Intent()
        i.putExtra(Constants.CITY, city)
        i.putExtra(Constants.ADDRESS, address)
        i.putExtra(Constants.COUNTRY, country)
        i.putExtra(Constants.LAT, addressLatLng?.latitude)
        i.putExtra(Constants.LNG, addressLatLng?.longitude)
        setResult(RESULT_OK, i)
        finish() //Destruye la activtiy y vuelve hacia atras
    }

    private fun getLastLocation(){
        //Primero verificamos los permisos
        if(checkPermission()){
            if (isLocationEnabled()){
                fusedLocationClient?.lastLocation?.addOnCompleteListener {  task ->

                    var location = task.result
                    myLocationLatLng = LatLng(location.latitude, location.longitude)

                    addDeliveryMarker()

                    if (location == null){
                        requestNewLocationData()
                    }else{
                        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder().target(
                                LatLng(location.latitude, location.longitude)
                            ).zoom(15f).build()
                        ))
                    }
                }
            }else{
                Toast.makeText(this, "Habilita la localización", Toast.LENGTH_LONG).show()
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }

        }else{ //Si no solicitamos los permisos
            requestPermissions()
        }
    }

    //Son configuraciones y se puede reutilizar en otro proyecto
    private fun requestNewLocationData(){
        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        //Este es el que empezará a escuchar la localización en tiempo real
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun isLocationEnabled():Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }
    }
}