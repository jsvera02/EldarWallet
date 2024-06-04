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
    @Query("SELECT * FROM UserEntity WHERE userName == :userName AND password == :password")
    suspend fun getUser(userName: String, password: String): List<UserWithCards>?

    @Query("SELECT * FROM UserEntity WHERE userName = :username AND password = :password")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE userName = :userName")
    suspend fun getUserByUsername(userName: String): UserEntity?

}
