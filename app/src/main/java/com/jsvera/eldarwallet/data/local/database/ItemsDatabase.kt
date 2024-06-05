package com.jsvera.eldarwallet.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.data.local.entities.UserEntity

@Database(entities = [UserEntity::class, CardEntity::class], version = 4, exportSchema = false)

abstract class ItemsDatabase : RoomDatabase() {
    abstract fun getItemsDao(): ApplicationDao
    abstract fun cardDao(): CardDao
}
