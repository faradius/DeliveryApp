package com.alex.deliveryapp.activities.client.payments.mercado_pago.status

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.home.ClientHomeActivity
import com.alex.deliveryapp.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView

class ClientPaymentStatusActivity : AppCompatActivity() {

    var tvStatus: TextView? = null
    var cvStatus: CircleImageView? = null
    var btnFinish: Button? = null

    var paymentMethodId = ""
    var paymentStatus = ""
    var lastFourDigits = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_status)

        tvStatus = findViewById(R.id.tv_status)
        cvStatus = findViewById(R.id.cv_status_pay)
        btnFinish = findViewById(R.id.btn_finish)

        paymentMethodId = intent.getStringExtra(Constants.PAYMENT_METHOD_ID).toString()
        paymentStatus = intent.getStringExtra(Constants.PAYMENT_STATUS).toString()
        lastFourDigits = intent.getStringExtra(Constants.LAST_FOUR_DIGITS).toString()

        if (paymentStatus == "approved"){
            cvStatus?.setImageResource(R.drawable.ic_check)
            tvStatus?.text = "Tu orden fue procesada exitosamente usando ( $paymentMethodId **** $lastFourDigits ) \n\nMira el estado de tu compra en la sección de Mis Pedidos"
        }else{
            cvStatus?.setImageResource(R.drawable.ic_cancel)
            tvStatus?.text = "Hubo un error procesando el pago"
        }

        btnFinish?.setOnClickListener { goToHome() }
    }

    private fun goToHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}