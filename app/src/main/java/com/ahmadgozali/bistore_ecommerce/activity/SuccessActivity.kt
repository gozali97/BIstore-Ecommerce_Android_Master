package com.ahmadgozali.bistore_ecommerce.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ahmadgozali.bistore_ecommerce.MainActivity
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.model.Bank
import com.ahmadgozali.bistore_ecommerce.model.Checkout
import com.ahmadgozali.bistore_ecommerce.model.Transaksi
import com.ahmadgozali.bistore_ecommerce.room.MyDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_success.*
import kotlinx.android.synthetic.main.toolbar.*

class SuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        Helper().setToolbar(this, toolbar, "Bank Transfer")

        setValues()
        mainButton()
    }

    var nominal = 0

    fun mainButton(){
        btn_copyNoRek.setOnClickListener {
            copyText(tv_nomorRekening.text.toString())
        }

        btn_copyNominal.setOnClickListener {
            copyText(nominal.toString())
        }

        btn_cekStatus.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }
    }

    fun copyText(text:String){
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)

        Toast.makeText(this, "Berhasil di copy", Toast.LENGTH_SHORT).show()
    }

    fun setValues(){
       val jsBank = intent.getStringExtra("bank")
       val jsTransaksi = intent.getStringExtra("transaksi")
       val jsCheckout = intent.getStringExtra("checkout")

        val bank = Gson().fromJson(jsBank, Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)
        val checkout = Gson().fromJson(jsCheckout, Checkout::class.java)

        //hapus keranjang
        val  myDb = MyDatabase.getInstance(this)!!


        for (produk in checkout.produks){
            myDb.daoKeranjang().deleteById(produk.id)
        }

        tv_nomorRekening.text = bank.rekening
        tv_namaPenerima.text = bank.namaPenerima
        image_bank.setImageResource(bank.image)

        nominal = Integer.valueOf(transaksi.total_bayar) + transaksi.kode_unik

        tv_nominal.text = Helper().gantiRupiah(nominal)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {



        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}