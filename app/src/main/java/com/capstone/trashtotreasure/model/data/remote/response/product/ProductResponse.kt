package com.capstone.trashtotreasure.model.data.remote.response.product

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("payload")
	val payload: List<PayloadItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PayloadItem(

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

	@field:SerializedName("penjual")
	val penjual: Penjual? = null,

	@field:SerializedName("foto_produk")
	val fotoProduk: String? = null
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
