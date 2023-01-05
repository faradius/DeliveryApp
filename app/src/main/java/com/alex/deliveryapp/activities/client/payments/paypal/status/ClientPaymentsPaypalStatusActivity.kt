package com.alex.deliveryapp.activities.client.payments.paypal.status

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.home.ClientHomeActivity
import com.alex.deliveryapp.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView

class ClientPaymentsPaypalStatusActivity : AppCompatActivity() {

    var tvStatus: TextView? = null
    var cvStatus: CircleImageView? = null
    var btnFinish: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payments_paypal_status)

        tvStatus = findViewById(R.id.tv_status)
        cvStatus = findViewById(R.id.cv_status_pay)
        btnFinish = findViewById(R.id.btn_finish)

        cvStatus?.setImageResource(R.drawable.ic_check)
        tvStatus?.text = "Tu orden fue procesada exitosamente usando"

        btnFinish?.setOnClickListener { goToHome() }
    }

    private fun goToHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}