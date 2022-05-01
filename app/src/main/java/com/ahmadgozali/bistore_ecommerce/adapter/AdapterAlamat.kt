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
import com.ahmadgozali.bistore_ecommerce.model.Produk
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

class AdapterAlamat(var data:ArrayList<Alamat>, var listener: Listeners):RecyclerView.Adapter<AdapterAlamat.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val tvAlamat = view.findViewById<TextView>(R.id.tv_alamat)
        val rdAlamat = view.findViewById<RadioButton>(R.id.rd_alamat)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_alamat, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val list = data[position]

        holder.rdAlamat.isChecked = list.isSelected
        holder.tvNama.text = list.nama_lengkap
        holder.tvPhone.text = list.no_tlp
        holder.tvAlamat.text = list.alamat + ", "+list.kabupaten+", "+list.kecamatan+", "+list.kodepos+", ("+list.type+")"



        holder.rdAlamat.setOnClickListener {
            list.isSelected = true
            listener.onClicked(list)
        }

        holder.layout.setOnClickListener {
            list.isSelected = true
            listener.onClicked(list)
        }
    }



    interface Listeners{
        fun onClicked(data: Alamat)
    }


}