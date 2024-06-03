package com.jsvera.eldarwallet.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jsvera.eldarwallet.data.local.entities.UserWithCards

@Dao
interface ApplicationDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(cardEntity: CardEntity)

    @Transaction
    @Query("SELECT * FROM UserEntity WHERE user_name == :userName AND password == :password")
    suspend fun getUser(userName: String, password: String): List<UserWithCards>?

    @Transaction
    @Query("SELECT * FROM UserEntity WHERE user_name == :userName")
    suspend fun getUserByUsername(userName: String): List<UserWithCards>?
}
