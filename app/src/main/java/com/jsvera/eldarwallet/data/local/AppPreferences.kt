package com.jsvera.eldarwallet.data.local

import android.content.Context
import com.google.gson.Gson
import com.jsvera.eldarwallet.di.EldarWalletApp
import com.jsvera.eldarwallet.data.local.entities.User

object AppPreferences {
    private const val USER = "USER"
    private const val APP_PREFERENCES = "APP_PREFERENCES"

    private fun getPreferences() = EldarWalletApp.instance.getSharedPreferences(
        APP_PREFERENCES, Context.MODE_PRIVATE
    )

    fun getUser(): User? {
        val json = getPreferences().getString(USER, "")
        return Gson().fromJson(json, User::class.java)
    }

    fun setUser(user: User?) = getPreferences().edit()?.putString(USER, Gson().toJson(user))?.apply()


}