package com.alex.deliveryapp.activities.delivery.orders.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.alex.deliveryapp.models.Order
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.OrdersProvider
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide
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
import com.google.gson.Gson
import com.maps.route.extensions.drawRouteOnMap
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryOrdersMapActivity : AppCompatActivity(), OnMapReadyCallback {
    var TAG = "DeliveryAddressMap"

    var googleMap: GoogleMap? = null

    val PERMISSION_ID = 42
    var fusedLocationClient: FusedLocationProviderClient? = null

    var city = ""
    var country = ""
    var address = ""
    var addressLatLng: LatLng? = null

    var markerDelivery: Marker? = null
    var markerAddress: Marker? = null
    var myLocationLatLng: LatLng? = null

    var order: Order? = null
    var gson = Gson()

    var tvNameClient: TextView? = null
    var tvAddress: TextView? = null
    var tvNeighborhood: TextView? = null
    var btnDelivered: Button? = null
    var ivCircleUser: CircleImageView? = null
    var ivPhone: ImageView? = null

    val REQUEST_PHONE_CALL = 30

    var ordersProvider: OrdersProvider? = null

    var user:User? = null
    var sharedPref: SharedPref? = null

    var distanceBetween = 0.0f

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            //Con esto podemos ver la localización en la que nos encontramos
            var lastLocation = locationResult.lastLocation
            //Aqui logramos actualizar la locación del repartidor en tiempo real, se ejecuta varias veces cuando detecte un cambio
            myLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)

//            googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
//                CameraPosition.builder().target(
//                    LatLng(myLocationLatLng?.latitude!!, myLocationLatLng?.longitude!!)
//                ).zoom(15f).build()
//            ))

            distanceBetween = getDistanceBetween(myLocationLatLng!!, addressLatLng!!)

            Log.d(TAG, "Distancia: $distanceBetween")

            //Eliminamos el marcador anterior y despues redibujamos el marcador
            removeDeliveryMarker()
            //Cada vez que hay un cambio volvemos a dibujar el marcador
            addDeliveryMarker()
            Log.d("LOCALIZACION", "Callback: $lastLocation ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_map)

        sharedPref = SharedPref(this)
        getUserFromSession()

        ordersProvider = OrdersProvider(user?.sessionToken!!)

        order = gson.fromJson(intent.getStringExtra(Constants.ORDER), Order::class.java)

        addressLatLng = LatLng(order?.address?.lat!!, order?.address?.lng!!)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        //Esta variable nos va ayudar a iniciar el servicio para encontrar nuestra localización
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        tvNameClient = findViewById(R.id.tv_client_orders_delivery)
        tvAddress = findViewById(R.id.tv_address_orders_delivery)
        tvNeighborhood = findViewById(R.id.tv_neighborhood_orders_delivery)
        ivCircleUser = findViewById(R.id.iv_circle_user_orders_delivery)
        ivPhone = findViewById(R.id.iv_phone_orders_delivery)
        btnDelivered = findViewById(R.id.btn_delivery_order)

        getLastLocation()

        tvNameClient?.text = "${order?.client?.name} ${order?.client?.lastName}"
        tvAddress?.text = order?.address?.address
        tvNeighborhood?.text = order?.address?.neighborhood

        if (!order?.client?.image.isNullOrBlank()){
            Glide.with(this).load(order?.client?.image).into(ivCircleUser!!)
        }


        btnDelivered?.setOnClickListener {
            if (distanceBetween <= 350){
                updateOrder()
            }else{
                Toast.makeText(this, "Acercate mas al lugar de entrega", Toast.LENGTH_LONG).show()
            }
        }

        ivPhone?.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            }else{
                call()
            }
        }

    }

    private fun updateOrder(){
        ordersProvider?.updateToDelivered(order!!)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body() != null){
                    Toast.makeText(this@DeliveryOrdersMapActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()

                    if (response.body()?.isSuccess == true){
                        goToHome()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DeliveryOrdersMapActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        } )
    }

    private fun goToHome(){
        val i = Intent(this, DeliveryHomeActivity::class.java)
        startActivity(i)
    }

    private fun getDistanceBetween(fromLatLng: LatLng, toLatLng: LatLng): Float{
        var distance = 0.0f

        val from = Location("")
        val to = Location("")

        from.latitude = fromLatLng.latitude
        from.longitude = fromLatLng.longitude
        to.latitude = toLatLng.latitude
        to.longitude = toLatLng.longitude

        distance = from.distanceTo(to)

        return distance
    }

    private fun call(){
        val i = Intent(Intent.ACTION_CALL)
        i.data = Uri.parse("tel:${order?.client?.phone}")

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permiso denegado para realizar la llamada", Toast.LENGTH_SHORT).show()
            return
        }

        startActivity(i)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        //Mostrar los botones de zoom
        googleMap?.uiSettings?.isZoomControlsEnabled = true
    }

    private fun drawRoute(){
        val addressLocation = LatLng(order?.address?.lat!!, order?.address?.lng!!)

        googleMap?.drawRouteOnMap(
            getString(R.string.google_map_api_key),
            source = myLocationLatLng!!,
            destination = addressLocation,
            context = this,
            color = Color.BLACK,
            polygonWidth = 10,
            boundMarkers = false,
            markers = false
        )
    }

    private fun removeDeliveryMarker(){
        markerDelivery?.remove()
    }

    private fun addDeliveryMarker(){
        markerDelivery = googleMap?.addMarker(
            MarkerOptions()
                .position(myLocationLatLng)
                .title("Mi posición")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_man))
        )
    }

    private fun addAddressMarker(){
        val addressLocation = LatLng(order?.address?.lat!!, order?.address?.lng!!)

        markerAddress = googleMap?.addMarker(
            MarkerOptions()
                .position(addressLocation)
                .title("Entregar Aqui")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home))
        )
    }

    private fun getLastLocation(){
        //Primero verificamos los permisos
        if(checkPermission()){
            if (isLocationEnabled()){

                //Iniciamos la posición en tiempo real
                requestNewLocationData()

                //Este nos da la ubicación actual pero se ejecuta una sola vez
                fusedLocationClient?.lastLocation?.addOnCompleteListener {  task ->

                    var location = task.result
                    myLocationLatLng = LatLng(location.latitude, location.longitude)

                    removeDeliveryMarker()
                    addDeliveryMarker()
                    addAddressMarker()
                    drawRoute()

                    googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition.builder().target(
                            LatLng(location.latitude, location.longitude)
                        ).zoom(15f).build()
                    ))

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
        //Este inicializa la posición en tiempo real
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

        if (requestCode == REQUEST_PHONE_CALL){
            call()
        }
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}