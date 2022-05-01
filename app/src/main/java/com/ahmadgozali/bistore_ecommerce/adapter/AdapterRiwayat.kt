package com.ahmadgozali.bistore_ecommerce.adapter

import android.app.Activity
import android.content.Context
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterRiwayat(var data:ArrayList<Transaksi>, var listener: Listeners):RecyclerView.Adapter<AdapterRiwayat.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvharga = view.findViewById<TextView>(R.id.tv_harga)
        val tvTanggal = view.findViewById<TextView>(R.id.tv_tgl)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val tvStatus = view.findViewById<TextView>(R.id.tv_status)
        val btnDetail = view.findViewById<TextView>(R.id.btn_detail)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val list = data[position]

        val namaProduk = list.details[0].produk.nama_produk
        holder.tvNama.text = namaProduk
        holder.tvharga.text = Helper().gantiRupiah(list.total_bayar)
        holder.tvJumlah.text = list.total_produk + " Items"
        holder.tvStatus.text = list.status

        val formatBaru = "d MMM yyyy "
        holder.tvTanggal.text = Helper().convertTanggal(list.created_at, formatBaru)

        var color = context.getColor(R.color.menunggu)
        if(list.status == "Selesai") color = context.getColor(R.color.selesai)
        else if (list.status == "Batal") color = context.getColor(R.color.batal)

        holder.tvStatus.setTextColor(color)


        holder.layout.setOnClickListener {
            listener.onClicked(list)
        }
    }



    interface Listeners{
        fun onClicked(data: Transaksi)
    }


}