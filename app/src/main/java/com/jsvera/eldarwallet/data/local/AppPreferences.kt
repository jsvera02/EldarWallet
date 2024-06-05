package com.jsvera.eldarwallet.data.local

import android.content.Context
import com.google.gson.Gson
import com.jsvera.eldarwallet.data.local.entities.User
import com.jsvera.eldarwallet.di.EldarWalletApp

object AppPreferences {
    private const val USER = "USER"
    private const val APP_PREFERENCES = "APP_PREFERENCES"
    private const val SELECTED_CARD_ID = "SELECTED_CARD_ID"
    private const val LOGGED_IN = "LOGGED_IN"

    private fun getPreferences() = EldarWalletApp.instance.getSharedPreferences(
        APP_PREFERENCES, Context.MODE_PRIVATE
    )

    fun getUser(): User? {
        val json = getPreferences().getString(USER, "")
        return Gson().fromJson(json, User::class.java)
    }

    fun setUser(user: User?) =
        getPreferences().edit()?.putString(USER, Gson().toJson(user))?.apply()


    fun getSelectedCardId(): Long {
        return getPreferences().getLong(SELECTED_CARD_ID, -1)
    }

    fun setSelectedCardId(cardId: Long) {
        getPreferences().edit().putLong(SELECTED_CARD_ID, cardId).apply()
    }

    fun isLoggedIn(): Boolean {
        return getPreferences().getBoolean(LOGGED_IN, false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        getPreferences().edit()?.putBoolean(LOGGED_IN, loggedIn)?.apply()
    }
}