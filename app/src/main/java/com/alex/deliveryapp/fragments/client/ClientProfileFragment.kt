package com.alex.deliveryapp.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.MainActivity
import com.alex.deliveryapp.activities.SelectRolesActivity
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView


class ClientProfileFragment : Fragment() {

    var myView: View? = null
    var btnSelectRol: Button? = null
    var btnUpdateProfile: Button? = null
    var circleImageUserProfile: CircleImageView? = null
    var tvNameUserProfile: TextView? = null
    var tvEmailUserProfile: TextView? = null
    var tvPhoneUserProfile: TextView? = null
    var ivLogout: ImageView? = null

    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        btnSelectRol = myView?.findViewById(R.id.btn_select_rol_profile)
        btnUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        circleImageUserProfile = myView?.findViewById(R.id.iv_circle_user_profile)
        tvNameUserProfile = myView?.findViewById(R.id.tv_name_user_profile)
        tvEmailUserProfile = myView?.findViewById(R.id.tv_email_user_profile)
        tvPhoneUserProfile = myView?.findViewById(R.id.tv_phone_user_profile)
        ivLogout = myView?.findViewById(R.id.iv_logout)

        btnSelectRol?.setOnClickListener { goToSelectRol() }
        ivLogout?.setOnClickListener { logout() }

        getUserFromSession()

        tvNameUserProfile?.text = "${user?.name} ${user?.lastName}"
        tvEmailUserProfile?.text = user?.email
        tvPhoneUserProfile?.text = user?.phone

        if (!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(circleImageUserProfile!!)
        }


        return myView
    }

    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

}