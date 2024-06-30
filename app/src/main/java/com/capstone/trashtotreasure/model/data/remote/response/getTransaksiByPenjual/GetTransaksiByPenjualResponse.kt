package com.capstone.trashtotreasure.model.data.remote.response.getTransaksiByPenjual

import com.google.gson.annotations.SerializedName

data class GetTransaksiByPenjualResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("payload")
	val payload: List<PayloadItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Pembeli(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("photo_url")
	val photoUrl: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("id_pembeli")
	val idPembeli: Int? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Penjual(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id_penjual")
	val idPenjual: Int? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("no_rekening")
	val noRekening: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Pagination(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("max")
	val max: String? = null,

	@field:SerializedName("prev")
	val prev: String? = null
)

data class PayloadItem(

	@field:SerializedName("id_produk")
	val idProduk: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("total_harga")
	val totalHarga: String? = null,

	@field:SerializedName("id_transaksi")
	val idTransaksi: Int? = null,

	@field:SerializedName("status_pesanan")
	val statusPesanan: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("produk")
	val produk: Produk? = null,

	@field:SerializedName("id_penjual")
	val idPenjual: Int? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("invoice_id")
	val invoiceId: String? = null,

	@field:SerializedName("pembeli")
	val pembeli: Pembeli? = null,

	@field:SerializedName("penjual")
	val penjual: Penjual? = null,

	@field:SerializedName("invoice_url")
	val invoiceUrl: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("id_pembeli")
	val idPembeli: Int? = null
)

data class Produk(

	@field:SerializedName("id_produk")
	val idProduk: Int? = null,

	@field:SerializedName("desc_produk")
	val descProduk: String? = null,

	@field:SerializedName("nama_produk")
	val namaProduk: String? = null,

	@field:SerializedName("harga_produk")
	val hargaProduk: Int? = null,

	@field:SerializedName("id_penjual")
	val idPenjual: Int? = null,

	@field:SerializedName("stok_produk")
	val stokProduk: Int? = null,

	@field:SerializedName("foto_produk")
	val fotoProduk: String? = null
)
