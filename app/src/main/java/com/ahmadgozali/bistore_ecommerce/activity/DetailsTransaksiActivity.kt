package com.ahmadgozali.bistore_ecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterDetailsProdukTransaksi
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterRiwayat
import com.ahmadgozali.bistore_ecommerce.app.ApiConfig
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.model.DetailsTransaksi
import com.ahmadgozali.bistore_ecommerce.model.ResponModel
import com.ahmadgozali.bistore_ecommerce.model.Transaksi
import com.google.gson.Gson
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_details_transaksi.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsTransaksiActivity : AppCompatActivity() {

    var transaksi = Transaksi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_transaksi)
        Helper().setToolbar(this, toolbar, "Details Produk Transaksi")


        val json  = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson(json, Transaksi::class.java)

        setData(transaksi)
        displayDetails(transaksi.details)
        mainButtom()

    }

    fun mainButtom(){
        btn_batal.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Transaksi akan dibatalkan dan tidak bisa dikembalikan!")
                .setConfirmText("Yes,batalkan transaksi!")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    batalTransaksi()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }
                .show()
        }

    }

    fun batalTransaksi(){
        val loading = SweetAlertDialog(this@DetailsTransaksiActivity, SweetAlertDialog.PROGRESS_TYPE)
            loading.setTitleText("Loading..")
        ApiConfig.instanceRetrofit.batalCheckout(transaksi.id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1){
                    SweetAlertDialog(this@DetailsTransaksiActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success...")
                        .setContentText("Transaksi dibatalkan!")
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            onBackPressed()
                        }
                        .show()
//                    Toast.makeText(this@DetailsTransaksiActivity, "Transaksi berhasil di batalkan", Toast.LENGTH_SHORT).show()
//                    onBackPressed()
                }
            }

        })
    }

    fun error(pesan: String){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }

    fun setData(transaksi: Transaksi) {
        tv_status.text = transaksi.status
        val formatBaru = "dd MMMM yyyy, kk:mm:ss "
        tv_tgl.text = Helper().convertTanggal(transaksi.created_at, formatBaru)
        tv_penerima.text = transaksi.nama_penerima+" - "+ transaksi.no_tlp
        tv_alamat.text = transaksi.detail_lokasi
        tv_kodeUnik.text = Helper().gantiRupiah(transaksi.kode_unik)
        tv_totalBelanja.text = Helper().gantiRupiah(transaksi.total_harga)
        tv_ongkir.text = Helper().gantiRupiah(transaksi.ongkir)
        tv_total.text = Helper().gantiRupiah(transaksi.total_bayar)

        if(transaksi.status != "Menunggu Pembayaran") div_footer.visibility = View.GONE

        var color = getColor(R.color.menungguPembayaran)
        if(transaksi.status == "Selesai") color = getColor(R.color.selesai)
        else if (transaksi.status == "Pembayaran Dikonfirmasi") color = getColor(R.color.pembayaranDikonfirmasi)
        else if (transaksi.status == "Packing") color = getColor(R.color.packing)
        else if (transaksi.status == "Dikirim") color = getColor(R.color.dikirim)
        else if (transaksi.status == "Batal") color = getColor(R.color.batal)

        tv_status.setTextColor(color)
    }

    fun displayDetails(transaksis: ArrayList<DetailsTransaksi>){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_produk.adapter = AdapterDetailsProdukTransaksi(transaksis)
        rv_produk.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}