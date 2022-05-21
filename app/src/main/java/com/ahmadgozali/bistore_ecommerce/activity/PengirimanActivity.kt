package com.ahmadgozali.bistore_ecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadgozali.bistore_ecommerce.MainActivity
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterKurir
import com.ahmadgozali.bistore_ecommerce.app.ApiConfig
import com.ahmadgozali.bistore_ecommerce.app.ApiConfigAlamat
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.helper.SharedPref
import com.ahmadgozali.bistore_ecommerce.model.*
import com.ahmadgozali.bistore_ecommerce.model.rajaongkir.Costs
import com.ahmadgozali.bistore_ecommerce.model.rajaongkir.ResponOngkir
import com.ahmadgozali.bistore_ecommerce.model.rajaongkir.Result
import com.ahmadgozali.bistore_ecommerce.room.MyDatabase
import com.ahmadgozali.bistore_ecommerce.util.ApiKey
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.activity_pengiriman.btn_tambahAlamat
import kotlinx.android.synthetic.main.activity_pengiriman.div_kosong
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengirimanActivity : AppCompatActivity() {
    lateinit var myDb : MyDatabase
    var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        Helper().setToolbar(this, toolbar, "Pengiriman")
        myDb = MyDatabase.getInstance(this)!!

        totalHarga = Integer.valueOf(intent.getStringExtra("extra")!!)
        tv_totalBelanja.text = Helper().gantiRupiah(totalHarga)

        mainButton()
        setSpinner()
    }

    fun setSpinner(){
        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter
        spn_kurir.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    getOngkir(spn_kurir.selectedItem.toString())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    fun checkAlamat(){
        if(myDb.daoAlamat().getByStatus(true) != null){
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE


            val list = myDb.daoAlamat().getByStatus(true)!!
            tv_nama.text = list.nama_lengkap
            tv_phone.text = list.no_tlp
            tv_alamat.text = list.alamat + ", "+list.kabupaten+", "+list.kecamatan+", "+list.kodepos+", ("+list.type+")"
            btn_tambahAlamat.text = "Ubah Alamat"

            getOngkir("JNE")
        }else{
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btn_tambahAlamat.text = "Tambah Alamat"
        }
    }

    private fun mainButton(){
        btn_tambahAlamat.setOnClickListener {
            startActivity(Intent(this, ListAlamatActivity::class.java))
        }

        btn_bayar.setOnClickListener {
            bayar()
        }
    }

    private fun bayar(){

        val user = SharedPref(this).getUser()
        val penerima = myDb.daoAlamat().getByStatus(true)!!

        val lisProduk = myDb.daoKeranjang().getAll() as ArrayList
        var totalItem = 0
        var totalHarga = 0
        val produks = ArrayList<Checkout.Item>()

        for (p in lisProduk){
            if (p.selected){
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga))

                val produk = Checkout.Item()
                produk.id = "" + p.id
                produk.total_item = "" + p.jumlah
                produk.total_harga = "" + (p.jumlah + Integer.valueOf(p.harga))
                produk.catatan = "catatan baru"

                produks.add(produk)
            }
        }

        val checkout = Checkout()
        checkout.user_id = "" + user!!.id
        checkout.total_produk = " "+ totalItem
        checkout.total_harga = " "+ totalHarga
        checkout.nama_penerima = penerima.nama_lengkap
        checkout.no_tlp = penerima.no_tlp
        checkout.jasa_pengiriman = jasaKirim
        checkout.ongkir = ongkir
        checkout.kurir = kurir
        checkout.detail_lokasi = tv_alamat.text.toString()
        checkout.total_bayar = "" + (totalHarga + Integer.valueOf(ongkir))
        checkout.produks = produks
        checkout.bank = "BCA"

        val json = Gson().toJson(checkout, Checkout::class.java)
        Log.d("Respond: ", "json" +json)

/*        val intent = Intent(this, PembayaranActivity::class.java)
        intent.putExtra("extra", json)
        startActivity(intent)*/

        ApiConfig.instanceRetrofit.checkout(checkout).enqueue(object : Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                if (!response.isSuccessful) {
                    error(response.message())
                    return
                }

                val respon = response.body()!!

                if (respon.success == 1){

                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                    val jsCheckout = Gson().toJson(checkout, Checkout::class.java)

                    val intent = Intent(this@PengirimanActivity, PaymentActivity::class.java)
                    intent.putExtra("transaksi", jsTransaksi)
                    intent.putExtra("checkout", jsCheckout)
                    startActivity(intent)

                    for (produk in checkout.produks){
                        myDb.daoKeranjang().deleteById(produk.id)
                    }

                }else{
                    error(respon.message)
                    Toast.makeText(this@PengirimanActivity, "Error:"+respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                error(t.message.toString())
//                Toast.makeText(this@PembayaranActivity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getOngkir(kurir: String){
        val alamat = myDb.daoAlamat().getByStatus(true)
        val origin = "501"
        val destination = " "+alamat!!.id_kab.toString()
        val weight = 1000

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, weight, kurir.toLowerCase()).enqueue(object : Callback<ResponOngkir> {
            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {

                if (response.isSuccessful){
                    Log.d("Succes", "berhasil memuat data" + response.message())
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()){
                        displayOngkir(result[0].code.toUpperCase(), result[0].costs)
                    }

                }else{
                    Log.d("Error", "gagal memuat data" + response.message())
                }
            }

            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {

            }


        })
    }

    var ongkir = ""
    var jasaKirim = ""
    var kurir = ""

    private fun displayOngkir(_kurir: String, arrayList : ArrayList<Costs>){

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices){
            val ongkir = arrayList[i]
            if(i == 0 ){
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }
        setTotal(arrayOngkir[0].cost[0].value)
        ongkir = arrayOngkir[0].cost[0].value
        jasaKirim = arrayOngkir[0].service
        kurir = _kurir

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        var adapter: AdapterKurir? = null
        adapter = AdapterKurir(arrayOngkir, _kurir, object : AdapterKurir.Listeners{
            override fun onClicked(data: Costs, index: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for (ongkir in arrayOngkir){
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }
                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                jasaKirim = data.service
                kurir = _kurir
            }

        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setTotal(ongkir :  String){
        tv_ongkir.text = Helper().gantiRupiah(ongkir)
        tv_total.text = Helper().gantiRupiah(Integer.valueOf(ongkir) + totalHarga)
    }

    override fun onResume() {
        checkAlamat()
        super.onResume()
    }
}