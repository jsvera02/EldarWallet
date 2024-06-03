package com.jsvera.eldarwallet.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, CardEntity::class],
    version = 4
)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun getItemsDao(): ApplicationDao
}
