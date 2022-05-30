package com.ahmadgozali.bistore_ecommerce.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.model.Checkout
import com.ahmadgozali.bistore_ecommerce.model.Transaksi
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.toolbar.*

class PaymentActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Helper().setToolbar(this, toolbar, "Pembayaran")

        val jsTransaksi = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("checkout")

        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)
        val checkout = Gson().fromJson(jsCheckout, Checkout::class.java)

        pbWebView.max = 100

        webView.isVerticalScrollBarEnabled = true
        webView.loadUrl("http://toko.baliindahphoto.xyz/payment?id_users=${checkout.user_id}&id_transaksi=${transaksi.id}")
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }

        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                try {
                    pbWebView.visibility = View.VISIBLE
                    pbWebView.progress = newProgress
                    if (newProgress == 100) {
                        pbWebView.visibility = View.GONE
                    }
                } catch (e: Exception) {}
                super.onProgressChanged(view, newProgress)
            }
        }

        pbWebView.progress = 0
    }

    override fun onSupportNavigateUp(): Boolean {
        RiwayatActivity.IS_PAYMENT = true
        startActivity(Intent(this, RiwayatActivity::class.java))
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        RiwayatActivity.IS_PAYMENT = true
        startActivity(Intent(this, RiwayatActivity::class.java))
        finish()
    }
}