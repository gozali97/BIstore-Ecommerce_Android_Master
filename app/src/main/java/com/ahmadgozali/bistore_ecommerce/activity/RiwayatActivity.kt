package com.ahmadgozali.bistore_ecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterProduk
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterRiwayat
import com.ahmadgozali.bistore_ecommerce.app.ApiConfig
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.helper.SharedPref
import com.ahmadgozali.bistore_ecommerce.model.ResponModel
import com.ahmadgozali.bistore_ecommerce.model.Transaksi
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)
        Helper().setToolbar(this, toolbar, "Riwayat Transaksi")
    }

    fun getRiwayat(){

        val id = SharedPref(this).getUser()!!.id

        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel> {

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayRiwayat(res.transaksis)
                }
            }

        })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat.adapter = AdapterRiwayat(transaksis, object : AdapterRiwayat.Listeners{
            override fun onClicked(data: Transaksi) {
                val json = Gson().toJson(data, Transaksi::class.java)
                val intent = Intent(this@RiwayatActivity, DetailsTransaksiActivity::class.java)
                intent.putExtra("transaksi", json)
                startActivity(intent)

            }

        })
        rv_riwayat.layoutManager = layoutManager
    }

    override fun onResume() {
        getRiwayat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}