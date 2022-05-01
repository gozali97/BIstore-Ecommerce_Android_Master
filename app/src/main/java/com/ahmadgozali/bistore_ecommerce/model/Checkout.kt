package com.ahmadgozali.bistore_ecommerce.model

class Checkout {
    lateinit var user_id: String
    lateinit var total_produk: String
    lateinit var total_harga: String
    lateinit var nama_penerima: String
    lateinit var no_tlp: String
    lateinit var kurir: String
    lateinit var detail_lokasi: String
    lateinit var jasa_pengiriman: String
    lateinit var ongkir: String
    lateinit var total_bayar: String
    lateinit var bank: String
    var produks = ArrayList<Item>()

    class Item{
        lateinit var id: String
        lateinit var total_item: String
        lateinit var total_harga: String
        lateinit var catatan: String
    }
}