package com.jsvera.eldarwallet.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jsvera.eldarwallet.data.local.entities.Card

@Entity
data class CardEntity(
    @PrimaryKey(autoGenerate = true) var cardId: Long = 0,
    @ColumnInfo(name = "number") var number: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "expiration_date") var expirationDate: String,
    @ColumnInfo(name = "security_code") var securityCode: String,
    @ColumnInfo(name = "document") var document: String, // corregido el nombre de la variable
    var userId: Long
) {
    fun toCard(): Card {
        return Card(
            number, name, expirationDate, securityCode, document
        )
    }
}
