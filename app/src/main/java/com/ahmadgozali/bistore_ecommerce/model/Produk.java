package com.ahmadgozali.bistore_ecommerce.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "keranjang") // the name of tabel
public class Produk implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;


    public int id;
    public String nama_produk;
    public String harga;
    public int kategori_id;
    public String deskripsi;
    public String gambar;
    public String stok;
    public String model;
    public String berat;
    public String created_at;
    public String updated_at;

    public int jumlah = 1;
    public boolean selected = true;


}
