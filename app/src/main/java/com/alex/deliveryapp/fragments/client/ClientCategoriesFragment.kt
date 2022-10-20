package com.alex.deliveryapp.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.shopping_car.ClientShoppingCarActivity
import com.alex.deliveryapp.adapters.CategoriesAdapter
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.CategoriesProvider
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientCategoriesFragment : Fragment() {

    val TAG ="CategoryFragment"

    var myView: View? = null
    var rvCategories: RecyclerView? = null
    var adapter: CategoriesAdapter? = null
    var user: User? = null
    var categoriesProvider: CategoriesProvider? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_categories, container, false)

        setHasOptionsMenu(true)
        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        toolbar?.title = "Categorías"
        //Ponemos esta linea para que el fragment soporte esta toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        rvCategories = myView?.findViewById(R.id.rv_categories)
        rvCategories?.layoutManager = LinearLayoutManager(requireContext()) //Definimos que los elementos se mostrarán de forma vertical
        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriesProvider = CategoriesProvider(user?.sessionToken!!)

        getCategories()

        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_car, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_shopping_car){
            goToShoppingCar()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun goToShoppingCar(){
        val i = Intent(requireContext(), ClientShoppingCarActivity::class.java)
        startActivity(i)
    }

    private fun getCategories(){
        //enqueue es para traer los datos
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>>{
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null){
                    categories = response.body()!!
                    adapter = CategoriesAdapter(requireActivity(),categories)
                    rvCategories?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        } )
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}