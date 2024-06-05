package com.jsvera.eldarwallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val userName: String,
    val name: String,
    val lastName: String,
    val password: String,
    val balance: Long
)
