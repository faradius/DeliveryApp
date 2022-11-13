package com.alex.deliveryapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alex.deliveryapp.fragments.client.ClientOrdersStatusFragment
import com.alex.deliveryapp.fragments.restaurant.RestaurantOrdersStatusFragment
import com.alex.deliveryapp.utils.Constants

class RestaurantTabsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var numberOfTabs: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "PAGADO")
                val restaurantStatusFragment = RestaurantOrdersStatusFragment()
                restaurantStatusFragment.arguments = bundle
                return restaurantStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "DESPACHADO")
                val restaurantStatusFragment = RestaurantOrdersStatusFragment()
                restaurantStatusFragment.arguments = bundle
                return restaurantStatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "EN CAMINO")
                val restaurantStatusFragment = RestaurantOrdersStatusFragment()
                restaurantStatusFragment.arguments = bundle
                return restaurantStatusFragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "ENTREGADO")
                val restaurantStatusFragment = RestaurantOrdersStatusFragment()
                restaurantStatusFragment.arguments = bundle
                return restaurantStatusFragment
            }
            else -> return RestaurantOrdersStatusFragment()
        }
    }
}