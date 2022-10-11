package com.alex.deliveryapp.activities.restaurant.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.MainActivity
import com.alex.deliveryapp.fragments.client.ClientCategoriesFragment
import com.alex.deliveryapp.fragments.client.ClientOrdersFragment
import com.alex.deliveryapp.fragments.client.ClientProfileFragment
import com.alex.deliveryapp.fragments.restaurant.RestaurantCategoryFragment
import com.alex.deliveryapp.fragments.restaurant.RestaurantOrdersFragment
import com.alex.deliveryapp.fragments.restaurant.RestaurantProductFragment
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class RestaurantHomeActivity : AppCompatActivity() {

    private val TAG = "RestaurantHomeActivity"
    //var btnLogout: Button? = null
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_home)
        //btnLogout = findViewById(R.id.btn_logout)
        sharedPref = SharedPref(this)

        /*btnLogout?.setOnClickListener {
            logout()
        }*/
        openFragment(RestaurantOrdersFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation_restaurant)
        bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_orders -> {
                    openFragment(RestaurantOrdersFragment())
                    true
                }

                R.id.item_category -> {
                    openFragment(RestaurantCategoryFragment())
                    true
                }

                R.id.item_product -> {
                    openFragment(RestaurantProductFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }

                else -> false
            }
        }

        getUserFromSession()
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerRestaurant, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG, "Usuario: $user")
        }
    }
}