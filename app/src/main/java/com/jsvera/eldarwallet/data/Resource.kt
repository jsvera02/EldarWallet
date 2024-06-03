package com.jsvera.eldarwallet.data

sealed class Resource<T>(
    val data: T? = null,
    val e: java.lang.Exception?=null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(e: java.lang.Exception, data: T? = null) : Resource<T>(data, e)
}