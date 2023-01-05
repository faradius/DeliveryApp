package com.alex.deliveryapp.activities.client.payments.payment_method

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.payments.mercado_pago.form.ClientPaymentFormActivity
import com.alex.deliveryapp.activities.client.payments.paypal.form.ClientPaymentPaypalFormActivity

class ClientPaymentMethodActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var ivMercadoPago: ImageView? = null
    var ivPaypal: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_method)

        toolbar = findViewById(R.id.toolbar)
        ivMercadoPago = findViewById(R.id.iv_mercado_pago)
        ivPaypal = findViewById(R.id.iv_paypal)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Metodos de Pago"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ivMercadoPago?.setOnClickListener { goToMercadoPago() }
        ivPaypal?.setOnClickListener { goToPaypal() }
    }

    private fun goToMercadoPago(){
        val i = Intent(this, ClientPaymentFormActivity::class.java)
        startActivity(i)
    }

    private fun goToPaypal(){
        val i = Intent(this, ClientPaymentPaypalFormActivity::class.java)
        startActivity(i)
    }
}