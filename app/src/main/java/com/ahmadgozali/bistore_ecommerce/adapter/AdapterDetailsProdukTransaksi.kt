package com.ahmadgozali.bistore_ecommerce.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ahmadgozali.bistore_ecommerce.MainActivity
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.activity.DetailProdukActivity
import com.ahmadgozali.bistore_ecommerce.activity.LoginActivity
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.model.Alamat
import com.ahmadgozali.bistore_ecommerce.model.DetailsTransaksi
import com.ahmadgozali.bistore_ecommerce.model.Produk
import com.ahmadgozali.bistore_ecommerce.model.Transaksi
import com.ahmadgozali.bistore_ecommerce.room.MyDatabase
import com.ahmadgozali.bistore_ecommerce.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterDetailsProdukTransaksi(var data:ArrayList<DetailsTransaksi>):RecyclerView.Adapter<AdapterDetailsProdukTransaksi.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val tvharga = view.findViewById<TextView>(R.id.tv_harga)
        val tvTotalHarga = view.findViewById<TextView>(R.id.tv_totalHarga)
        val tvTanggal = view.findViewById<TextView>(R.id.tv_tgl)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk_transaksi, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val list = data[position]

        val namaProduk = list.produk.nama_produk
        val p = list.produk
        holder.tvNama.text = namaProduk
        holder.tvharga.text = Helper().gantiRupiah(p.harga)
        holder.tvTotalHarga.text = Helper().gantiRupiah(list.total_harga)
        holder.tvJumlah.text = list.total_item.toString() + " Items"

        val image = Config.produkUrl + p.gambar
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.produk)
            .error(R.drawable.produk)
            .into(holder.imgProduk)



        holder.layout.setOnClickListener {
//            listener.onClicked(list)
        }
    }



    interface Listeners{
        fun onClicked(data: DetailsTransaksi)
    }


}