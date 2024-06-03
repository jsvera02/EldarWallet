package com.jsvera.eldarwallet

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.jsvera.eldarwallet.data.local.database.ItemsDatabase

class EldarWalletApp : Application() {
    private lateinit var databaseInstance: ItemsDatabase

    companion object {
        lateinit var instance: EldarWalletApp
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun getDatabase(context: Context): ItemsDatabase {
        synchronized(ItemsDatabase::class.java) {
            if (!::databaseInstance.isInitialized) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    ItemsDatabase::class.java,
                    "application_db"
                ).fallbackToDestructiveMigration()
                databaseInstance = builder.build()
            }
        }
        return databaseInstance
    }

}