package com.jsvera.eldarwallet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "balance") var balance: Long
)
