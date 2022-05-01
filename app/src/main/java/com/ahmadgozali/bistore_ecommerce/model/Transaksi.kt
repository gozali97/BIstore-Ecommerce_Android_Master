package com.ahmadgozali.bistore_ecommerce.model

class Transaksi {
    var id = 0
    var bank = ""
    var jasa_pengiriman = ""
    var kurir = ""
    var nama_penerima = ""
    var no_tlp = ""
    var ongkir = ""
    var total_bayar = ""
    var detail_lokasi = ""
    var total_harga = ""
    var total_produk = ""
    var user_id = ""
    var kode_payment = ""
    var kode_trx = ""
    var kode_unik = 0
    var status = ""
    var expired_at = ""
    var updated_at = ""
    var created_at = ""
    val details = ArrayList<DetailsTransaksi>()
}