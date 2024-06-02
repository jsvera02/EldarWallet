package com.jsvera.eldarwallet.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null,
    @SerializedName("user_name")
    var userName: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("cards")
    var cards: MutableList<Card>? = mutableListOf(),
    @SerializedName("balance")
    var balance: Long = 100000,
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("qr")
    var qr: Bitmap? = null
)
