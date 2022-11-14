package com.alex.deliveryapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alex.deliveryapp.fragments.delivery.DeliveryOrdersStatusFragment
import com.alex.deliveryapp.utils.Constants

class DeliveryTabsPagerAdapter(
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
                bundle.putString(Constants.STATUS, "DESPACHADO")
                val deliveryStatusFragment = DeliveryOrdersStatusFragment()
                deliveryStatusFragment.arguments = bundle
                return deliveryStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "EN CAMINO")
                val deliveryStatusFragment = DeliveryOrdersStatusFragment()
                deliveryStatusFragment.arguments = bundle
                return deliveryStatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "ENTREGADO")
                val deliveryStatusFragment = DeliveryOrdersStatusFragment()
                deliveryStatusFragment.arguments = bundle
                return deliveryStatusFragment
            }
            else -> return DeliveryOrdersStatusFragment()
        }
    }
}