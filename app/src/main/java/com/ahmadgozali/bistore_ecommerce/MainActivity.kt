package com.ahmadgozali.bistore_ecommerce

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ahmadgozali.bistore_ecommerce.activity.LoginActivity
import com.ahmadgozali.bistore_ecommerce.activity.MasukActivity
import com.ahmadgozali.bistore_ecommerce.fragment.AkunFragment
import com.ahmadgozali.bistore_ecommerce.fragment.HomeFragment
import com.ahmadgozali.bistore_ecommerce.fragment.KeranjangFragment
import com.ahmadgozali.bistore_ecommerce.helper.SharedPref
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val fragmentHome: Fragment = HomeFragment()
    private val fragmentKeranjang: Fragment = KeranjangFragment()
    private val fragmentAkun: Fragment = AkunFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private val statusLogin = false

    private  lateinit var s:SharedPref

    private var dariDetail :Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s = SharedPref(this)

       setUpBottonNav()

        LocalBroadcastManager.getInstance(this).registerReceiver(message, IntentFilter("event:keranjang"))


    }

    val message : BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            dariDetail = true
        }

    }

    fun setUpBottonNav(){
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.navigation_home-> {
                    callFragment(0, fragmentHome)
                }
                R.id.navigation_keranjang-> {
                    callFragment(1, fragmentKeranjang)
                }
                R.id.navigation_akun-> {
                    if (s.getStatusLogin()) {
                        callFragment(2, fragmentAkun)
                    }else{
                        startActivity(Intent(this, MasukActivity::class.java))
                    }
                }
            }

            false
        }
    }

    fun callFragment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onResume() {
        if (dariDetail){
            dariDetail = false
            callFragment(1, fragmentKeranjang)
        }
        super.onResume()
    }
}