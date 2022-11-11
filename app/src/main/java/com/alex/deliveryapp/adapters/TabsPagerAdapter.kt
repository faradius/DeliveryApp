package com.alex.deliveryapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alex.deliveryapp.fragments.client.ClientOrdersStatusFragment
import com.alex.deliveryapp.utils.Constants

class TabsPagerAdapter(
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
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "DESPACHADO")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "EN CAMINO")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString(Constants.STATUS, "ENTREGADO")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            else -> return ClientOrdersStatusFragment()
        }
    }
}