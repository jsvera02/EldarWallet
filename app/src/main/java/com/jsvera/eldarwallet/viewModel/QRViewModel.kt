package com.jsvera.eldarwallet.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.data.remote.request.QRCodeService
import com.jsvera.eldarwallet.utils.BitmapConverterFactory
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class QRViewModel(application: Application) : AndroidViewModel(application) {

    private val qrCodeService: QRCodeService by lazy {
        Retrofit.Builder()
            .baseUrl("https://neutrinoapi-qr-code.p.rapidapi.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("X-RapidAPI-Key", "03081e8c64msha15e280819e3cadp1da469jsna8f1564dfaa2")
                            .addHeader("X-RapidAPI-Host", "neutrinoapi-qr-code.p.rapidapi.com")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .addConverterFactory(BitmapConverterFactory())
            .build()
            .create(QRCodeService::class.java)
    }

    private val _qrCodeLiveData = MutableLiveData<Resource<Bitmap>>()
    val qrCodeLiveData: LiveData<Resource<Bitmap>> get() = _qrCodeLiveData

    fun generateQR(userName: String, lastName: String) {
        viewModelScope.launch {
            try {
                _qrCodeLiveData.postValue(Resource.Loading())

                val response = qrCodeService.generateQRCode(
                    "$userName $lastName",
                    "03081e8c64msha15e280819e3cadp1da469jsna8f1564dfaa2",
                    320,
                    320,
                    "#4586BE",
                    "#131722"
                )

                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val imageBytes: ByteArray = response.body()?.bytes() ?: byteArrayOf()
                            val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            _qrCodeLiveData.postValue(Resource.Success(bitmap))
                        } else {
                            _qrCodeLiveData.postValue(Resource.Error(Exception("Failed to generate QR code")))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        _qrCodeLiveData.postValue(Resource.Error(Exception("Failed to generate QR code")))
                    }
                })
            } catch (e: Exception) {
                _qrCodeLiveData.postValue(Resource.Error(e))
            }
        }
    }
}

