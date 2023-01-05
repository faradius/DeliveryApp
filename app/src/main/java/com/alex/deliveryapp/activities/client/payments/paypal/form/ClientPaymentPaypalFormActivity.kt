package com.alex.deliveryapp.activities.client.payments.paypal.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.alex.deliveryapp.R
import com.alex.deliveryapp.activities.client.payments.paypal.status.ClientPaymentsPaypalStatusActivity
import com.alex.deliveryapp.adapters.ShoppingCarAdapter
import com.alex.deliveryapp.models.Address
import com.alex.deliveryapp.models.Product
import com.alex.deliveryapp.models.ResponseHttp
import com.alex.deliveryapp.models.User
import com.alex.deliveryapp.providers.OrdersProvider
import com.alex.deliveryapp.utils.Constants
import com.alex.deliveryapp.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PayPalButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientPaymentPaypalFormActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var tvAmount: TextView? = null
    var paypalButtom: PayPalButton? =null
    var ordersProvider: OrdersProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var selectedProducts = ArrayList<Product>()
    var adress:Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_paypal_form)

        sharedPref = SharedPref(this)
        getUserFromSession()
        getProductsFromSharedPref()
        getAddressFromSession()
        ordersProvider = OrdersProvider(user?.sessionToken!!)

        toolbar = findViewById(R.id.toolbar)
        tvAmount = findViewById(R.id.tv_amount)
        paypalButtom = findViewById(R.id.payPalButton)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Procesar Pago"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        paypalButtom?.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->

                    if(captureOrderResult.toString().contains("Success")){
                        Toast.makeText(this, "Pago Aprobado", Toast.LENGTH_LONG).show()
                        createOrder()
                    }


                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                }
            },
            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
            },
            onError = OnError { errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
            }
        )
    }

    private fun goToPaypalStatus(){
        val i = Intent(this, ClientPaymentsPaypalStatusActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun createOrder(){

        val order = com.alex.deliveryapp.models.Order(
            products = selectedProducts,
            idClient = user?.id!!,
            idAddress = adress?.id!!
        )

        ordersProvider?.create(order)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body() != null){
                    if (response.body()?.isSuccess == true){
                        sharedPref?.remove(Constants.ORDER)
                        goToPaypalStatus()
                    }
                    Toast.makeText(this@ClientPaymentPaypalFormActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@ClientPaymentPaypalFormActivity, "Ocurrio un error en el servidor", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@ClientPaymentPaypalFormActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
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

    private fun getProductsFromSharedPref(){
        //Si existe una orden en sharedPreferences
        if (!sharedPref?.getData(Constants.ORDER).isNullOrBlank()){
            //Aqui indicamos que vamos a transformar una lista de tipo Gson a una lista de tipo product
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            //Aqui le indicamos que los datos que se obtienen del sharePref se convierte a un tipo de objeto product
            selectedProducts = gson.fromJson(sharedPref?.getData(Constants.ORDER), type)
        }
    }

    private fun getAddressFromSession(){
        //Si el usuario ya elegio una dirección
        if (!sharedPref?.getData(Constants.ADDRESS).isNullOrBlank()){
            adress = gson.fromJson(sharedPref?.getData(Constants.ADDRESS), Address::class.java)
        }else{
            Toast.makeText(this, "Selecciona una dirección para continuar", Toast.LENGTH_LONG).show()
        }
    }
}