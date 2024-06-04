package com.jsvera.eldarwallet.data.local.entities

import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("card_id")
    var cardId: Long? = null,

    @SerializedName("user_id")
    var userId: Long? = null,

    @SerializedName("card_number")
    var cardNumber: String? = null,

    @SerializedName("card_holder_name")
    var cardHolderName: String? = null,

    @SerializedName("card_type")
    var cardType: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("expiration_date")
    var expirationDate: String? = null,

    @SerializedName("dni")
    var dni: String,

    )