package com.ahmadgozali.bistore_ecommerce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ahmadgozali.bistore_ecommerce.MainActivity
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.app.ApiConfig
import com.ahmadgozali.bistore_ecommerce.helper.SharedPref
import com.ahmadgozali.bistore_ecommerce.model.ResponModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var s:SharedPref
    lateinit var fcm:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = SharedPref(this)
        getFcm()

        btn_login.setOnClickListener {
            login()
        }
    }

    private fun getFcm(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Respon", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            fcm = token.toString()

            // Log and toast
            Log.d("respon fcm:", token.toString())
        })
    }

    fun login(){
        if (edt_email.text.isEmpty()){
            edt_email.error = "Kolom email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_password.text.isEmpty()) {
            edt_password.error = "Kolom password tidak boleh kosong"
            edt_password.requestFocus()
            return
        }

        pb_login.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.login(edt_email.text.toString(), edt_password.text.toString(), fcm).enqueue(object : Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb_login.visibility = View.GONE

                val respon = response.body()!!

                if (respon.success == 1){
                    s.setStatusLogin(true)
                    s.setUser(respon.user)
//                    s.setString(s.nama,respon.user.name)
//                    s.setString(s.phone,respon.user.phone)
//                    s.setString(s.email,respon.user.email)


                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@LoginActivity, "Selamat Datang "+respon.user.name , Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@LoginActivity, "Error:"+respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb_login.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
}