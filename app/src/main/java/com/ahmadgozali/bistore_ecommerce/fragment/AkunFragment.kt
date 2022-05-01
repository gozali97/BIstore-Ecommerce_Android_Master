package com.ahmadgozali.bistore_ecommerce.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ahmadgozali.bistore_ecommerce.MainActivity
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.activity.ListAlamatActivity
import com.ahmadgozali.bistore_ecommerce.activity.LoginActivity
import com.ahmadgozali.bistore_ecommerce.activity.RiwayatActivity
import com.ahmadgozali.bistore_ecommerce.helper.SharedPref


class AkunFragment : Fragment() {


    lateinit var s:SharedPref
    lateinit var btnLogout: TextView
    lateinit var tvNama: TextView
    lateinit var tvPhone: TextView
    lateinit var tvEmail: TextView

    lateinit var btnRiwayat : RelativeLayout
    lateinit var btnAlamat : RelativeLayout
    lateinit var btnKeranjang : RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
        init(view)

        s = SharedPref(activity!!)

        mainButtom()

        setData()
        return view
    }

    fun mainButtom(){
        btnLogout.setOnClickListener{
            s.setStatusLogin(false)
        }

        btnRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatActivity::class.java))
        }

        btnAlamat.setOnClickListener {
            startActivity(Intent(requireActivity(), ListAlamatActivity::class.java))
        }

        btnKeranjang.setOnClickListener {
        }

    }

    fun setData(){

        if (s.getUser() == null){
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }
        val user = s.getUser()!!

        tvNama.text = user.name
        tvPhone.text = user.phone
        tvEmail.text = user.email
    }

    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvPhone = view.findViewById(R.id.tv_phone)
        tvEmail = view.findViewById(R.id.tv_email)
        btnRiwayat = view.findViewById(R.id.btn_riwayat)
        btnAlamat = view.findViewById(R.id.btn_alamat)
        btnKeranjang = view.findViewById(R.id.btn_keranjang)
    }

    


}