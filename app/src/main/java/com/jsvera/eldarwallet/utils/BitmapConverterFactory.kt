package com.jsvera.eldarwallet.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class BitmapConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return if (type == Bitmap::class.java) {
            BitmapResponseBodyConverter()
        } else {
            null
        }
    }

    private class BitmapResponseBodyConverter : Converter<ResponseBody, Bitmap> {
        override fun convert(value: ResponseBody): Bitmap? {
            return BitmapFactory.decodeStream(value.byteStream())
        }
    }
}