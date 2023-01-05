package com.alex.deliveryapp.activities.client.payments.mercado_pago.installments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.payments.mercado_pago.status.ClientPaymentStatusActivity
import com.alex.deliveryapp.adapters.ShoppingCarAdapter
import com.alex.deliveryapp.models.*
import com.alex.deliveryapp.providers.MercadoPagoProvider
import com.alex.deliveryapp.providers.PaymentsProvider
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
    var progress: FrameLayout? = null

    var mercadoPagoProvider = MercadoPagoProvider()
    var paymentsProvider: PaymentsProvider? = null

    var cardToken = ""
    var firstSixDigits = ""

    var sharedPref:SharedPref? = null
    var user:User? = null
    var selectedProducts = ArrayList<Product>()
    var gson = Gson()

    var total = 0.0

    var installmentsSelected = "" //Se almacena la cuota seleccionada

    var address: Address? = null

    var paymentMethodId = ""
    var paymentTypeId = ""
    var issuerId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payments_installments)

        sharedPref = SharedPref(this)

        getUserFromSession()
        getAddressFromSession()

        paymentsProvider = PaymentsProvider(user?.sessionToken!!)

        cardToken = intent.getStringExtra(Constants.CARD_TOKEN).toString()
        firstSixDigits = intent.getStringExtra(Constants.FIRST_SIX_DIGITS).toString()

        tvTotal = findViewById(R.id.tv_total)
        tvInstallmentsDescription = findViewById(R.id.tv_installments_description)
        btnPay = findViewById(R.id.btn_pay)
        spinnerInstallments = findViewById(R.id.spinner_installments)
        progress =  findViewById(R.id.progress)

        getProductsFromSharedPref()
        getInstallments()

        btnPay?.setOnClickListener {
            if (!installmentsSelected.isNullOrBlank()){
                createPayment()
            }else{
                Toast.makeText(this, "Debe seleccionar el numero de mensualidades", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createPayment(){

        val order = Order(
            products = selectedProducts,
            idClient = user?.id!!,
            idAddress = address?.id!!
        )

        val payer = Payer(email = user?.email!!)

        val mercadoPagoPayment = MercadoPagoPayment(
            order = order,
            token = cardToken,
            description = "DeliveryFoodApp",
            paymentMethodId = paymentMethodId,
            paymentTypeId = paymentTypeId,
            issuerId = issuerId,
            payer = payer,
            transactionAmount = total,
            installments = installmentsSelected.toInt()
        )

        progress?.visibility = View.VISIBLE

        paymentsProvider?.create(mercadoPagoPayment)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                progress?.visibility = View.GONE

                if (response.body() != null){

                    if (response.body()?.isSuccess == true){
                        sharedPref?.remove(Constants.ORDER)
                    }

                    Log.d(TAG, "Response: $response")
                    Log.d(TAG, "Body: ${response.body()}")

                    Toast.makeText(this@ClientPaymentsInstallmentsActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    val status = response.body()?.data?.get("status")?.asString
                    val lastFour = response.body()?.data?.get("card")?.asJsonObject?.get("last_four_digits")?.asString
                    goToPaymentsStatus(paymentMethodId, status!!, lastFour!!)

                }else{
                    goToPaymentsStatus(paymentMethodId, "denied", "")
                    Toast.makeText(this@ClientPaymentsInstallmentsActivity, "No hubo una respuesta exitosa", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                progress?.visibility = View.GONE
                Toast.makeText(this@ClientPaymentsInstallmentsActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun goToPaymentsStatus(paymentMethodId: String, paymentStatus: String, lastFourDigits: String){
        val i = Intent(this, ClientPaymentStatusActivity::class.java)
        i.putExtra(Constants.PAYMENT_METHOD_ID, paymentMethodId)
        i.putExtra(Constants.PAYMENT_STATUS, paymentStatus)
        i.putExtra(Constants.LAST_FOUR_DIGITS, lastFourDigits)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
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

                    paymentMethodId = response.body()?.get(0)?.asJsonObject?.get("payment_method_id")?.asString!!
                    paymentTypeId = response.body()?.get(0)?.asJsonObject?.get("payment_type_id")?.asString!!
                    issuerId = response.body()?.get(0)?.asJsonObject?.get("issuer")?.asJsonObject?.get("id")?.asString!!

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

    private fun getUserFromSession(){
        val gson = Gson()

        //Si el usuario existe en sesión
        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun getAddressFromSession(){
        //Si el usuario ya elegio una dirección
        if (!sharedPref?.getData(Constants.ADDRESS).isNullOrBlank()){
            address = gson.fromJson(sharedPref?.getData(Constants.ADDRESS), Address::class.java)
        }
    }
}