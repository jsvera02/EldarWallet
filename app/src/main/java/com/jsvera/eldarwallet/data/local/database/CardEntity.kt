package com.jsvera.eldarwallet.data.local.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.jsvera.eldarwallet.data.local.entities.Card

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0,
    val user_id: Long, // Ensure this matches the column name in the relation
    val card_number: String,
    val name: String,
    val card_holder_name: String,
    val card_type: String,
    val expiration_date: String,
    val dni: String
){
    fun toCard(): Card {
        return Card(
            cardId = this.cardId,
            userId = this.user_id,
            cardNumber = this.card_number,
            name = this.name,
            cardHolderName = this.card_holder_name,
            cardType = this.card_type,
            expirationDate = this.expiration_date,
            dni = this.dni
        )
    }
}