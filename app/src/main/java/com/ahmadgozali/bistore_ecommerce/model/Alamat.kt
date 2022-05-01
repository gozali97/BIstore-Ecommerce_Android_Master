package com.ahmadgozali.bistore_ecommerce.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alamat") // the name of tabel
class Alamat {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    var idTb = 0

    var id = 0
    var nama_lengkap = " "
    var no_tlp = " "
    var alamat = " "
    var type = " "

    var id_prov = 0
    var id_kab = 0
    var id_kec = 0
    var provinsi = " "
    var kabupaten = " "
    var kecamatan = " "
    var kodepos = " "
    var isSelected = false

}