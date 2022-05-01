package com.ahmadgozali.bistore_ecommerce.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.model.Bank
import com.ahmadgozali.bistore_ecommerce.model.rajaongkir.Costs
import kotlinx.android.synthetic.main.item_slider.view.*
import java.util.*
import kotlin.collections.ArrayList

class AdapterBank(var data:ArrayList<Bank>, var listener: Listeners):RecyclerView.Adapter<AdapterBank.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val imgBank = view.findViewById<ImageView>(R.id.img_bank)
        val layout = view.findViewById<LinearLayout>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_bank, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val list = data[position]
        holder.tvNama.text = list.namaBank
        holder.imgBank.setImageResource(list.image)

        holder.layout.setOnClickListener {
            listener.onClicked(list, holder.adapterPosition)
        }
    }



    interface Listeners{
        fun onClicked(data: Bank, index: Int)
    }


}