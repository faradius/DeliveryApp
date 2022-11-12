package com.alex.deliveryapp.fragments.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.adapters.OrdersClientAdapter
import com.alex.deliveryapp.models.Order
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.OrdersProvider
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientOrdersStatusFragment : Fragment() {

    var myView:View? = null
    var ordersProvider:OrdersProvider? = null
    var user: User? = null
    var sharedPref:SharedPref? = null

    var rvOrders: RecyclerView? = null
    var adapter:OrdersClientAdapter? = null

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_orders_status, container, false)

        sharedPref = SharedPref(requireActivity())

        status = arguments?.getString(Constants.STATUS)!!

        getUserFromSession()
        ordersProvider = OrdersProvider(user?.sessionToken!!)

        rvOrders = myView?.findViewById(R.id.rv_orders)
        rvOrders?.layoutManager = LinearLayoutManager(requireContext())

        getOrders()

        return myView
    }

    private fun getOrders(){
        ordersProvider?.getOrdersByStatus(status)?.enqueue(object: Callback<ArrayList<Order>>{
            override fun onResponse(
                call: Call<ArrayList<Order>>,
                response: Response<ArrayList<Order>>
            ) {
                if (response.body() != null){
                    val orders = response.body()
                    adapter = OrdersClientAdapter(requireActivity(), orders!!)
                    rvOrders?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                Toast.makeText(requireActivity(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

}