package com.ahmadgozali.bistore_ecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterBank
import com.ahmadgozali.bistore_ecommerce.app.ApiConfig
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.model.Bank
import com.ahmadgozali.bistore_ecommerce.model.Checkout
import com.ahmadgozali.bistore_ecommerce.model.ResponModel
import com.ahmadgozali.bistore_ecommerce.model.Transaksi
import com.google.gson.Gson
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        Helper().setToolbar(this, toolbar, "Pembayaran")


    displayBank()

    }

    fun displayBank(){
        val arrbank = ArrayList<Bank>()
        arrbank.add(Bank("BCA", "5180411001", "Ahmad Gozali", R.drawable.logo_bca))
        arrbank.add(Bank("Mandiri", "5180411002", "Ahmad Gozali", R.drawable.logo_mandiri))
        arrbank.add(Bank("BRI", "5180411003", "Ahmad Gozali", R.drawable.logo_bri))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_bank.layoutManager = layoutManager
        rv_bank.adapter = AdapterBank(arrbank, object : AdapterBank.Listeners{
            override fun onClicked(data: Bank, index: Int) {

                bayar(data)
            }

        })
    }

    fun bayar(bank: Bank){

        val json = intent.getStringExtra("extra")!!.toString()

        val checkout = Gson().fromJson(json, Checkout::class.java)
        checkout.bank = bank.namaBank

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading..")

        ApiConfig.instanceRetrofit.checkout(checkout).enqueue(object : Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                if (!response.isSuccessful) {
                    error(response.message())
                    return
                }

                val respon = response.body()!!

                if (respon.success == 1){

                val jsBank = Gson().toJson(bank, Bank::class.java)
                val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                val jsCheckout = Gson().toJson(checkout, Checkout::class.java)

                val intent = Intent(this@PembayaranActivity, SuccessActivity::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksi)
                    intent.putExtra("checkout", jsCheckout)
                    startActivity(intent)

                }else{
                    error(respon.message)
                    Toast.makeText(this@PembayaranActivity, "Error:"+respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
//                Toast.makeText(this@PembayaranActivity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun error(pesan: String){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}