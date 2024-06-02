package com.jsvera.eldarwallet

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.jsvera.eldarwallet.database.ItemsDatabase

class EldarWallerApp : Application() {

    private lateinit var databaseInstance: ItemsDatabase

    companion object {
        lateinit var instance: EldarWallerApp
        lateinit var appContext: Context
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

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}
