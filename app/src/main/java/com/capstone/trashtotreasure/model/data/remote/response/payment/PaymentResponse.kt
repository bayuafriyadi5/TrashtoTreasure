package com.capstone.trashtotreasure.model.data.remote.response.payment

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Payload(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("available_paylaters")
	val availablePaylaters: List<AvailablePaylatersItem?>? = null,

	@field:SerializedName("metadata")
	val metadata: Any? = null,

	@field:SerializedName("available_banks")
	val availableBanks: List<AvailableBanksItem?>? = null,

	@field:SerializedName("available_ewallets")
	val availableEwallets: List<AvailableEwalletsItem?>? = null,

	@field:SerializedName("available_retail_outlets")
	val availableRetailOutlets: List<Any?>? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("expiry_date")
	val expiryDate: String? = null,

	@field:SerializedName("merchant_name")
	val merchantName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("available_qr_codes")
	val availableQrCodes: List<AvailableQrCodesItem?>? = null,

	@field:SerializedName("external_id")
	val externalId: String? = null,

	@field:SerializedName("available_direct_debits")
	val availableDirectDebits: List<AvailableDirectDebitsItem?>? = null,

	@field:SerializedName("merchant_profile_picture_url")
	val merchantProfilePictureUrl: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("should_send_email")
	val shouldSendEmail: Boolean? = null,

	@field:SerializedName("should_exclude_credit_card")
	val shouldExcludeCreditCard: Boolean? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("invoice_url")
	val invoiceUrl: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class AvailableEwalletsItem(

	@field:SerializedName("ewallet_type")
	val ewalletType: String? = null
)

data class AvailableQrCodesItem(

	@field:SerializedName("qr_code_type")
	val qrCodeType: String? = null
)

data class AvailableBanksItem(

	@field:SerializedName("bank_code")
	val bankCode: String? = null,

	@field:SerializedName("bank_branch")
	val bankBranch: String? = null,

	@field:SerializedName("transfer_amount")
	val transferAmount: Int? = null,

	@field:SerializedName("account_holder_name")
	val accountHolderName: String? = null,

	@field:SerializedName("identity_amount")
	val identityAmount: Int? = null,

	@field:SerializedName("collection_type")
	val collectionType: String? = null
)

data class Pagination(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("max")
	val max: String? = null,

	@field:SerializedName("prev")
	val prev: String? = null
)

data class AvailableDirectDebitsItem(

	@field:SerializedName("direct_debit_type")
	val directDebitType: String? = null
)

data class AvailablePaylatersItem(

	@field:SerializedName("paylater_type")
	val paylaterType: String? = null
)
