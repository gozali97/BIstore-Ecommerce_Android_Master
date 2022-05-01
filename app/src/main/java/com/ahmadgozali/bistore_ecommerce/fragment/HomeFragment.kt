package com.ahmadgozali.bistore_ecommerce.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterProduk
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterSemuaProduk
import com.ahmadgozali.bistore_ecommerce.adapter.AdapterSlider
import com.ahmadgozali.bistore_ecommerce.app.ApiConfig
import com.ahmadgozali.bistore_ecommerce.model.Produk
import com.ahmadgozali.bistore_ecommerce.model.ResponModel
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ahmadgozali.bistore_ecommerce.activity.LoginActivity
import com.ahmadgozali.bistore_ecommerce.activity.PengirimanActivity
import com.ahmadgozali.bistore_ecommerce.activity.RiwayatActivity
import kotlinx.android.synthetic.main.activity_masuk.*


class HomeFragment : Fragment() {

    lateinit var vpSlider: ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvProdukTerlairs: RecyclerView
    lateinit var rvSemuaProduk: RecyclerView

    lateinit var btnToKeranjang: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        getProduk()
        mainButton()

        return view
    }

    private fun mainButton(){
    }



    fun displayProduk(){

        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.bi1)
        arrSlider.add(R.drawable.bi2)
        arrSlider.add(R.drawable.bi3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = GridLayoutManager(activity, 2)

        rvProduk.adapter = AdapterProduk(requireActivity(), listProduk)
        rvProduk.layoutManager = layoutManager

        rvProdukTerlairs.adapter = AdapterProduk(requireActivity(), listProduk)
        rvProdukTerlairs.layoutManager = layoutManager2

        rvSemuaProduk.adapter = AdapterSemuaProduk(requireActivity(), listProduk)
        rvSemuaProduk.layoutManager = layoutManager3
    }

    private var listProduk:ArrayList<Produk> = ArrayList()

    fun getProduk(){
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponModel> {

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    listProduk = res.produks
                    displayProduk()

                }
            }

        })
    }

    fun init(view: View){
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvProdukTerlairs = view.findViewById(R.id.rv_produkterlaris)
        rvSemuaProduk = view.findViewById(R.id.rv_semuaProduk)
    }

//    val arrProduk: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val p1 = Produk()
//        p1.nama_produk = "Canon 4000D"
//        p1.harga = "Rp. 5.000.000"
//        p1.gambar = R.drawable.canon4000d
//
//        val p2 = Produk()
//        p2.nama_produk = "Canon G7X"
//        p2.harga = "Rp. 3.000.000"
//        p2.gambar = R.drawable.canong7x
//
//        val p3 = Produk()
//        p3.nama_produk = "Lensa 16-35MM"
//        p3.harga = "Rp. 2.000.000"
//        p3.gambar = R.drawable.lensatele
//
//        arr.add(p1)
//        arr.add(p2)
//        arr.add(p3)
//
//        return arr
//    }
//
//    val arrProdukTerlaris: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val p1 = Produk()
//        p1.nama_produk = "Canon 70D"
//        p1.harga = "Rp. 12.000.000"
//        p1.gambar = R.drawable.canon70d
//
//        val p2 = Produk()
//        p2.nama_produk = "Flash Kamera"
//        p2.harga = "Rp. 1.000.000"
//        p2.gambar = R.drawable.flashcamera
//
//        val p3 = Produk()
//        p3.nama_produk = "Tripod 1 Meter"
//        p3.harga = "Rp. 700.000"
//        p3.gambar = R.drawable.tripod
//
//        arr.add(p1)
//        arr.add(p2)
//        arr.add(p3)
//
//        return arr
//    }
//
//    val arrKamera: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val p1 = Produk()
//        p1.nama_produk = "Canon 4000D"
//        p1.harga = "Rp. 5.000.000"
//        p1.gambar = R.drawable.canon4000d
//
//        val p2 = Produk()
//        p2.nama_produk = "Nikon Coolpix"
//        p2.harga = "Rp. 4.000.000"
//        p2.gambar = R.drawable.nikon
//
//        val p3 = Produk()
//        p3.nama_produk = "Sony Alpha A5100"
//        p3.harga = "Rp. 4.500.000"
//        p3.gambar = R.drawable.sony
//
//        arr.add(p1)
//        arr.add(p2)
//        arr.add(p3)
//
//        return arr
//    }


}