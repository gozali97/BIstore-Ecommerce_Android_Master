package com.ahmadgozali.bistore_ecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.helper.SharedPref
import com.ahmadgozali.bistore_ecommerce.model.Produk
import com.ahmadgozali.bistore_ecommerce.room.MyDatabase
import com.ahmadgozali.bistore_ecommerce.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbar_detail.*

lateinit var produk: Produk
lateinit var myDb: MyDatabase


class DetailProdukActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)
        myDb = MyDatabase.getInstance(this)!! // call database

        getInfo()
        mainButton()
        cekKeranjang()
    }

    var listProduk = ArrayList<Produk>()
    var totalHarga = 0
   private fun mainButton(){
        btn_keranjang.setOnClickListener {
            val data = myDb.daoKeranjang().getProduk(produk.id)
            if(data == null){
                insert()
            }else{
                data.jumlah += 1
                update(data)
            }
        }

        btn_favorit.setOnClickListener {
            val listData = myDb.daoKeranjang().getAll() // get All data
            for(note :Produk in listData){
                println("-----------------------")
                println(note.nama_produk)
                println(note.harga)
            }
        }

       btn_to_Keranjang.setOnClickListener {
           val intent = Intent("event:keranjang")
           LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
           onBackPressed()
       }

       btn_bayar.setOnClickListener {

       }


    }

   private fun insert(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                cekKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil dimasukkan keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun update(data:Produk){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                cekKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil dimasukkan keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun cekKeranjang(){
        val dataKeranjang = myDb.daoKeranjang().getAll()

        if(myDb.daoKeranjang().getAll().isNotEmpty()){
            div_angka_keranjang.visibility = View.VISIBLE
            tv_angka_keranjang.text = dataKeranjang.size.toString()
        }else{
            div_angka_keranjang.visibility = View.GONE
        }
    }


   private fun getInfo(){
        val data = intent.getStringExtra("extra")
        produk = Gson().fromJson<Produk>(data, Produk::class.java)

        //set value
        tv_nama.text = produk.nama_produk
        tv_harga.text = Helper().gantiRupiah(produk.harga)
        tv_deskripsi.text = produk.deskripsi

        val image = Config.produkUrl + produk.gambar
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.produk)
            .error(R.drawable.produk)
            .resize(500, 400)
            .into(im_produk)

        //setToolbar
        Helper().setToolbar(this, toolbar, produk.nama_produk)
//        setSupportActionBar(toolbar)
//        supportActionBar!!.title = produk.nama_produk
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}