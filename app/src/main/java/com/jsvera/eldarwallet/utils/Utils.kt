package com.jsvera.eldarwallet.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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
fun addAutoFormatExpirationDateWatcher(editText: EditText) {
    val textWatcher = object : TextWatcher {
        private var isEditing = false

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(editable: Editable) {
            if (isEditing) return

            isEditing = true

            val input = editable.toString().replace("/", "")
            val formatted = when {
                input.length >= 3 -> "${input.substring(0, 2)}/${input.substring(2)}"
                else -> input
            }

            editable.replace(0, editable.length, formatted)

            isEditing = false
        }
    }

    editText.addTextChangedListener(textWatcher)
}