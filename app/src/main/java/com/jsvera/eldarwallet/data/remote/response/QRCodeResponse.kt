package com.jsvera.eldarwallet.data.remote.response

import com.google.gson.annotations.SerializedName

data class QRCodeResponse(
    @SerializedName("image") val imageUrl: String
)
