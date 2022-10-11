package com.alex.deliveryapp.fragments.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.SelectRolesActivity


class ClientProfileFragment : Fragment() {

    var myView: View? = null
    var btnSelectRol: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)
        btnSelectRol = myView?.findViewById(R.id.btn_select_rol)

        btnSelectRol?.setOnClickListener { goToSelectRol() }

        return myView
    }

    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}