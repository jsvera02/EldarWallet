package com.jsvera.eldarwallet.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jsvera.eldarwallet.data.local.entities.CardEntity

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: CardEntity): Long

    @Query("SELECT * FROM cards WHERE user_id = :userId")
    suspend fun getCardsByUserId(userId: Long): List<CardEntity>
}