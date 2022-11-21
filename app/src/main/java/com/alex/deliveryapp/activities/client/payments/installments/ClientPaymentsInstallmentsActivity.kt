package com.alex.deliveryapp.activities.client.payments.installments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.alex.deliveryapp.R
import com.alex.deliveryapp.adapters.ShoppingCarAdapter
import com.alex.deliveryapp.models.Category
import com.alex.deliveryapp.models.MercadoPagoInstallments
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.providers.MercadoPagoProvider
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientPaymentsInstallmentsActivity : AppCompatActivity() {

    val TAG = "ClientPaymentsInst"

    var tvTotal: TextView? = null
    var tvInstallmentsDescription: TextView? = null
    var btnPay: Button? = null
    var spinnerInstallments: Spinner? = null

    var mercadoPagoProvider = MercadoPagoProvider()

    var cardToken = ""
    var firstSixDigits = ""

    var sharedPref:SharedPref? = null
    var selectedProducts = ArrayList<Product>()
    var gson = Gson()

    var total = 0.0

    var installmentsSelected = "" //Se almacena la cuota seleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payments_installments)

        sharedPref = SharedPref(this)

        cardToken = intent.getStringExtra(Constants.CARD_TOKEN).toString()
        firstSixDigits = intent.getStringExtra(Constants.FIRST_SIX_DIGITS).toString()

        getProductsFromSharedPref()

        tvTotal = findViewById(R.id.tv_total)
        tvInstallmentsDescription = findViewById(R.id.tv_installments_description)
        btnPay = findViewById(R.id.btn_pay)
        spinnerInstallments = findViewById(R.id.spinner_installments)

        getInstallments()
    }

    private fun getInstallments(){
        mercadoPagoProvider.getInstallments(firstSixDigits, "$total")?.enqueue(object: Callback<JsonArray> {
            override fun onResponse(
                call: Call<JsonArray>,
                response: Response<JsonArray>
            ) {
                if (response.body() != null){
                   val jsonInstallments = response.body()!!.get(0).asJsonObject.get("payer_costs").asJsonArray

                    //Se hace la conversión de a un JsonArray a un array de mercado pago installments
                    val type = object: TypeToken<ArrayList<MercadoPagoInstallments>>(){}.type
                    val installments = gson.fromJson<ArrayList<MercadoPagoInstallments>>(jsonInstallments, type)

                    Log.d(TAG, "Response: $response")
                    Log.d(TAG, "installmets: $installments")

                    val arrayAdapter = ArrayAdapter<MercadoPagoInstallments>(this@ClientPaymentsInstallmentsActivity, android.R.layout.simple_dropdown_item_1line, installments)
                    spinnerInstallments?.adapter = arrayAdapter
                    spinnerInstallments?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                            installmentsSelected = "${installments[position].installments}"
                            Log.d(TAG, "Cuota Seleccionada: $installmentsSelected")
                            tvInstallmentsDescription?.text = installments[position].recommendedMessage
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {

                Toast.makeText(this@ClientPaymentsInstallmentsActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        } )
    }

    private fun getProductsFromSharedPref(){
        //Si existe una orden en sharedPreferences
        if (!sharedPref?.getData(Constants.ORDER).isNullOrBlank()){
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            selectedProducts = gson.fromJson(sharedPref?.getData(Constants.ORDER), type)

            for (p in selectedProducts){
                total = total + (p.price * p.quantity!!)
            }

            tvTotal?.text = "$$total"
        }
    }
}