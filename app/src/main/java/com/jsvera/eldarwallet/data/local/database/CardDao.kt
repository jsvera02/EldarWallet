package com.jsvera.eldarwallet.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: CardEntity): Long

    @Query("SELECT * FROM cardentity WHERE user_id = :userId")
    suspend fun getCardsByUserId(userId: Int): List<CardEntity>
}