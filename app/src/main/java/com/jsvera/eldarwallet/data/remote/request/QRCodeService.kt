package com.jsvera.eldarwallet.data.remote.request

import com.jsvera.eldarwallet.data.remote.response.QRCodeResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QRCodeService {
    @POST("qr-code")
    fun generateQRCode(
        @Query("content") content: String,
        @Query("apiKey") apiKey: String,
        @Query("width") width: Int,
        @Query("height") height: Int,
        @Query("fg-color") fgColor: String,
        @Query("bg-color") bgColor: String
    ): Call<ResponseBody>
}