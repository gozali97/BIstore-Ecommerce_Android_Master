package com.ahmadgozali.bistore_ecommerce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.ahmadgozali.bistore_ecommerce.R
import com.ahmadgozali.bistore_ecommerce.app.ApiConfigAlamat
import com.ahmadgozali.bistore_ecommerce.helper.Helper
import com.ahmadgozali.bistore_ecommerce.model.Alamat
import com.ahmadgozali.bistore_ecommerce.model.ModelAlamat
import com.ahmadgozali.bistore_ecommerce.model.ResponModel
import com.ahmadgozali.bistore_ecommerce.room.MyDatabase
import com.ahmadgozali.bistore_ecommerce.util.ApiKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_alamat.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.edt_nama
import kotlinx.android.synthetic.main.activity_tambah_alamat.edt_phone
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahAlamatActivity : AppCompatActivity() {

    var provinsi = ModelAlamat.Provinsi()
    var kota = ModelAlamat.Provinsi()
    var kecamatan = ModelAlamat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)
        Helper().setToolbar(this, toolbar, "Tambah Alamat")

        mainButton()
        getProvinsi()

    }

    private fun mainButton(){
        btn_simpan.setOnClickListener {
                simpan()
        }
    }

    private fun simpan(){
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

        if (provinsi.province_id == "0"){
            toast("Silahkan pilih provinsi")
            return
        }
        if (kota.city_id == "0"){
            toast("Silahkan pilih kabupaten")
            return
        }
//        if (kecamatan.id == 0){
//            toast("Silahkan pilih kecamatan")
//            return
//        }

        val alamat = Alamat()
        alamat.nama_lengkap = edt_nama.text.toString()
        alamat.no_tlp = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

        alamat.id_prov = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province
        alamat.id_kab = Integer.valueOf(kota.city_id)
        alamat.kabupaten = kota.city_name
//        alamat.id_kec = kecamatan.id
//        alamat.kecamatan = kecamatan.nama

        insert(alamat)

    }

    fun toast(string: String){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    fun error(editText: EditText){
        editText.error = "Kolom tidak boleh kosong"
        editText.requestFocus()
    }

    private fun getProvinsi(){
        ApiConfigAlamat.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object : Callback<ResponModel> {

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    div_provinsi.visibility = View.VISIBLE

                val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Provinsi")

                    val listProvinsi = res.rajaongkir.results
                    for (prov in listProvinsi){
                        arryString.add(prov.province)
                    }


                val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spn_provinsi.adapter = adapter
                    spn_provinsi.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?,view: View?, position: Int, id: Long) {
                            if  (position != 0){
                                provinsi = listProvinsi[position - 1]
                                val idProv = provinsi.province_id
                                getKota(idProv)
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
                }else{
                        Log.d("Error", "gagal memuat data" + response.message())
                }
            }

        })
    }

    private fun getKota(id: String){
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKota(ApiKey.key, id).enqueue(object : Callback<ResponModel> {

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kota")

                    val listKota = res.rajaongkir.results
                    for (kota in listKota){
                        arryString.add(kota.type+" "+kota.city_name)
                    }


                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter
                    spn_kota.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position != 0) {
                                kota = listKota[position - 1]
                                val kodePos = kota.postal_code
                                edt_kodePos.setText(kodePos)
//                                val idKota = listKota[position - 1].city_id
//                                getKecamatan(idKota)
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }

                    }else{
                    Log.d("Error", "gagal memuat data" + response.message())
                }
            }

        })
    }

    private fun getKecamatan(id: Int){
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponModel> {

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kecamatan")

                    val listKecamatan = res.kecamatan
                    for (data in res.kecamatan){
                        arryString.add(data.nama)
                    }


                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                    spn_kecamatan.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position != 0) {
                                kecamatan = listKecamatan[position - 1]
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }

                }else{
                    Log.d("Error", "gagal memuat data" + response.message())
                }
            }

        })
    }

    private fun insert(data: Alamat){
        val myDb =MyDatabase.getInstance(this)!!
        if(myDb.daoAlamat().getByStatus(true) == null){
            data.isSelected = true
        }

        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                toast("Insert Data Sukses")
                onBackPressed()
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}