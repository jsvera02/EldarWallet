package com.jsvera.eldarwallet

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class EldarWallerApp : Application() {

    companion object {
        lateinit var instance: EldarWallerApp
        fun isInitialized() = this::instance.isInitialized
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //SET ACTIVE CRASHLYTICS ONLY ON PROD
    }
}