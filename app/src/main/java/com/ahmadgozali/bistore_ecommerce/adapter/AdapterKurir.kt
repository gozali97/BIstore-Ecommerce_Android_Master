package com.ahmadgozali.bistore_ecommerce.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.ahmadgozali.bistore_ecommerce.model.rajaongkir.Costs
import com.ahmadgozali.bistore_ecommerce.model.rajaongkir.Result
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

class AdapterKurir(var data:ArrayList<Costs>,var kurir:String, var listener: Listeners):RecyclerView.Adapter<AdapterKurir.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvLamaPengiriman = view.findViewById<TextView>(R.id.tv_lamaPengiriman)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvBerat = view.findViewById<TextView>(R.id.tv_berat)
        val rd = view.findViewById<RadioButton>(R.id.rd)
        val layout = view.findViewById<LinearLayout>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kurir, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val list = data[position]

        holder.rd.isChecked = list.isActive
        holder.tvNama.text = kurir + " "+list.service
        val cost = list.cost[0]
        holder.tvLamaPengiriman.text = cost.etd +" Hari Kerja"
        holder.tvHarga.text = Helper().gantiRupiah(cost.value)
        holder.tvBerat.text = "1 Kg x "+ Helper().gantiRupiah(cost.value)
//        holder.tvAlamat.text = list.alamat + ", "+list.kabupaten+", "+list.kecamatan+", "+list.kodepos+", ("+list.type+")"
//
//
//
        holder.rd.setOnClickListener {
            list.isActive = true
            listener.onClicked(list, holder.adapterPosition)
        }
//
//        holder.layout.setOnClickListener {
//            list.isSelected = true
//            listener.onClicked(list)
//        }
    }



    interface Listeners{
        fun onClicked(data: Costs, index: Int)
    }


}