package com.alex.deliveryapp.activities.client.address.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alex.deliveryapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class ClientAddressMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }
}