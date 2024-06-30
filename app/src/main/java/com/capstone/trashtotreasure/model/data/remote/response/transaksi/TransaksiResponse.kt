package com.capstone.trashtotreasure.model.data.remote.response.transaksi

import com.google.gson.annotations.SerializedName

data class TransaksiResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Pagination(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("max")
	val max: String? = null,

	@field:SerializedName("prev")
	val prev: String? = null
)

data class Payload(

	@field:SerializedName("id_produk")
	val idProduk: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id_penjual")
	val idPenjual: Int? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("invoice_id")
	val invoiceId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("total_harga")
	val totalHarga: Int? = null,

	@field:SerializedName("id_transaksi")
	val idTransaksi: Int? = null,

	@field:SerializedName("invoice_url")
	val invoiceUrl: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("id_pembeli")
	val idPembeli: Int? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
)
