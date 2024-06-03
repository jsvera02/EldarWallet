package com.jsvera.eldarwallet.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import androidx.core.view.isGone

    fun View.visible() {
        this.isGone = false
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

fun maskEmail(idx: Int, s: String): String {
    var emailResult: String = ""
    when (idx) {
        1 -> {
            val p = """^([^@]{0})([^@]+)([^@]{0}@)""".toRegex()
            emailResult = (s.replace(p) {
                it.groupValues[1] + "*".repeat(it.groupValues[2].length) + it.groupValues[3]
            })
        }

        2 -> {
            val p = """^([^@]{0})([^@]+)([^@]{1}@)""".toRegex()
            emailResult = (s.replace(p) {
                it.groupValues[1] + "*".repeat(it.groupValues[2].length) + it.groupValues[3]
            })
        }

        3 -> {
            val p = """^([^@]{0})([^@]+)([^@]{2}@)""".toRegex()
            emailResult = (s.replace(p) {
                it.groupValues[1] + "*".repeat(it.groupValues[2].length) + it.groupValues[3]
            })
        }

        else -> {
            val p = """^([^@]{0})([^@]+)([^@]{3}@)""".toRegex()
            emailResult = (s.replace(p) {
                it.groupValues[1] + "*".repeat(it.groupValues[2].length) + it.groupValues[3]
            })
        }
    }
    return emailResult
}
fun Context.isConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
        else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true
        else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true
    }
    return false
}